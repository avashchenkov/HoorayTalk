package delivery.hooray.discordadapter.bot;

import delivery.hooray.botadapterspringbootstarter.bot.Bot;
import delivery.hooray.botadapterspringbootstarter.service.BotBehavior;
import delivery.hooray.botadapterspringbootstarter.service.EncryptionService;
import delivery.hooray.botadapterspringbootstarter.service.MessageHubSenderService;
import delivery.hooray.discordadapter.service.DiscordToMessageHubSenderService;

/**
 * Represents a Discord bot that can send and receive messages.
 */
public class DiscordBot extends Bot {
    private final DiscordBotImpl discordBotImpl;
    private String guildId;
    private final DiscordToMessageHubSenderService messageHubSenderService;

    public DiscordBot(MessageHubSenderService messageHubSenderService,
                      BotBehavior botBehavior,
                      EncryptionService encryptionService) {
        super(messageHubSenderService, botBehavior, encryptionService);

        this.discordBotImpl = new DiscordBotImpl(this);
        this.messageHubSenderService = (DiscordToMessageHubSenderService) messageHubSenderService;
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

    public void sendInstructionToMessageHub(InstructionToMessageHubRequestData instruction) {
        this.messageHubSenderService.sendInstruction(instruction)
                .subscribe(response -> {
                    System.out.println("Operation successful: " + response);
                }, (error) -> {
                    System.err.println("Operation failed: " + error.getMessage());
                });
    }
}
