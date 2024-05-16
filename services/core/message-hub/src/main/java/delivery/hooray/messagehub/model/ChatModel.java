package delivery.hooray.messagehub.model;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "chat", indexes = {
        @Index(name = "idx_tenant_id", columnList = "tenant_id")
})
public class ChatModel {

    protected ChatModel() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected UUID id;

    @ManyToOne
    @Column(name = "tenant_id")
    protected TenantModel tenant;

    @ManyToOne
    @Column(name = "ai_assistant_instruction_id")
    protected AiAssistantInstructionModel aiAssistantInstruction;

    public UUID getId() {
        return id;
    }

    public AiAssistantInstructionModel getAiAssistantInstruction() {
        return aiAssistantInstruction;
    }

    public void setAiAssistantInstruction(AiAssistantInstructionModel aiAssistantInstruction) {
        this.aiAssistantInstruction = aiAssistantInstruction;
    }
}
