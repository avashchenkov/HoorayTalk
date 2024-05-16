package delivery.hooray.messagehub.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "business")
public class BusinessModel {

    protected BusinessModel() {}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected UUID id;

    @Column(name = "name", nullable = false, unique = true)
    protected String name;

    @Column(name = "info")
    protected String info;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }
}
