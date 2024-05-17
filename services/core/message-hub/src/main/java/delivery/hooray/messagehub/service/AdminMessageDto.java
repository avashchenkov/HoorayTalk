package delivery.hooray.messagehub.service;

import java.util.UUID;

public class AdminMessageDto {
    protected UUID botId;
    protected String adminChatId;
    protected String message;

    public AdminMessageDto(String botId, String adminChatId, String message) {
        this.botId = UUID.fromString(botId);
        this.adminChatId = adminChatId;
        this.message = message;
    }

    public UUID getBotId() {
        return botId;
    }

    public String getAdminChatId() {
        return adminChatId;
    }

    public String getMessage() {
        return message;
    }

    public void setBotId(UUID botId) {
        this.botId = botId;
    }

    public void setAdminChatId(String adminChatId) {
        this.adminChatId = adminChatId;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
