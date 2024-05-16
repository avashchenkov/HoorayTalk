package delivery.hooray.messagehub.model;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "ai_assistant_instruction")
public class AiAssistantInstructionModel {

    protected AiAssistantInstructionModel() {}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "instruction_text", nullable = false)
    private String text;

    public UUID getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
