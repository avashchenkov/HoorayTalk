package delivery.hooray.discordadapter.bot;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InstructionToMessageHubRequestData {
    @JsonProperty("bot_id")
    private final String botId;
    @JsonProperty("instruction")
    private final String instruction;

    public InstructionToMessageHubRequestData(String botId, String instruction) {
        this.botId = botId;
        this.instruction = instruction;
    }

    public String getBot_Id() {
        return botId;
    }

    public String getInstruction() {
        return instruction;
    }
}