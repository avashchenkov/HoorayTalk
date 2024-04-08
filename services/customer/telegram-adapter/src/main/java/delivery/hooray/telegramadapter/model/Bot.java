package delivery.hooray.telegramadapter.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bot")
public class Bot {
    @Id
    @Column(name = "id", nullable = false, length = 500)
    private String id;

    @Column(name = "token_encrypted", nullable = false, unique = true, length = 500)
    private String tokenEncrypted;

    public Bot(String id, String tokenEncrypted) {
        this.id = id;
        this.tokenEncrypted = tokenEncrypted;
    }

    protected Bot() {}

    public String getId() {
        return id;
    }

    public String getTokenEncrypted() {
        return tokenEncrypted;
    }
}
