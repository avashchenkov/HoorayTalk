package delivery.hooray.telegramadapter.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "bot")
public class Bot {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Lob
    @Column(name = "token_encrypted", nullable = false, columnDefinition = "bytea")  // Указываем точный тип данных для PostgreSQL
    private byte[] tokenEncrypted;

    public Bot() {}

    public Bot(byte[] tokenEncrypted) {
        this.tokenEncrypted = tokenEncrypted;
    }

    public UUID getId() {
        return id;
    }

    public byte[] getTokenEncrypted() {
        return tokenEncrypted;
    }

    public void setTokenEncrypted(byte[] tokenEncrypted) {
        this.tokenEncrypted = tokenEncrypted;
    }
}