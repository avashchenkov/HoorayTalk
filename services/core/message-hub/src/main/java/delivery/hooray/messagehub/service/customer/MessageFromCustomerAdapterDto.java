package delivery.hooray.messagehub.service.customer;

import java.util.UUID;


public class MessageFromCustomerAdapterDto {
    private UUID botId;
    private String customerChatId;
    private String message;
    private String customerDisplayName;

    public MessageFromCustomerAdapterDto(String botId, String customerChatId, String message, String customerDisplayName) {
        this.botId = UUID.fromString(botId);
        this.customerChatId = customerChatId;
        this.message = message;
        this.customerDisplayName = customerDisplayName;
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

    public String getCustomerDisplayName() {
        return customerDisplayName;
    }

    public void setCustomerDisplayName(String customerDisplayName) {
        this.customerDisplayName = customerDisplayName;
    }
}
