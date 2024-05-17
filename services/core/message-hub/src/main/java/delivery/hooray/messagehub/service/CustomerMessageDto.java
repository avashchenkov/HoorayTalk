package delivery.hooray.messagehub.service;

import java.util.UUID;


public class CustomerMessageDto {
    private UUID botId;
    private String customerChatId;
    private String message;

    public CustomerMessageDto(String botId, String customerChatId, String message) {
        this.botId = UUID.fromString(botId);
        this.customerChatId = customerChatId;
        this.message = message;
    }

    public UUID getBotId() {
        return botId;
    }

    public String getCustomerChatId() {
        return customerChatId;
    }

    public String getMessage() {
        return message;
    }

    public void setBotId(UUID botId) {
        this.botId = botId;
    }

    public void setCustomerChatId(String customerChatId) {
        this.customerChatId = customerChatId;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
