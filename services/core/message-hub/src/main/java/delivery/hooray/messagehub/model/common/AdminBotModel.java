package delivery.hooray.messagehub.model.common;

import jakarta.persistence.*;

import java.util.UUID;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "admin_bot")
public class AdminBotModel {

    protected AdminBotModel() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected UUID id;

    public UUID getId() {
        return id;
    }
}
