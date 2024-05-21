package delivery.hooray.telegramadapter.bot;

import com.fasterxml.jackson.annotation.JsonProperty;
import delivery.hooray.botadapterspringbootstarter.bot.MessageToMessageHubRequestData;

public class TelegramMessageToMessageHubRequestData extends MessageToMessageHubRequestData {
    @JsonProperty("customer_chat_id")
    private final String customerChatId;

    public TelegramMessageToMessageHubRequestData(String botId, String message, String customerChatId) {
        super(botId, message);

        this.customerChatId = customerChatId;
    }

    public String getCustomer_chat_id() {
        return customerChatId;
    }
}
