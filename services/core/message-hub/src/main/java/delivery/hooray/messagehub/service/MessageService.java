package delivery.hooray.messagehub.service;


import delivery.hooray.messagehub.model.common.ChatModel;
import delivery.hooray.messagehub.model.common.MessageModel;
import delivery.hooray.messagehub.model.common.TenantModel;
import delivery.hooray.messagehub.repository.common.ChatRepository;
import delivery.hooray.messagehub.repository.common.MessageRepository;
import delivery.hooray.messagehub.repository.common.TenantRepository;
import delivery.hooray.messagehub.service.admin.MessageToAdminAdapterDto;
import delivery.hooray.messagehub.service.admin.discord.DiscordSenderService;
import delivery.hooray.messagehub.service.customer.MessageToCustomerAdapterDto;
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
    public MessageService(ChatRepository chatRepository,    // TODO: too many arguments here
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

    public void handleCustomerMessage(MessageToCustomerAdapterDto messageDto) {
        ChatModel chatModel;

        try {
            chatModel = getChatModel(messageDto);
        } catch (NotFoundException e) {  // TODO: use Optional instead of throwing exception
            TenantModel tenantModel = tenantRepository.findByAdminBot_Id(messageDto.getBotId());
            chatModel = null;
        }

        MessageModel messageModel = new MessageModel(chatModel, messageDto.getCustomerChatId(), messageDto.getMessage());

        messageRepository.save(messageModel);

        //System.out.println("Handling customer message: " + messageDto.getMessage());

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

    public void handleAdminMessage(MessageToAdminAdapterDto messageDto) {
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

    protected ChatModel getChatModel(MessageToCustomerAdapterDto messageToCustomerAdapterDto) {
        ChatModel chatModel = chatRepository.findByCustomerChatId(messageToCustomerAdapterDto.getCustomerChatId());

        if (chatModel != null) {
            return chatModel;
        } else {
            throw new RuntimeException("Chat not found");  // TODO: handle this case
        }
    }

    protected ChatModel getChatModel(MessageToAdminAdapterDto messageToAdminAdapterDto) {
        ChatModel chatModel = chatRepository.findByAdminChatId(messageToAdminAdapterDto.getAdminChatId());

        if (chatModel != null) {
            return chatModel;
        } else {
            throw new NotFoundException("Chat not found");
        }
    }
}
