package delivery.hooray.messagehub.repository;

import delivery.hooray.messagehub.model.BusinessModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BusinessRepository extends JpaRepository<BusinessModel, UUID> {
}
