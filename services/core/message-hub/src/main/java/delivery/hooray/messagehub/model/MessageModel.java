package delivery.hooray.messagehub.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "message", indexes = {
        @Index(name = "idx_chat_id", columnList = "chat_id"),
        @Index(name = "idx_timestamp", columnList = "timestamp")
})
public class MessageModel {

    protected MessageModel() {
        this.timestamp = Instant.now();
    }

    public MessageModel(ChatModel chat, String author, String content) {
        this();
        this.chat = chat;
        this.author = author;
        this.content = content;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected UUID id;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    protected ChatModel chat;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    private Instant timestamp;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;
}
