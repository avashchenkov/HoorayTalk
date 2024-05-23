package delivery.hooray.messagehub.service;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageToDiscordAdapterRequestData {
    @JsonProperty("message")
    private final String message;
    @JsonProperty("admin_chat_id")
    private final String adminChatId;
    @JsonProperty("bot_id")
    private final String botId;

    public MessageToDiscordAdapterRequestData(String message, String adminChatId, String botId) {
        this.message = message;
        this.adminChatId = adminChatId;
        this.botId = botId;
    }

    public String getMessage() {
        return message;
    }

    public String getAdminChatId() {
        return adminChatId;
    }

    public String getBotId() {
        return botId;
    }
}