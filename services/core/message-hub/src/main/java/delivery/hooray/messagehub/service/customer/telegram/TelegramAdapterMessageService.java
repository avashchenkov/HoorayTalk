package delivery.hooray.messagehub.service.customer.telegram;

import delivery.hooray.customeradapter.model.SendMessageRequest;
import delivery.hooray.messagehub.service.customer.CustomerAdapterMessageServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TelegramAdapterMessageService implements CustomerAdapterMessageServiceInterface {
    private final TelegramSenderService telegramSenderService;

    @Autowired
    public TelegramAdapterMessageService(TelegramSenderService telegramSenderService) {
        this.telegramSenderService = telegramSenderService;
    }

    /**
     * @param message
     */
    @Override
    public void sendMessageToCustomer(SendMessageRequest message) {
        telegramSenderService.sendMessage(message).block();
    }
}
