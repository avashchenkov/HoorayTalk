package delivery.hooray.discordadapter.bot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEmojiEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;


import java.util.UUID;
import java.util.concurrent.CompletableFuture;


public class DiscordBotImpl extends ListenerAdapter {
    private final DiscordBot discordBot;
    private JDA jda;


    public DiscordBotImpl(DiscordBot discordBot) {
        this.discordBot = discordBot;
        this.jda = null;
    }


    // TODO: Use it
    /**
     * @return
     */
    public void sendMsgToClient(MessageToDiscordBotEndUserRequestData data) {
        TextChannel channel = jda.getTextChannelById(data.getChatId());
        if (channel != null) {
            channel.sendMessage(data.getMessage()).queue();
        } else {
            throw new IllegalArgumentException("Channel with ID " + data.getChatId() + " not found");
        }
    }

    public CompletableFuture<String> createChannel(String newChannelName) {
        CompletableFuture<String> future = new CompletableFuture<>();

        Guild guild = jda.getGuildById(this.discordBot.getGuildId());
        if (guild != null) {
            ChannelAction<TextChannel> action = guild.createTextChannel(newChannelName);
            action.queue(
                    channel -> {
                        System.out.println("Channel created: " + channel.getName());
                        future.complete(channel.getId()); // Complete the future with the channel ID
                    },
                    throwable -> {
                        System.err.println("Failed to create channel: " + throwable.getMessage());
                        future.completeExceptionally(throwable); // Complete the future exceptionally
                    }
            );
        } else {
            future.completeExceptionally(new IllegalArgumentException("Guild not found"));
        }

        return future;
    }

    public void run() {
        JDABuilder builder = JDABuilder.createDefault(discordBot.getBotToken());

        builder.enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT);

        builder.addEventListeners(discordBot.getDiscordBotImpl());
        this.jda = builder.build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().getId().equals(jda.getSelfUser().getId())) {
            return;
        }

        MessageChannel channel = event.getChannel();

        if (channel.getType() == ChannelType.TEXT) {
            System.out.println("Message received in server channel: " + event.getMessage().getContentDisplay());

            DiscordMessageToMessageHubRequestData data = new DiscordMessageToMessageHubRequestData(
                    this.getBotId().toString(),
                    event.getMessage().getContentDisplay(),
                    event.getChannel().getId()
            );

            this.getDiscordBot().sendMsgToMessageHub(data);
        }
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent event) {
        MessageChannel channel = event.getChannel();

        if (channel.getType() == ChannelType.TEXT) {
            String response = "Sorry, the Message Editing feature is not implemented yet. No effect was taken on the customer side.";
            channel.sendMessage(response).queue();
        }
    }

    @Override
    public void onMessageDelete(MessageDeleteEvent event) {
        MessageChannel channel = event.getChannel();

        if (channel.getType() == ChannelType.TEXT) {
            String response = "Sorry, the Message Deletion feature is not implemented yet. No effect was taken on the customer side.";
            channel.sendMessage(response).queue();
        }
    }

    @Override
    public void onMessageBulkDelete(MessageBulkDeleteEvent event) {
        MessageChannel channel = event.getChannel();

        if (channel.getType() == ChannelType.TEXT) {
            String response = "Sorry, the Message Bulk Deletion feature is not implemented yet. No effect was taken on the customer side.";
            channel.sendMessage(response).queue();
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        MessageChannel channel = event.getChannel();

        if (channel.getType() == ChannelType.TEXT) {
            String response = "Sorry, the Message Reaction feature is not implemented yet. No effect was taken on the customer side.";
            channel.sendMessage(response).queue();
        }
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        MessageChannel channel = event.getChannel();

        if (channel.getType() == ChannelType.TEXT) {
            String response = "Sorry, the Message Reaction Removal feature is not implemented yet. No effect was taken on the customer side.";
            channel.sendMessage(response).queue();
        }
    }

    @Override
    public void onMessageReactionRemoveAll(MessageReactionRemoveAllEvent event) {
        MessageChannel channel = event.getChannel();

        if (channel.getType() == ChannelType.TEXT) {
            String response = "Sorry, the Message Reaction Removal feature is not implemented yet. No effect was taken on the customer side.";
            channel.sendMessage(response).queue();
        }
    }

    @Override
    public void onMessageReactionRemoveEmoji(MessageReactionRemoveEmojiEvent event) {
        MessageChannel channel = event.getChannel();

        if (channel.getType() == ChannelType.TEXT) {
            String response = "Sorry, the Message Reaction Removal feature is not implemented yet. No effect was taken on the customer side.";
            channel.sendMessage(response).queue();
        }
    }

    public UUID getBotId() {  // TODO: move it to the parent interface if the ID is needed for the class (?)
        return this.getDiscordBot().getBotId();
    }


    private DiscordBot getDiscordBot() {  // TODO:
        return this.discordBot;
    }
}
