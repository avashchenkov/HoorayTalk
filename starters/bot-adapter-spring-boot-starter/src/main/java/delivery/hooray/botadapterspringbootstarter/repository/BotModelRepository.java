package delivery.hooray.botadapterspringbootstarter.repository;

import delivery.hooray.botadapterspringbootstarter.model.BotModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BotModelRepository extends JpaRepository<BotModel, UUID> {}