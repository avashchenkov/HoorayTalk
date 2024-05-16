package delivery.hooray.discordadapter.bot;

import delivery.hooray.botadapterspringbootstarter.bot.MessageToCustomerRequestData;
import net.dv8tion.jda.api.entities.channel.ChannelType;
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

import java.util.UUID;


public class DiscordBotImpl extends ListenerAdapter {
    private final DiscordBot discordBot;


    public DiscordBotImpl(DiscordBot discordBot) {
        this.discordBot = discordBot;
    }


    // TODO: Use it
    /**
     * @return
     */
    public void sendMsgToClient(MessageToCustomerRequestData data) {
        System.out.println(data.getMessage());
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();

        if (channel.getType() == ChannelType.TEXT) {
            System.out.println("Message received in server channel: " + event.getMessage().getContentDisplay());
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