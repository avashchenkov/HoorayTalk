package delivery.hooray.messagehub.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "openai_ai_assistant")
public class OpenAiAiAssistantModel extends AiAssistantModel {

    protected OpenAiAiAssistantModel() {
    }

    @Column(name= "openai_assistant_account_id", nullable = false)
    private UUID openaiAssistantAccountId;

    public UUID getOpenaiAssistantAccountId() {
        return openaiAssistantAccountId;
    }
}
