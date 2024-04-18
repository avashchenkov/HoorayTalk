package delivery.hooray.telegramadapter.repository;

import delivery.hooray.telegramadapter.model.BotConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BotConfigRepository extends JpaRepository<BotConfig, UUID> {}