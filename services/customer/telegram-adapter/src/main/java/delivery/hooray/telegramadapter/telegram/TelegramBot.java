package delivery.hooray.telegramadapter.telegram;

import org.jvnet.hk2.annotations.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.UUID;

/**
 * Represents a Telegram bot that can send and receive messages.
 */
@Service
public class TelegramBot extends TelegramLongPollingBot {
    protected final UUID id;
    protected final String username;
    protected final String token;

    public TelegramBot(UUID id, String username, String token) {
        this.id = id;
        this.username = username;
        this.token = token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("Received update: " + update);
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
