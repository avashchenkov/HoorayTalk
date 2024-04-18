package delivery.hooray.telegramadapter.config;

import delivery.hooray.telegramadapter.telegram.TelegramBot;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TelegramBotFactory {

    public TelegramBot createBot(UUID id, String username, String token) {
        return new TelegramBot(id, username, token);
    }
}
