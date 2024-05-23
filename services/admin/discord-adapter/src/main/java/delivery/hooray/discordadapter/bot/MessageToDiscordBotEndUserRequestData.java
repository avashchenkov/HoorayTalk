package delivery.hooray.discordadapter.bot;

import delivery.hooray.botadapterspringbootstarter.bot.MessageToBotEndUserRequestData;

public class MessageToDiscordBotEndUserRequestData extends MessageToBotEndUserRequestData {

    public MessageToDiscordBotEndUserRequestData(String message, String chat_id) {
        super(message, chat_id);
    }
}