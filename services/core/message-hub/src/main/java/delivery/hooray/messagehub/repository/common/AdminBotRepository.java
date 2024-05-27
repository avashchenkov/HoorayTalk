package delivery.hooray.messagehub.repository.common;

import delivery.hooray.messagehub.model.common.AdminBotModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdminBotRepository extends JpaRepository<AdminBotModel, UUID> {
}
