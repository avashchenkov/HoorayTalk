package delivery.hooray.messagehub.repository;

import delivery.hooray.messagehub.model.ChatModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<ChatModel, UUID> {
}
