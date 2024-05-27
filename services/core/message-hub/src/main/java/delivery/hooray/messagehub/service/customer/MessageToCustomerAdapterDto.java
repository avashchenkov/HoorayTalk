package delivery.hooray.messagehub.service.customer;

import java.util.UUID;


public class MessageToCustomerAdapterDto {
    private UUID botId;
    private String customerChatId;
    private String message;

    public MessageToCustomerAdapterDto(String botId, String customerChatId, String message) {
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
