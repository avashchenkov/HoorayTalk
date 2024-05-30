package delivery.hooray.discordadapter.bot;

import delivery.hooray.botadapterspringbootstarter.service.EncryptionService;
import delivery.hooray.sharedlib.Message;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import net.dv8tion.jda.api.utils.FileUpload;
import org.apache.http.HttpEntity;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.UUID;


public class DiscordBotImpl extends ListenerAdapter {
    private final DiscordBot discordBot;
    private JDA jda;
    EncryptionService encryptionService;


    public DiscordBotImpl(DiscordBot discordBot) {
        this.discordBot = discordBot;
        this.jda = null;
        this.encryptionService = discordBot.getEncryptionService();
    }


    // TODO: Use it
    /**
     * @return
     */
    public void sendMsgToAdmins(MessageToDiscordBotEndUserRequestData data) {
        Message message;

        ObjectMapper mapper = new ObjectMapper();
        try {
            message = mapper.readValue(data.getMessage(), Message.class);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        TextChannel channel = jda.getTextChannelById(data.getChatId());
        if (channel == null) {
            throw new IllegalArgumentException("Channel with ID " + data.getChatId() + " not found");
        }

        File tempFile = downloadImage(this.encryptionService.decrypt(message.getMediaUrl()));

        if (tempFile != null) {
            FileUpload fileUpload = FileUpload.fromData(tempFile);

            channel.sendFiles(fileUpload).queue(
                    success -> {
                        if (message.getText() != null && !message.getText().isEmpty()) {
                            channel.sendMessage(message.getText()).queue();
                        }
                        tempFile.delete();
                    },
                    failure -> {
                        tempFile.delete();
                        throw new RuntimeException("Failed to send image: " + failure.getMessage());
                    }
            );
        } else if (message.getText() != null) {
            channel.sendMessage(message.getText()).queue();
        }
    }

    private File downloadImage(String url) {
        File tempFile = null;
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = client.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        tempFile = File.createTempFile("image_", ".jpg");
                        try (InputStream in = entity.getContent(); FileOutputStream out = new FileOutputStream(tempFile)) {
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = in.read(buffer)) != -1) {
                                out.write(buffer, 0, length);
                            }
                        }
                    }
                } else {
                    System.err.println("Failed to download image: HTTP error code " + statusCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (tempFile != null) {
                tempFile.delete();  // Clean up if anything goes wrong
            }
        }
        return tempFile;
    }

    public String createChannel(String newChannelName) {
        Guild guild = jda.getGuildById(this.discordBot.getGuildId());
        if (guild != null) {
            try {
                TextChannel channel = guild.createTextChannel(newChannelName).complete();
                System.out.println("Channel created: " + channel.getName());
                return channel.getId();
            } catch (Exception e) {
                System.err.println("Failed to create channel: " + e.getMessage());
                throw new RuntimeException("Failed to create channel", e);
            }
        } else {
            throw new IllegalArgumentException("Guild not found");
        }
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
