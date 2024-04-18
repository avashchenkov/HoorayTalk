package delivery.hooray.telegramadapter.telegram;

import delivery.hooray.telegramadapter.service.MessageHubSenderService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.UUID;

/**
 * Represents a Telegram bot that can send and receive messages.
 */
public class TelegramBot extends TelegramLongPollingBot {
    protected final UUID id;
    protected final String username;
    protected final String token;

    protected final TelegramUpdateProcessor updateProcessor;
    protected final MessageHubSenderService messageHubSenderService;

    public TelegramBot(UUID id,
                       String username,
                       String token,
                       TelegramUpdateProcessor updateProcessor,
                       MessageHubSenderService messageHubSenderService) {
        this.id = id;
        this.username = username;
        this.token = token;
        this.updateProcessor = updateProcessor;
        this.messageHubSenderService = messageHubSenderService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        String processedMsg = this.updateProcessor.processUpdate(update);

        this.messageHubSenderService.sendMessage(processedMsg)
                .subscribe();
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    public UUID getBotId() {
        return id;
    }
}
