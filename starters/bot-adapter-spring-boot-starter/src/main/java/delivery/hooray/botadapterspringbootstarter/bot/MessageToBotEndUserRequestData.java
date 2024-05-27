package delivery.hooray.botadapterspringbootstarter.bot;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class MessageToBotEndUserRequestData {
    @JsonProperty("message")
    private String message;
    @JsonProperty("chat_id")
    private String chatId;

    public MessageToBotEndUserRequestData(String message, String chatId) {
        this.message = message;
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chat_id) {
        this.chatId = chat_id;
    }
}