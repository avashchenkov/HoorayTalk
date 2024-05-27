package delivery.hooray.messagehub.model.ai;

import delivery.hooray.messagehub.model.common.AiAssistantModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "openai_ai_assistant")
public class OpenAiAssistantModel extends AiAssistantModel {

    protected OpenAiAssistantModel() {
    }
}
