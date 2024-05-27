package delivery.hooray.messagehub.repository.admin;

import delivery.hooray.messagehub.model.admin.DiscordServerBotModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DiscordServerBotRepository extends JpaRepository<DiscordServerBotModel, UUID> {
}
