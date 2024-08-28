package delivery.hooray.messagehub.model.common;

import delivery.hooray.messagehub.model.ai.AiAssistantModel;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tenant")
public class TenantModel {

    protected TenantModel() {}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected UUID id;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private BusinessModel business;

    @ManyToOne
    @JoinColumn(name = "ai_assistant_start_instruction")
    private AiAssistantInstructionModel aiAssistantStartInstruction;

    @ManyToOne
    @JoinColumn(name = "customer_bot_id", nullable = false)
    private CustomerBotModel customerBot;

    @ManyToOne
    @JoinColumn(name = "admin_bot_id", nullable = false)
    private AdminBotModel adminBot;

    @ManyToOne
    @JoinColumn(name = "ai_assistant_id", nullable = false)
    private AiAssistantModel aiAssistant;

    @ManyToOne
    @JoinColumn(name = "user_ai_assistant_instruction_id")
    private UserAiAssistantInstructionModel userAiAssistantInstruction;

    public UUID getId() {
        return id;
    }

    public BusinessModel getBusiness() {
        return business;
    }

    public AiAssistantInstructionModel getAiAssistantStartInstruction() {
        return aiAssistantStartInstruction;
    }

    public UserAiAssistantInstructionModel getUserAiAssistantInstruction() {
        return userAiAssistantInstruction;
    }

    public void setUserAiAssistantInstruction(UserAiAssistantInstructionModel userAiAssistantInstruction) {
        this.userAiAssistantInstruction = userAiAssistantInstruction;
    }

    public CustomerBotModel getCustomerBot() {
        return customerBot;
    }

    public AdminBotModel getAdminBot() {
        return adminBot;
    }

    public AiAssistantModel getAiAssistant() {
        return aiAssistant;
    }
}
