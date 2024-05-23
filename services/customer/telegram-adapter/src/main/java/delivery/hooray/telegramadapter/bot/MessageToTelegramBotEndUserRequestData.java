package delivery.hooray.telegramadapter.bot;

import delivery.hooray.botadapterspringbootstarter.bot.MessageToBotEndUserRequestData;

public class MessageToTelegramBotEndUserRequestData extends MessageToBotEndUserRequestData {

    public MessageToTelegramBotEndUserRequestData(String message, String chat_id) {
        super(message, chat_id);
    }
}