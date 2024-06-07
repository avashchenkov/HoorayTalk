package delivery.hooray.messagehub.repository.common;

import delivery.hooray.messagehub.model.common.TenantModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TenantRepository extends JpaRepository<TenantModel, UUID> {
    TenantModel findByCustomerBot_Id(UUID botId);
}
