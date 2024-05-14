package delivery.hooray.discordadapter.bot;

import delivery.hooray.botadapterspringbootstarter.bot.Bot;
import delivery.hooray.botadapterspringbootstarter.service.BotBehavior;
import delivery.hooray.botadapterspringbootstarter.service.EncryptionService;
import delivery.hooray.botadapterspringbootstarter.service.MessageHubSenderService;

/**
 * Represents a Discord bot that can send and receive messages.
 */
public class DiscordBot extends Bot {
    private final DiscordBotImpl discordBotImpl;

    public DiscordBot(MessageHubSenderService messageHubSenderService,
                      BotBehavior botBehavior,
                      EncryptionService encryptionService) {
        super(messageHubSenderService, botBehavior, encryptionService);

        this.discordBotImpl = new DiscordBotImpl(this);
    }

    public DiscordBotImpl getDiscordBotImpl() {
        return this.discordBotImpl;
    }
}
