package delivery.hooray.botadapterspringbootstarter.bot;

public abstract class MessageToCustomerRequestData {
    private final String message;
    private final String chat_id;

    public MessageToCustomerRequestData(String message, String chat_id) {
        this.message = message;
        this.chat_id = chat_id;
    }

    public String getMessage() {
        return message;
    }

    public String getChat_id() {
        return chat_id;
    }
}