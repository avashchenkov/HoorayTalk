package delivery.hooray.telegramadapter.config;

import delivery.hooray.telegramadapter.service.MessageHubSenderService;
import delivery.hooray.telegramadapter.telegram.TelegramBot;
import delivery.hooray.telegramadapter.telegram.TelegramUpdateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TelegramBotFactory {
    protected MessageHubSenderService messageHubSenderService;
    protected TelegramUpdateProcessor updateProcessor;

    @Autowired
    public TelegramBotFactory(MessageHubSenderService messageHubSenderService,
                              TelegramUpdateProcessor updateProcessor) {
        this.messageHubSenderService = messageHubSenderService;
        this.updateProcessor = updateProcessor;
    }

    public TelegramBot createBot(UUID id, String username, String token) {
        return new TelegramBot(id,
                               username,
                               token,
                               updateProcessor,
                               messageHubSenderService);
    }
}
