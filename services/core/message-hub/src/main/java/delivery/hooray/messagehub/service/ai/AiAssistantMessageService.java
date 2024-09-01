package delivery.hooray.messagehub.service.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import delivery.hooray.aiassistant.model.CompleteChatRequest;
import delivery.hooray.aiassistant.model.CompleteChatRequestRecentMessagesInner;
import delivery.hooray.messagehub.enums.MessageRole;
import delivery.hooray.messagehub.model.common.ChatModel;
import delivery.hooray.messagehub.model.common.MessageModel;
import delivery.hooray.messagehub.repository.common.ChatRepository;
import delivery.hooray.messagehub.repository.common.MessageRepository;
import delivery.hooray.messagehub.service.admin.AdminAdapterMessageService;
import delivery.hooray.messagehub.service.customer.CustomerAdapterMessageService;
import delivery.hooray.sharedlib.Message;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static delivery.hooray.messagehub.enums.TelegramCommand.NEWORDER;
import static delivery.hooray.messagehub.enums.TelegramCommand.START;

@Service
public class AiAssistantMessageService {
    private final AiAssistantSenderService aiAssistantSenderService;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final CustomerAdapterMessageService customerAdapterMessageService;
    private final AdminAdapterMessageService adminAdapterMessageService;
    private final int MAX_TRIALS = 3;  // TODO: move it to the application.properties

    @Autowired
    public AiAssistantMessageService(AiAssistantSenderService aiAssistantSenderService,
                                     ChatRepository chatRepository,
                                     MessageRepository messageRepository,
                                     CustomerAdapterMessageService customerAdapterMessageService, @Qualifier("adminAdapterMessageService") AdminAdapterMessageService adminAdapterMessageService) {
        this.aiAssistantSenderService = aiAssistantSenderService;
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.customerAdapterMessageService = customerAdapterMessageService;
        this.adminAdapterMessageService = adminAdapterMessageService;
    }

    public void handleCustomerMessage(ChatModel chatModel, List<CompleteChatRequestRecentMessagesInner> recentMessages) {
        // TODO: refactor this method
        CompleteChatRequest completeChatRequest = new CompleteChatRequest();

        completeChatRequest.setAssistantId(chatModel.getTenant().getAiAssistant().getId().toString());

        List<CompleteChatRequestRecentMessagesInner> currentOrderMessages = filterCurrentOrderMessages(recentMessages);

        completeChatRequest.setRecentMessages(currentOrderMessages);

        String systemPrompt = chatModel.getAiAssistantInstruction().getText() + "\n" +
                              chatModel.getTenant().getAdminAiAssistantInstruction().getText();

        completeChatRequest.setSystemMessage(systemPrompt);

        AiAssistantResponse aiAssistantResponse = completeChat(completeChatRequest).orElseGet(() -> {
            AiAssistantResponse response = new AiAssistantResponse();
            response.setMessage("Sorry, AI Assistant is not available at the moment. Our Administrator will get back to you soon.");
            response.setAdminActionRequired(true);
            return response;
        });

        if (aiAssistantResponse.isAdminActionRequired()) {
            chatModel.setAiAssistantInstruction(null);
            chatRepository.save(chatModel);
        }

        ObjectMapper objectMapper = new ObjectMapper();

        MessageModel aiAssistantMessage = null;
        try {
            aiAssistantMessage = new MessageModel(chatModel, MessageRole.AI, objectMapper.writeValueAsString(aiAssistantResponse));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        messageRepository.save(aiAssistantMessage);

        delivery.hooray.customeradapter.model.SendMessageRequest messageToTelegram =
                new delivery.hooray.customeradapter.model.SendMessageRequest(chatModel.getTenant().getCustomerBot().getId().toString(),
                                                                             chatModel.getCustomerChatId(),
                                                                             aiAssistantResponse.getMessage());

        customerAdapterMessageService.sendMessageToCustomer(messageToTelegram);

        Message messageToAdminAdapter = new Message();

        messageToAdminAdapter.setText(aiAssistantResponse.getMessage());

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(messageToAdminAdapter);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        delivery.hooray.adminadapter.model.SendMessageRequest messageToAdmins =
                new delivery.hooray.adminadapter.model.SendMessageRequest(chatModel.getTenant().getAdminBot().getId().toString(),
                        jsonString);

        messageToAdmins.setAdminChatId(chatModel.getAdminChatId());
        messageToAdmins.setCustomerDisplayName("AI");

        adminAdapterMessageService.sendMessageToAdmin(messageToAdmins);
    }

    private Optional<AiAssistantResponse> completeChat(CompleteChatRequest completeChatRequest) {
        int trials = 0;
        String aiAssistantResponse = "";
        ObjectMapper objectMapper = new ObjectMapper();
        AiAssistantResponse response;

        do {
            try {
                aiAssistantResponse = this.aiAssistantSenderService.sendMessage(completeChatRequest).block();
                response = objectMapper.readValue(aiAssistantResponse, AiAssistantResponse.class);

                return Optional.of(response);
            } catch (IOException e) {
                e.printStackTrace();

                this.addWrongFormatCorrectionPrompts(completeChatRequest.getRecentMessages(), aiAssistantResponse);
            }
        } while (trials++ < MAX_TRIALS);

        return Optional.empty();
    }

    private void addWrongFormatCorrectionPrompts(List<@Valid CompleteChatRequestRecentMessagesInner> recentMessages,
                                                                                                String aiAssistantWrongFormatMessageContent) {
        CompleteChatRequestRecentMessagesInner aiAssistantWrongFormatMessage = new CompleteChatRequestRecentMessagesInner();

        aiAssistantWrongFormatMessage.setRole(MessageRole.AI.name());
        aiAssistantWrongFormatMessage.setContent(aiAssistantWrongFormatMessageContent);

        recentMessages.add(aiAssistantWrongFormatMessage);

        CompleteChatRequestRecentMessagesInner systemWrongFormatCorrectingMessage = new CompleteChatRequestRecentMessagesInner();

        systemWrongFormatCorrectingMessage.setRole(MessageRole.SYSTEM.name());
        systemWrongFormatCorrectingMessage.setContent("Attention! Your last message was not in the correct format. You have to answer according to the JSON pattern: {\"message\":\"The text that will be shown to the customer.\", \"isAdminActionRequired\": false or true");

        recentMessages.add(systemWrongFormatCorrectingMessage);
    }

    private List<CompleteChatRequestRecentMessagesInner> filterCurrentOrderMessages(List<CompleteChatRequestRecentMessagesInner> allMessages) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<CompleteChatRequestRecentMessagesInner> lastOrderMessages = new ArrayList<>();

        for (int i = allMessages.size() - 1; i >= 0; i--) {
            CompleteChatRequestRecentMessagesInner message = allMessages.get(i);
            if (message.getRole().equals(MessageRole.CUSTOMER.name())) {
                try {
                    String text = objectMapper.readValue(message.getContent(), Message.class).getText();

                    lastOrderMessages.add(message);

                    if (START.getCommand().equals(text) || NEWORDER.getCommand().equals(text)) {
                        break;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                lastOrderMessages.add(message);
            }
        }

        Collections.reverse(lastOrderMessages);

        return lastOrderMessages;
    }
}
