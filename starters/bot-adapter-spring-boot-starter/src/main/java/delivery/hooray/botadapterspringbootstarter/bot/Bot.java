package delivery.hooray.botadapterspringbootstarter.bot;

import delivery.hooray.botadapterspringbootstarter.service.BotBehavior;
import delivery.hooray.botadapterspringbootstarter.service.EncryptionService;
import delivery.hooray.botadapterspringbootstarter.service.MessageHubSenderService;

import java.util.UUID;

/**
 * Represents a bot that can send and receive messages.
 */
public class Bot {
    protected UUID id;
    protected String tokenEncrypted;
    protected final BotBehavior botBehavior;
    protected final EncryptionService encryptionService;

    protected final MessageHubSenderService messageHubSenderService;

    public Bot(MessageHubSenderService messageHubSenderService,
               BotBehavior botBehavior,
               EncryptionService encryptionService) {
        this.messageHubSenderService = messageHubSenderService;
        this.botBehavior = botBehavior;
        this.encryptionService = encryptionService;
    }

    public void run() {
        botBehavior.run(this);
    }

    public void sendMsgToCustomer(MessageToCustomerRequestData request) {
        botBehavior.sendMsgToClient(this, request);
    }

    public void sendMsgToMessageHub(MessageToMessageHubRequestData request) {
        messageHubSenderService.sendMessage(request);
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setTokenEncrypted(String token) {
        this.tokenEncrypted = token;
    }

    public String getBotToken() {
        return encryptionService.decrypt(tokenEncrypted);
    }

    public UUID getBotId() {
        return id;
    }
}