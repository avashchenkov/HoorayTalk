package delivery.hooray.messagehub.model.ai;

import jakarta.persistence.*;

import java.util.UUID;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "ai_assistant")
public class AiAssistantModel {

    protected AiAssistantModel() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected UUID id;

    public UUID getId() {
        return id;
    }
}
