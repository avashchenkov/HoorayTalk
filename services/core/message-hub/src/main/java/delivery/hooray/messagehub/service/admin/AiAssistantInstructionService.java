package delivery.hooray.messagehub.service.admin;

import delivery.hooray.messagehub.model.common.AdminAiAssistantInstructionModel;
import delivery.hooray.messagehub.model.common.TenantModel;
import delivery.hooray.messagehub.repository.common.AdminAiAssistantInstructionRepository;
import delivery.hooray.messagehub.repository.common.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AiAssistantInstructionService {
    private final AdminAiAssistantInstructionRepository adminAiAssistantInstructionRepository;
    private final TenantRepository tenantRepository;

    @Autowired
    public AiAssistantInstructionService(AdminAiAssistantInstructionRepository adminAiAssistantInstructionRepository,
                                         TenantRepository tenantRepository) {
        this.adminAiAssistantInstructionRepository = adminAiAssistantInstructionRepository;
        this.tenantRepository = tenantRepository;
    }

    public void saveAdminAIAssistantInstruction(InstructionDto instructionDto) {
        TenantModel tenantModel = tenantRepository.findByAdminBot_Id(UUID.fromString(instructionDto.getBotId()));
        AdminAiAssistantInstructionModel adminAiAssistantInstructionModel = tenantModel.getAdminAiAssistantInstruction();

        adminAiAssistantInstructionModel.setText(instructionDto.getInstruction());
        adminAiAssistantInstructionRepository.save(adminAiAssistantInstructionModel);
    }
}
