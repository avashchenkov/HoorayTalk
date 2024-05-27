package delivery.hooray.messagehub.repository.common;

import delivery.hooray.messagehub.model.common.CustomerBotModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerBotRepository extends JpaRepository<CustomerBotModel, UUID> {
}
