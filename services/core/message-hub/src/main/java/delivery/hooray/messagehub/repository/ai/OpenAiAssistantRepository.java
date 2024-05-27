package delivery.hooray.messagehub.repository.ai;

import delivery.hooray.messagehub.model.ai.OpenAiAssistantModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OpenAiAssistantRepository extends JpaRepository<OpenAiAssistantModel, UUID> {
}
