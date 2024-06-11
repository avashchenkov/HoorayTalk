package delivery.hooray.messagehub.service;


import delivery.hooray.adminadapter.model.SendMessageRequest;
import delivery.hooray.adminadapter.model.SendMessageResponse;
import delivery.hooray.aiassistant.model.CompleteChatRequestRecentMessagesInner;
import delivery.hooray.messagehub.enums.MessageRole;
import delivery.hooray.messagehub.service.admin.AdminAdapterMessageService;
import delivery.hooray.messagehub.service.ai.AiAssistantMessageService;
import delivery.hooray.messagehub.service.customer.CustomerAdapterMessageService;
import delivery.hooray.messagehub.model.common.AdminBotModel;
import delivery.hooray.messagehub.model.common.ChatModel;
import delivery.hooray.messagehub.model.common.MessageModel;
import delivery.hooray.messagehub.model.common.TenantModel;
import delivery.hooray.messagehub.repository.common.ChatRepository;
import delivery.hooray.messagehub.repository.common.MessageRepository;
import delivery.hooray.messagehub.repository.common.TenantRepository;
import delivery.hooray.messagehub.service.admin.MessageFromAdminAdapterDto;
import delivery.hooray.messagehub.service.customer.MessageFromCustomerAdapterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageService {
    private final Integer aiAssistantContextWindowSize = 50;  // TODO: parametrise it later for every tenant
    private final LocalDateTime aiAssistantReactivationTime = LocalDateTime.now().minusDays(7);  // TODO: parametrise it later for every tenant

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final TenantRepository tenantRepository;
    private final AiAssistantMessageService aiAssistantMessageService;
    private final AdminAdapterMessageService adminAdapterMessageService;
    private final CustomerAdapterMessageService customerAdapterMessageService;

    @Autowired
    public MessageService(ChatRepository chatRepository,
                          MessageRepository messageRepository,
                          TenantRepository tenantRepository,
                          AiAssistantMessageService aiAssistantMessageService,
                          AdminAdapterMessageService adminAdapterMessageService,
                          CustomerAdapterMessageService customerAdapterMessageService) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.tenantRepository = tenantRepository;
        this.aiAssistantMessageService = aiAssistantMessageService;
        this.adminAdapterMessageService = adminAdapterMessageService;
        this.customerAdapterMessageService = customerAdapterMessageService;
    }

    public void handleCustomerMessage(MessageFromCustomerAdapterDto messageDto) {
        TenantModel tenantModel = getTenantModel(messageDto).orElse(null);

        if (tenantModel == null) {
            return;  // TODO: The system is set up incorrectly, we should log this and notify the admin
        }

        ChatModel chatModel = getChatModel(messageDto).orElseGet(() -> {
            return null;
        });

        String adminChatId = getAdminChatId(chatModel).orElseGet(() -> {
            return null;
        });

        SendMessageRequest sendMessageRequest = buildSendMessageRequest(messageDto, tenantModel.getAdminBot(), adminChatId);

        SendMessageResponse sendMessageResponse = this.adminAdapterMessageService.sendMessageToAdmin(sendMessageRequest);

        chatModel = this.createChatModelIfNotExists(sendMessageResponse.getAdminChatId(), messageDto.getCustomerChatId(), tenantModel, chatModel);
        this.activateAiAssistantIfNecessary(chatModel, tenantModel);  // TODO: add the reactivation timestamp to the chat model

        MessageModel messageModel = new MessageModel(chatModel, MessageRole.CUSTOMER, messageDto.getMessage());

        messageRepository.save(messageModel);

        this.customerAdapterMessageService.handleCustomerCommand(messageDto);

        chatModel = chatRepository.findById(chatModel.getId()).orElse(null);

        if(chatModel != null && chatModel.getAiAssistantInstruction() != null) {
            this.aiAssistantMessageService.handleCustomerMessage(chatModel, getRecentMessages(chatModel.getId(), this.aiAssistantContextWindowSize));
        }
    }

    public void handleAdminMessage(MessageFromAdminAdapterDto messageDto) {
        ChatModel chatModel;

        try {
            chatModel = getChatModel(messageDto);
        } catch (NotFoundException e) {
            return;
        }

        MessageModel messageModel = new MessageModel(chatModel, MessageRole.ADMIN, messageDto.getMessage());

        messageRepository.save(messageModel);

        System.out.println("Handling admin message: " + messageDto.getMessage());

        TenantModel tenantModel = chatModel.getTenant();

        delivery.hooray.customeradapter.model.SendMessageRequest message =
                new delivery.hooray.customeradapter.model.SendMessageRequest(tenantModel.getCustomerBot().getId().toString(),
                        chatModel.getCustomerChatId(),
                        messageDto.getMessage());

        customerAdapterMessageService.sendMessageToCustomer(message);
        disableAiAssistantIfNecessary(chatModel);
    }

    private void disableAiAssistantIfNecessary(ChatModel chatModel) {
        if (chatModel.getAiAssistantInstruction() != null) {
            chatModel.setAiAssistantInstruction(null);

            chatRepository.save(chatModel);
        }
    }

    protected Optional<ChatModel> getChatModel(MessageFromCustomerAdapterDto messageFromCustomerAdapterDto) {
        return Optional.ofNullable(chatRepository.findByCustomerChatId(messageFromCustomerAdapterDto.getCustomerChatId()));
    }

    protected Optional<TenantModel> getTenantModel(MessageFromCustomerAdapterDto messageFromCustomerAdapterDto) {
        return Optional.ofNullable(tenantRepository.findByCustomerBot_Id(messageFromCustomerAdapterDto.getBotId()));
    }

    protected Optional<String> getAdminChatId(ChatModel chatModel) {
        if (chatModel == null) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(chatModel.getAdminChatId());
        }
    }

    private SendMessageRequest buildSendMessageRequest(MessageFromCustomerAdapterDto messageDto, AdminBotModel adminBotModel, String adminChatId) {
        SendMessageRequest sendMessageRequest = new SendMessageRequest(adminBotModel.getId().toString(),
                messageDto.getMessage());

        sendMessageRequest.setAdminChatId(adminChatId);
        sendMessageRequest.setCustomerDisplayName(messageDto.getCustomerDisplayName());

        return sendMessageRequest;
    }

    private ChatModel createChatModelIfNotExists(String adminChatId,
                                            String customerChatId,
                                            TenantModel tenantModel,
                                            ChatModel chatModel) {
        if (chatModel == null) {
            chatModel = new ChatModel(customerChatId, adminChatId, tenantModel);

            chatModel.setAiAssistantInstruction(tenantModel.getAiAssistantStartInstruction());

            this.chatRepository.save(chatModel);

        }

        return chatModel;
    }

    private void activateAiAssistantIfNecessary(ChatModel chatModel, TenantModel tenantModel) {
        List<MessageModel> messages = getMessages(chatModel.getId(), 1);

        if (!messages.isEmpty()) {
            MessageModel latestMessage = messages.getFirst();

            if (latestMessage.getTimestamp().isBefore(aiAssistantReactivationTime.atZone(ZoneId.systemDefault()).toInstant())) {
                chatModel.setAiAssistantInstruction(tenantModel.getAiAssistantStartInstruction());

                chatRepository.save(chatModel);
            }
        }
    }

    protected ChatModel getChatModel(MessageFromAdminAdapterDto messageFromAdminAdapterDto) {
        ChatModel chatModel = chatRepository.findByAdminChatId(messageFromAdminAdapterDto.getAdminChatId());

        if (chatModel != null) {
            return chatModel;
        } else {
            throw new NotFoundException("Chat not found");
        }
    }

    private List<MessageModel> getMessages(UUID chatId, int numberOfMessages) {
        List<MessageModel> messages = messageRepository.findByChatIdOrderByTimestampDesc(chatId, PageRequest.of(0, numberOfMessages));

        List<MessageModel> reversedMessages = messages.reversed();

        return reversedMessages;
    }

    private List<CompleteChatRequestRecentMessagesInner> getRecentMessages(UUID chatId, int numberOfMessages) {
        List<MessageModel> messages = getMessages(chatId, numberOfMessages);
        List<CompleteChatRequestRecentMessagesInner> chatHistory = new ArrayList<>();

        for (MessageModel message : messages) {
            CompleteChatRequestRecentMessagesInner chatItem = new CompleteChatRequestRecentMessagesInner();
            chatItem.setRole(message.getAuthor().name());
            chatItem.setContent(message.getContent());
            chatHistory.add(chatItem);
        }

        return chatHistory;
    }
}
