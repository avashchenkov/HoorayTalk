package delivery.hooray.telegramadapter.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "bot_config")
public class BotConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected UUID id;

    @Column(name = "token_encrypted", nullable = false, unique = true)
    protected String tokenEncrypted;

    @Column
    protected String username;

    protected BotConfig() {}

    public BotConfig(String tokenEncrypted) {
        this.setToken(tokenEncrypted);
    }

    public UUID getId() {
        return id;
    }

    public String getToken() {
        return this.tokenEncrypted;
    }

    public void setToken(String tokenEncrypted) {
        this.tokenEncrypted = tokenEncrypted;
    }

    public String getUsername() {
        return username;
    }

public void setUsername(String username) {
        this.username = username;
    }
}