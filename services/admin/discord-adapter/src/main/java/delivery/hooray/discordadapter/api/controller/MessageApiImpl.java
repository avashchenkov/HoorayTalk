package delivery.hooray.discordadapter.api.controller;

import delivery.hooray.discordadapter.bot.MessageToDiscordBotEndUserRequestData;
import delivery.hooray.botadapterspringbootstarter.service.BotService;
import delivery.hooray.discordadapter.api.MessageApi;
import delivery.hooray.discordadapter.bot.DiscordBot;
import delivery.hooray.discordadapter.model.SendMessageRequest;
import delivery.hooray.discordadapter.model.SendMessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class MessageApiImpl implements MessageApi {
    private final BotService botService;

    @Autowired
    public MessageApiImpl(BotService botService) {
        this.botService = botService;
    }

    @Override
    public ResponseEntity<SendMessageResponse> sendMessage(SendMessageRequest sendMessageRequest) {
        DiscordBot bot = (DiscordBot) botService.getBot(UUID.fromString(sendMessageRequest.getBotId()));
        MessageToDiscordBotEndUserRequestData msg = new MessageToDiscordBotEndUserRequestData(sendMessageRequest.getMessage(),
                                                                                              sendMessageRequest.getAdminChatId());

        String channelId = sendMessageRequest.getAdminChatId();
        SendMessageResponse response;

        if (channelId == null) {
            channelId = String.valueOf(bot.getDiscordBotImpl().createChannel("New Channel"));
            msg.setChatId(channelId);
        }

        bot.sendMsgToBotEndUser(msg);
        response = new SendMessageResponse();
        response.setAdminChatId(channelId);

        return ResponseEntity.ok(response);
    }
}