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

    public ChatModel(String customerChatId, String adminChatId) {
        this.customerChatId = customerChatId;
        this.adminChatId = adminChatId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected UUID id;

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    protected TenantModel tenant;

    @ManyToOne
    @JoinColumn(name = "ai_assistant_instruction_id")
    protected AiAssistantInstructionModel aiAssistantInstruction;

    @Column
    protected String customerChatId;  // TODO: Add Index

    @Column
    protected String adminChatId;  // TODO: Add Index

    public UUID getId() {
        return id;
    }

    public String getCustomerChatId() {
        return customerChatId;
    }

    public String getAdminChatId() {
        return adminChatId;
    }

    public AiAssistantInstructionModel getAiAssistantInstruction() {
        return aiAssistantInstruction;
    }

    public void setAiAssistantInstruction(AiAssistantInstructionModel aiAssistantInstruction) {
        this.aiAssistantInstruction = aiAssistantInstruction;
    }
}
