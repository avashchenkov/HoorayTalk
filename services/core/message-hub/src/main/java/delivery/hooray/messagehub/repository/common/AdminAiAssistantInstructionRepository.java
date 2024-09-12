package delivery.hooray.messagehub.repository.common;

import delivery.hooray.messagehub.model.common.AdminAiAssistantInstructionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdminAiAssistantInstructionRepository extends JpaRepository<AdminAiAssistantInstructionModel, UUID> {
}
