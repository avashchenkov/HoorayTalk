package delivery.hooray.messagehub.repository.common;

import delivery.hooray.messagehub.model.common.AiAssistantInstructionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserAiAssistantInstructionRepository extends JpaRepository<AiAssistantInstructionModel, UUID> {
}
