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
    private String guildId;

    public DiscordBot(MessageHubSenderService messageHubSenderService,
                      BotBehavior botBehavior,
                      EncryptionService encryptionService) {
        super(messageHubSenderService, botBehavior, encryptionService);

        this.discordBotImpl = new DiscordBotImpl(this);
    }

    @Override
    public void run() {
        this.getDiscordBotImpl().run();
    }

    public DiscordBotImpl getDiscordBotImpl() {
        return this.discordBotImpl;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    public String getGuildId() {
        return this.guildId;
    }

    public EncryptionService getEncryptionService() {
        return this.encryptionService;
    }

    public DiscordBotBehavior getBotBehavior() {
        return (DiscordBotBehavior) this.botBehavior;
    }
}