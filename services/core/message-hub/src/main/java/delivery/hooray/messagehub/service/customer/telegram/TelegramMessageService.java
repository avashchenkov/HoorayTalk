package delivery.hooray.messagehub.service.customer.telegram;

import delivery.hooray.messagehub.service.customer.MessageFromCustomerAdapterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TelegramMessageService {
    private final TelegramSenderService telegramSenderService;
    private final TelegramCommandService telegramCommandService;

    @Autowired
    public TelegramMessageService(TelegramSenderService telegramSenderService,
                                  TelegramCommandService telegramCommandService) {
        this.telegramSenderService = telegramSenderService;
        this.telegramCommandService = telegramCommandService;
    }

    public void handleCustomerMessage(MessageFromCustomerAdapterDto messageDto) {
        telegramCommandService.handleBotCommand(messageDto.getMessage());
    }

}
