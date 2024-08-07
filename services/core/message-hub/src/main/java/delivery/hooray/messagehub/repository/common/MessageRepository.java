package delivery.hooray.messagehub.repository.common;

import delivery.hooray.messagehub.model.common.MessageModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<MessageModel, UUID> {
    List<MessageModel> findByChatIdOrderByTimestampDesc(UUID chatId, Pageable pageable);

    List<MessageModel> findByChatIdAndTimestampAfter(UUID chat_id, Instant timestamp);
}