package delivery.hooray.botadapterspringbootstarter.bot;

public abstract class MessageToBotEndUserRequestData {
    private final String message;
    private final String chat_id;

    public MessageToBotEndUserRequestData(String message, String chat_id) {
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