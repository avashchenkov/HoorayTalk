package delivery.hooray.botadapterspringbootstarter.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "bot")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
public abstract class BotModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected UUID id;

    @Column(name = "token_encrypted", nullable = false, unique = true)
    protected String tokenEncrypted;

    protected BotModel() {}

    public BotModel(String tokenEncrypted) {
        this.setToken(tokenEncrypted);
    }

    public UUID getId() {
        return id;
    }

    public String getTokenEncrypted() {
        return this.tokenEncrypted;
    }

    public void setToken(String tokenEncrypted) {
        this.tokenEncrypted = tokenEncrypted;
    }
}