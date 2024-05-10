package delivery.hooray.botadapterspringbootstarter.service;

import delivery.hooray.botadapterspringbootstarter.bot.MessageToMessageHubRequestData;
import delivery.hooray.botadapterspringbootstarter.config.WebClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class MessageHubSenderService {
    protected final WebClient webClient;
    protected String url;
    protected String messageHubApiKey;

    @Autowired
    public MessageHubSenderService(WebClientConfig webClientConfig,
                                   @Value("${MESSAGE_HUB_URL}") String url,
                                   @Value("${MESSAGE_HUB_API_KEY}") String messageHubApiKey) {
        this.webClient = webClientConfig.getWebClient();
        this.url = url;
        this.messageHubApiKey = messageHubApiKey;
    }

    public Mono<String> sendMessage(MessageToMessageHubRequestData messageFromCustomer) {
        return webClient.post()
                .uri(url)
                .header("API-KEY", messageHubApiKey)
                .bodyValue(messageFromCustomer)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> System.out.println("Message sent successfully: " + response))
                .doOnError(error -> System.err.println("Error sending message: " + error.getMessage()));
    }
}
