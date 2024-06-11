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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageService {
    private final Integer aiAssistantContextWindowSize = 50;  // TODO: parametrise it later for every tenant
    private final LocalDateTime aiAssistantReactivationTime = LocalDateTime.now().minusDays(7);  // TODO: parametrise it later for every tenant
    private final Integer aiAssistantResponsesLimit = 20;  // TODO: parametrise it later for every tenant

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

        ChatModel chatModel = getChatModel(messageDto).orElseGet(() -> null);

        String adminChatId = getAdminChatId(chatModel).orElseGet(() -> null);

        SendMessageRequest sendMessageRequest = buildSendMessageRequest(messageDto, tenantModel.getAdminBot(), adminChatId);

        SendMessageResponse sendMessageResponse = this.adminAdapterMessageService.sendMessageToAdmin(sendMessageRequest);

        chatModel = this.createChatModelIfNotExists(sendMessageResponse.getAdminChatId(), messageDto.getCustomerChatId(), tenantModel, chatModel);

        MessageModel messageModel = new MessageModel(chatModel, MessageRole.CUSTOMER, messageDto.getMessage());

        messageRepository.save(messageModel);

        this.customerAdapterMessageService.handleCustomerCommand(messageDto);

        if (isAiAssistantResponseRequired(chatModel)) {
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

    private boolean isAiAssistantResponseRequired(ChatModel chatModel) {
        if (chatModel.getAiAssistantInstruction() != null) {
            return !isAiAssistantResponsesLimitExceeded(chatModel);
        } else {
            List<MessageModel> messages = getMessages(chatModel.getId(), 1);
            MessageModel latestMessage = messages.getFirst();

            if (latestMessage.getTimestamp().isBefore(aiAssistantReactivationTime.atZone(ZoneId.systemDefault()).toInstant())) {
                chatModel.setAiAssistantInstruction(chatModel.getTenant().getAiAssistantStartInstruction());
                chatRepository.save(chatModel);

                return true;
            } else {
                return false;
            }
        }
    }

    private boolean isAiAssistantResponsesLimitExceeded(ChatModel chatModel) {
        List<MessageModel> messagesLast24Hours = messageRepository.findByChatIdAndTimestampAfter(chatModel.getId(), Instant.now().minus(1, ChronoUnit.DAYS));

        long aiAssistantResponsesCount = messagesLast24Hours.stream()
                .filter(message -> message.getAuthor() == MessageRole.AI)
                .count();

        return aiAssistantResponsesCount >= aiAssistantResponsesLimit;
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
