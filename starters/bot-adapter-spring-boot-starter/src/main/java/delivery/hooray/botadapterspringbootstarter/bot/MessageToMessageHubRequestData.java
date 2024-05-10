package delivery.hooray.botadapterspringbootstarter.bot;

import java.util.UUID;

public abstract class MessageToMessageHubRequestData {
    private final UUID bot_id;
    private final String message;
    private final String customer_chat_id;

    public MessageToMessageHubRequestData(UUID bot_id, String message, String customer_chat_id) {
        this.bot_id = bot_id;
        this.message = message;
        this.customer_chat_id = customer_chat_id;
    }

    public UUID getBot_Id() {
        return bot_id;
    }

    public String getMessage() {
        return message;
    }

    public String getCustomer_chat_id() {
        return customer_chat_id;
    }
}