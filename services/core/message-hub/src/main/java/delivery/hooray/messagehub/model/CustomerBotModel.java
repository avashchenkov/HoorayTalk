package delivery.hooray.messagehub.model;

import jakarta.persistence.*;

import java.util.UUID;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "customer_bot")
public class CustomerBotModel {

    protected CustomerBotModel() {}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected UUID id;

    public UUID getId() {
        return id;
    }
}
