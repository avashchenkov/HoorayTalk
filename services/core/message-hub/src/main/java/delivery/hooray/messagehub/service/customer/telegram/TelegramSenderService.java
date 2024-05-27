package delivery.hooray.messagehub.service.customer.telegram;

import delivery.hooray.messagehub.config.WebClientConfig;
import delivery.hooray.telegramadapter.model.SendMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class TelegramSenderService {
    private static final Logger logger = LoggerFactory.getLogger(TelegramSenderService.class);
    protected final WebClient webClient;
    protected String url;
    protected String messageHubApiKey;

    @Autowired
    public TelegramSenderService(WebClientConfig webClientConfig,
                                 @Value("${TELEGRAM_ADAPTER_URL}") String url,
                                 @Value("${API_KEY}") String messageHubApiKey) {
        this.webClient = webClientConfig.getWebClient().mutate()
                .filter(logRequest())
                .filter(logResponse())
                .build();
        this.url = url;
        this.messageHubApiKey = messageHubApiKey;
    }

    public Mono<String> sendMessage(SendMessageRequest messageToAdmin) {
        return webClient.post()
                .uri(url)
                .header("X-API-KEY", messageHubApiKey)
                .bodyValue(messageToAdmin)
                .retrieve()
                .onStatus(status -> status.isError(), response -> response.bodyToMono(String.class).flatMap(body -> Mono.error(new RuntimeException("Error from server: " + body))))
                .bodyToMono(String.class)
                .doOnSuccess(response -> logger.info("Message sent successfully: {}", response))
                .doOnError(error -> logger.error("Error sending message: ", error));
    }

    private static org.springframework.web.reactive.function.client.ExchangeFilterFunction logRequest() {
        return org.springframework.web.reactive.function.client.ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            logger.info("Sending request to URL: {} Method: {}", clientRequest.url(), clientRequest.method());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> logger.info("{}: {}", name, value)));
            return Mono.just(clientRequest);
        });
    }

    private static org.springframework.web.reactive.function.client.ExchangeFilterFunction logResponse() {
        return org.springframework.web.reactive.function.client.ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            logger.info("Received response with status code: {}", clientResponse.statusCode());
            clientResponse.headers().asHttpHeaders().forEach((name, values) -> values.forEach(value -> logger.info("{}: {}", name, value)));
            return Mono.just(clientResponse);
        });
    }
}