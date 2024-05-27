package delivery.hooray.messagehub.service.customer.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageToTelegramEnd {  //TODO: use generated class instead
    @JsonProperty("message")
    private final String message;
    @JsonProperty("customer_chat_id")
    private final String chatId;
    @JsonProperty("bot_id")
    private final String botId;

    public MessageToTelegramEnd(String message, String chatId, String botId) {
        this.message = message;
        this.chatId = chatId;
        this.botId = botId;
    }

    public String getMessage() {
        return message;
    }

    public String getChatId() {
        return chatId;
    }

    public String getBotId() {
        return botId;
    }
}