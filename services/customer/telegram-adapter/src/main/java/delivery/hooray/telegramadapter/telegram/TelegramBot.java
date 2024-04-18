package delivery.hooray.telegramadapter.telegram;

import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.UUID;

/**
 * Represents a Telegram bot that can send and receive messages.
 */
public class TelegramBot extends TelegramLongPollingBot {
    protected final UUID id;
    protected final String username;
    protected final String token;

    protected final TelegramUpdateProcessor updateProcessor;
    protected final WebClient webClient;

    public TelegramBot(UUID id, String username, String token) {
        this.id = id;
        this.username = username;
        this.token = token;
        this.updateProcessor = new TelegramUpdateProcessor();
        this.webClient = WebClient.create();
    }

    @Override
    public void onUpdateReceived(Update update) {
        String processedMsg = this.updateProcessor.processUpdate(update);

        webClient.post()
                .uri("https://fabf0576-fb3d-4d74-b8af-3c8d33986607.mock.pstmn.io/customer-message")
                .bodyValue(processedMsg)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(result -> System.out.println("Message: " + result),
                        error -> System.err.println("Error during HTTP POST: " + error.getMessage()));
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
