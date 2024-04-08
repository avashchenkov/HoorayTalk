package delivery.hooray.telegramadapter.repository;

import delivery.hooray.telegramadapter.model.Bot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BotRepository extends JpaRepository<Bot, String> {}