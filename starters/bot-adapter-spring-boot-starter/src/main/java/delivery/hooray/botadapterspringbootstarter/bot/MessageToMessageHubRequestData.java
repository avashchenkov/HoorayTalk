package delivery.hooray.botadapterspringbootstarter.bot;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class MessageToMessageHubRequestData {
    @JsonProperty("bot_id")
    private final String botId;
    @JsonProperty("message")
    private final String message;

    public MessageToMessageHubRequestData(String botId, String message) {
        this.botId = botId;
        this.message = message;
    }

    public String getBot_Id() {
        return botId;
    }

    public String getMessage() {
        return message;
    }
}