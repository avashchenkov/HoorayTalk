package delivery.hooray.telegramadapter.bot;

import delivery.hooray.botadapterspringbootstarter.bot.Bot;
import delivery.hooray.botadapterspringbootstarter.service.BotBehavior;
import delivery.hooray.botadapterspringbootstarter.service.EncryptionService;
import delivery.hooray.botadapterspringbootstarter.service.MessageHubSenderService;

/**
 * Represents a Telegram bot that can send and receive messages.
 */
public class TelegramBot extends Bot {
    private String username;
    private final TelegramBotImpl telegramBotImpl;

    public TelegramBot(MessageHubSenderService messageHubSenderService,
                       BotBehavior botBehavior,
                       EncryptionService encryptionService) {
        super(messageHubSenderService, botBehavior, encryptionService);

        this.telegramBotImpl = new TelegramBotImpl(this);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public TelegramBotImpl getTelegramBotImpl() {
        return this.telegramBotImpl;
    }
}
