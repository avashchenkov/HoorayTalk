package delivery.hooray.messagehub.service;

import delivery.hooray.messagehub.model.ChatModel;
import delivery.hooray.messagehub.model.MessageModel;
import delivery.hooray.messagehub.model.TenantModel;
import delivery.hooray.messagehub.repository.ChatRepository;
import delivery.hooray.messagehub.repository.MessageRepository;
import delivery.hooray.messagehub.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final DiscordSenderService discordSenderService;  // TODO:
    private final TelegramSenderService telegramSenderService;
    private final TenantRepository tenantRepository;

    @Autowired
    public MessageService(ChatRepository chatRepository,
                          MessageRepository messageRepository,
                          DiscordSenderService discordSenderService,
                          TelegramSenderService telegramSenderService,
                          TenantRepository tenantRepository) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.discordSenderService = discordSenderService;
        this.telegramSenderService = telegramSenderService;
        this.tenantRepository = tenantRepository;
    }

    public void handleCustomerMessage(CustomerMessageDto messageDto) {
        ChatModel chatModel = getChatModel(messageDto);
        MessageModel messageModel = new MessageModel(chatModel, messageDto.getCustomerChatId(), messageDto.getMessage());

        messageRepository.save(messageModel);

        System.out.println("Handling customer message: " + messageDto.getMessage());

        TenantModel tenantModel = chatModel.getTenant();

        MessageToDiscordAdapterRequestData message = new MessageToDiscordAdapterRequestData(messageDto.getMessage(),
                                                                                            chatModel.getAdminChatId(),
                                                                                            tenantModel.getAdminBot().getId().toString());

        discordSenderService.sendMessage(message).subscribe(
                result -> System.out.println("Operation successful: " + result),
                error -> System.err.println("Operation failed: " + error.getMessage())
        );
    }

    public void handleAdminMessage(AdminMessageDto messageDto) {
        ChatModel chatModel = getChatModel(messageDto);
        MessageModel messageModel = new MessageModel(chatModel, messageDto.getAdminChatId(), messageDto.getMessage());

        messageRepository.save(messageModel);

        System.out.println("Handling admin message: " + messageDto.getMessage());

        TenantModel tenantModel = chatModel.getTenant();

        MessageToTelegramAdapterRequestData message = new MessageToTelegramAdapterRequestData(messageDto.getMessage(),
                chatModel.getCustomerChatId(),
                tenantModel.getCustomerBot().getId().toString());

        telegramSenderService.sendMessage(message).subscribe(
                result -> System.out.println("Operation successful: " + result),
                error -> System.err.println("Operation failed: " + error.getMessage())
        );
    }

    protected ChatModel getChatModel(CustomerMessageDto customerMessageDto) {
        ChatModel chatModel = chatRepository.findByCustomerChatId(customerMessageDto.getCustomerChatId());

        if (chatModel != null) {
            return chatModel;
        } else {
            throw new RuntimeException("Chat not found");  // TODO: handle this case
        }
    }

    protected ChatModel getChatModel(AdminMessageDto adminMessageDto) {
        ChatModel chatModel = chatRepository.findByAdminChatId(adminMessageDto.getAdminChatId());

        if (chatModel != null) {
            return chatModel;
        } else {
            throw new RuntimeException("Chat not found");  // TODO: handle this case
        }
    }
}
