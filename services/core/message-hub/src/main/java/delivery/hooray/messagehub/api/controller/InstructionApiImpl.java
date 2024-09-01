package delivery.hooray.messagehub.api.controller;

import delivery.hooray.messagehub.api.InstructionApi;
import delivery.hooray.messagehub.model.HandleAdminAiAssistantInstructionRequest;
import delivery.hooray.messagehub.service.admin.AiAssistantInstructionService;
import delivery.hooray.messagehub.service.admin.InstructionDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InstructionApiImpl implements InstructionApi {
    private final AiAssistantInstructionService aiAssistantInstructionService;

    @Autowired
    public InstructionApiImpl(AiAssistantInstructionService aiAssistantInstructionService) {
        this.aiAssistantInstructionService = aiAssistantInstructionService;
    }

    @Override
    public ResponseEntity<Void> saveAdminAIAssistantInstruction(@Valid HandleAdminAiAssistantInstructionRequest handleAdminAiAssistantInstructionRequest) {
        InstructionDto instructionDto = new InstructionDto();

        instructionDto.setInstruction(handleAdminAiAssistantInstructionRequest.getInstruction());
        instructionDto.setBotId(handleAdminAiAssistantInstructionRequest.getBotId());
        aiAssistantInstructionService.saveAdminAIAssistantInstruction(instructionDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
