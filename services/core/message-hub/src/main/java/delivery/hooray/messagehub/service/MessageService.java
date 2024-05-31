package delivery.hooray.messagehub.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import delivery.hooray.messagehub.service.ai.AiAssistantResponse;
import delivery.hooray.sharedlib.Message;
import delivery.hooray.aiassistant.model.CompleteChatRequest;
import delivery.hooray.messagehub.model.common.AdminBotModel;
import delivery.hooray.messagehub.model.common.ChatModel;
import delivery.hooray.messagehub.model.common.MessageModel;
import delivery.hooray.messagehub.model.common.TenantModel;
import delivery.hooray.messagehub.repository.common.ChatRepository;
import delivery.hooray.messagehub.repository.common.MessageRepository;
import delivery.hooray.messagehub.repository.common.TenantRepository;
import delivery.hooray.messagehub.service.admin.MessageFromAdminAdapterDto;
import delivery.hooray.messagehub.service.admin.discord.DiscordSenderService;
import delivery.hooray.messagehub.service.ai.AiAssistantSenderService;
import delivery.hooray.messagehub.service.customer.MessageFromCustomerAdapterDto;
import delivery.hooray.messagehub.service.customer.telegram.TelegramSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final DiscordSenderService discordSenderService;  // TODO:
    private final TelegramSenderService telegramSenderService;
    private final TenantRepository tenantRepository;
    private final AiAssistantSenderService aiAssistantSenderService;

    @Autowired
    public MessageService(ChatRepository chatRepository,    // TODO: too many arguments
                          MessageRepository messageRepository,
                          DiscordSenderService discordSenderService,
                          TelegramSenderService telegramSenderService,
                          TenantRepository tenantRepository,
                          AiAssistantSenderService aiAssistantSenderService) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.discordSenderService = discordSenderService;
        this.telegramSenderService = telegramSenderService;
        this.tenantRepository = tenantRepository;
        this.aiAssistantSenderService = aiAssistantSenderService;
    }

    public void handleCustomerMessage(MessageFromCustomerAdapterDto messageDto) {
        ChatModel chatModel = null;
        String adminChatId = null;
        TenantModel tenantModel;

        try {
            chatModel = getChatModel(messageDto);
            adminChatId = chatModel.getAdminChatId();
        } catch (NotFoundException e) {  // TODO: use Optional instead of throwing exception
        }

        try {
            tenantModel = tenantRepository.findByCustomerBot_Id(messageDto.getBotId());
        } catch (NotFoundException ex) {
            return;  //TODO: unknown bot case/incorrect setup
        }

        AdminBotModel adminBotModel = tenantModel.getAdminBot();

        delivery.hooray.discordadapter.model.SendMessageRequest sendMessageRequest =
                new delivery.hooray.discordadapter.model.SendMessageRequest(adminBotModel.getId().toString(),
                        messageDto.getMessage());

        sendMessageRequest.setAdminChatId(adminChatId);
        sendMessageRequest.setCustomerDisplayName(messageDto.getCustomerDisplayName());

        delivery.hooray.discordadapter.model.SendMessageResponse sendMessageResponse =
                discordSenderService.sendMessage(sendMessageRequest).block();

        if (adminChatId == null) {
            adminChatId = sendMessageResponse.getAdminChatId();

            chatModel = new ChatModel(messageDto.getCustomerChatId(), adminChatId, tenantModel);

            chatModel.setAiAssistantInstruction(tenantModel.getAiAssistantStartInstruction());

            chatRepository.save(chatModel);
        }

        MessageModel messageModel = new MessageModel(chatModel, messageDto.getCustomerChatId(), messageDto.getMessage());

        messageRepository.save(messageModel);

        if(chatModel.getAiAssistantInstruction() != null) {
            CompleteChatRequest completeChatRequest = new CompleteChatRequest();

            completeChatRequest.setAssistantId(chatModel.getTenant().getAiAssistant().getId().toString());

            String userPrompt = getChatHistory(chatModel.getId(), 50);

            completeChatRequest.setUserMessage(userPrompt);

            String systemPrompt = chatModel.getAiAssistantInstruction().getText();

            completeChatRequest.setSystemMessage(systemPrompt);

            String aiAssistantResponse = aiAssistantSenderService.sendMessage(completeChatRequest).block();

            ObjectMapper objectMapper = new ObjectMapper();
            AiAssistantResponse response;
            try {
                response = objectMapper.readValue(aiAssistantResponse, AiAssistantResponse.class);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            if (response.isAdminActionRequired()) {
                chatModel.setAiAssistantInstruction(null);
                chatRepository.save(chatModel);
            }

            MessageModel aiAssistantMessage = new MessageModel(chatModel, "AI", aiAssistantResponse);

            messageRepository.save(aiAssistantMessage);

            delivery.hooray.telegramadapter.model.SendMessageRequest messageToTelegram =
                    new delivery.hooray.telegramadapter.model.SendMessageRequest(tenantModel.getCustomerBot().getId().toString(),
                            chatModel.getCustomerChatId(),
                            response.getMessage());

            telegramSenderService.sendMessage(messageToTelegram).subscribe(
                    result -> System.out.println("Operation successful: " + result),
                    error -> System.err.println("Operation failed: " + error.getMessage())
            );

            Message discordMessage = new Message();

            discordMessage.setText(response.getMessage());

            ObjectMapper mapper = new ObjectMapper();
            String jsonString = null;
            try {
                jsonString = mapper.writeValueAsString(discordMessage);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            delivery.hooray.discordadapter.model.SendMessageRequest messageToDiscord =
                    new delivery.hooray.discordadapter.model.SendMessageRequest(adminBotModel.getId().toString(),
                            jsonString);

            messageToDiscord.setAdminChatId(adminChatId);
            messageToDiscord.setCustomerDisplayName("AI");

            discordSenderService.sendMessage(messageToDiscord).block();
        }
    }

    public void handleAdminMessage(MessageFromAdminAdapterDto messageDto) {
        ChatModel chatModel;

        try {
            chatModel = getChatModel(messageDto);
        } catch (NotFoundException e) {
            return;
        }

        MessageModel messageModel = new MessageModel(chatModel, messageDto.getAdminChatId(), messageDto.getMessage());

        messageRepository.save(messageModel);

        System.out.println("Handling admin message: " + messageDto.getMessage());

        TenantModel tenantModel = chatModel.getTenant();

        delivery.hooray.telegramadapter.model.SendMessageRequest message =
                new delivery.hooray.telegramadapter.model.SendMessageRequest(tenantModel.getCustomerBot().getId().toString(),
                        chatModel.getCustomerChatId(),
                        messageDto.getMessage());

        telegramSenderService.sendMessage(message).subscribe(
                result -> System.out.println("Operation successful: " + result),
                error -> System.err.println("Operation failed: " + error.getMessage())
        );
    }

    protected ChatModel getChatModel(MessageFromCustomerAdapterDto messageFromCustomerAdapterDto) {
        ChatModel chatModel = chatRepository.findByCustomerChatId(messageFromCustomerAdapterDto.getCustomerChatId());

        if (chatModel != null) {
            return chatModel;
        } else {
            throw new NotFoundException("Chat not found");  // TODO: handle this case
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
        return messageRepository.findByChatIdOrderByTimestampDesc(chatId, PageRequest.of(0, numberOfMessages));
    }

    private String getChatHistory(UUID chatId, int numberOfMessages) {
        List<MessageModel> messages = getMessages(chatId, numberOfMessages);
        return messages.stream()
                .sorted((m1, m2) -> m2.getTimestamp().compareTo(m1.getTimestamp()))
                .map(message -> String.format("Timestamp: %s\nContent: %s\n---",
                        message.getTimestamp(), message.getContent()))
                .collect(Collectors.joining("\n"));
    }
}
