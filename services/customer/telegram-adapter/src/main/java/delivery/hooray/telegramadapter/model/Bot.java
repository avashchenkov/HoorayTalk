package delivery.hooray.telegramadapter.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "bot")
public class Bot {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2")
    protected UUID id;

    @Column(name = "token_encrypted", nullable = false)
    protected String tokenEncrypted;

    protected Bot() {}

    protected Bot(String tokenEncrypted) {
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
}