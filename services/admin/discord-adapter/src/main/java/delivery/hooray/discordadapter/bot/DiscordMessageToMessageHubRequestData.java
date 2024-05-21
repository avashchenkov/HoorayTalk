package delivery.hooray.discordadapter.bot;

import com.fasterxml.jackson.annotation.JsonProperty;
import delivery.hooray.botadapterspringbootstarter.bot.MessageToMessageHubRequestData;

public class DiscordMessageToMessageHubRequestData extends MessageToMessageHubRequestData {
    @JsonProperty("admin_chat_id")
    private final String adminChatId;

    public DiscordMessageToMessageHubRequestData(String botId, String message, String adminChatId) {
        super(botId, message);

        this.adminChatId = adminChatId;
    }

    public String getAdmin_chat_id() {
        return adminChatId;
    }
}
