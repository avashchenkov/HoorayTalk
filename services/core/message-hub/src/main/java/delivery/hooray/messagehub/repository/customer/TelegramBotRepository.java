package delivery.hooray.messagehub.repository.customer;

import delivery.hooray.messagehub.model.customer.TelegramBotModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TelegramBotRepository extends JpaRepository<TelegramBotModel, UUID> {
}
