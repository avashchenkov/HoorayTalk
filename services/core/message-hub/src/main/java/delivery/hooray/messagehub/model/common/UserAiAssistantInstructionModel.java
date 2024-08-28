package delivery.hooray.messagehub.model.common;

import jakarta.persistence.*;

import java.util.UUID;


@Entity
@Table(name = "user_ai_assistant_instruction")
public class UserAiAssistantInstructionModel {

    protected UserAiAssistantInstructionModel() {}

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

    public void setText(String text) {
        this.text = text;
    }
}
