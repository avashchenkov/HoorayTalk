package delivery.hooray.messagehub.repository;

import delivery.hooray.messagehub.model.AiAssistantModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AiAssistantRepository extends JpaRepository<AiAssistantModel, UUID> {
}
