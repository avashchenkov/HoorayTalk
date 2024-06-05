package delivery.hooray.messagehub.model.common;

import delivery.hooray.messagehub.enums.MessageRole;
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

    public MessageModel(ChatModel chat, MessageRole messageRole, String content) {
        this();
        this.chat = chat;
        this.author = messageRole.name();
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

    public UUID getId() {
        return id;
    }

    public ChatModel getChat() {
        return chat;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public MessageRole getAuthor() {
        return MessageRole.valueOf(this.author);
    }

    public String getContent() {
        return content;
    }
}
