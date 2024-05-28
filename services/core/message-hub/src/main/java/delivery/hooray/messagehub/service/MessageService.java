package delivery.hooray.messagehub.service;


import delivery.hooray.messagehub.model.common.ChatModel;
import delivery.hooray.messagehub.model.common.MessageModel;
import delivery.hooray.messagehub.model.common.TenantModel;
import delivery.hooray.messagehub.repository.common.ChatRepository;
import delivery.hooray.messagehub.repository.common.MessageRepository;
import delivery.hooray.messagehub.repository.common.TenantRepository;
import delivery.hooray.messagehub.service.admin.MessageFromAdminAdapterDto;
import delivery.hooray.messagehub.service.admin.discord.DiscordSenderService;
import delivery.hooray.messagehub.service.customer.MessageFromCustomerAdapterDto;
import delivery.hooray.messagehub.service.customer.telegram.TelegramSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
public class MessageService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final DiscordSenderService discordSenderService;  // TODO:
    private final TelegramSenderService telegramSenderService;
    private final TenantRepository tenantRepository;

    @Autowired
    public MessageService(ChatRepository chatRepository,    // TODO: too many arguments
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

    public void handleCustomerMessage(MessageFromCustomerAdapterDto messageDto) {
        ChatModel chatModel;

        try {
            chatModel = getChatModel(messageDto);
        } catch (NotFoundException e) {  // TODO: use Optional instead of throwing exception
            TenantModel tenantModel = tenantRepository.findByAdminBot_Id(messageDto.getBotId());
            chatModel = null;
        }

        MessageModel messageModel = new MessageModel(chatModel, messageDto.getCustomerChatId(), messageDto.getMessage());

        messageRepository.save(messageModel);

        TenantModel tenantModel = chatModel.getTenant();

        delivery.hooray.discordadapter.model.SendMessageRequest sendMessageRequest =
                new delivery.hooray.discordadapter.model.SendMessageRequest(tenantModel.getAdminBot().getId().toString(),
                                                                       messageDto.getMessage());
        sendMessageRequest.setAdminChatId(chatModel.getAdminChatId());

        discordSenderService.sendMessage(sendMessageRequest).subscribe(
                sendMessageResponse -> {
                    System.out.println("Operation successful: " + sendMessageResponse);

                    String adminChatId = sendMessageResponse.getAdminChatId();
                    System.out.println("Admin Chat ID: " + adminChatId);
                },
                error -> {
                    System.err.println("Operation failed: " + error.getMessage());
                }
        );
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
            throw new RuntimeException("Chat not found");  // TODO: handle this case
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
}
