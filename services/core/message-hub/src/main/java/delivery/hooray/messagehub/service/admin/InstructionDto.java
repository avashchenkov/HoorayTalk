package delivery.hooray.messagehub.service.admin;

public class InstructionDto {
    private String instruction;
    private String botId;

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getBotId() {
        return botId;
    }

    public void setBotId(String botId) {
        this.botId = botId;
    }
}
