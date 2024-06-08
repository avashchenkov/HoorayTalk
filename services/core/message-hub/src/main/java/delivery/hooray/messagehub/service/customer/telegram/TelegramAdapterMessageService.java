package delivery.hooray.messagehub.service.customer.telegram;

import delivery.hooray.customeradapter.model.SendMessageRequest;
import delivery.hooray.messagehub.service.customer.CustomerAdapterMessageServiceInterface;
import delivery.hooray.messagehub.service.customer.MessageFromCustomerAdapterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TelegramAdapterMessageService implements CustomerAdapterMessageServiceInterface {
    private final TelegramSenderService telegramSenderService;
    private final TelegramCommandService telegramCommandService;

    @Autowired
    public TelegramAdapterMessageService(TelegramSenderService telegramSenderService,
                                         TelegramCommandService telegramCommandService) {
        this.telegramSenderService = telegramSenderService;
        this.telegramCommandService = telegramCommandService;
    }

    /**
     * @param message
     */
    @Override
    public void sendMessageToCustomer(SendMessageRequest message) {
        telegramSenderService.sendMessage(message).block();
    }

    /**
     * @param messageDto
     */
    public void handleCustomerCommand(MessageFromCustomerAdapterDto messageDto) {
        telegramCommandService.handleBotCommand(messageDto);
    }
}
