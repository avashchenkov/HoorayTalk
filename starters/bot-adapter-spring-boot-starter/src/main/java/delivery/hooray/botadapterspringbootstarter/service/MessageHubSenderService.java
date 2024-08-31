package delivery.hooray.botadapterspringbootstarter.service;

import delivery.hooray.botadapterspringbootstarter.bot.MessageToMessageHubRequestData;
import delivery.hooray.botadapterspringbootstarter.config.WebClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@ConditionalOnMissingBean
public class MessageHubSenderService {
    protected static final Logger logger = LoggerFactory.getLogger(MessageHubSenderService.class);
    protected final WebClient webClient;
    protected String messageHubUrl;
    protected String messageHubMessagePath;
    protected String messageHubApiKey;

    @Autowired
    public MessageHubSenderService(WebClientConfig webClientConfig,
                                   @Value("${MESSAGE_HUB_URL}") String messageHubUrl,
                                   @Value("${MESSAGE_HUB_MESSAGE_PATH}") String messageHubMessagePath,
                                   @Value("${MESSAGE_HUB_API_KEY}") String messageHubApiKey) {
        this.webClient = webClientConfig.getWebClient().mutate()
                .filter(logRequest())
                .filter(logResponse())
                .build();
        this.messageHubUrl = messageHubUrl;
        this.messageHubMessagePath = messageHubMessagePath;
        this.messageHubApiKey = messageHubApiKey;
    }

    public Mono<String> sendMessage(MessageToMessageHubRequestData message) {
        return webClient.post()
                .uri(messageHubUrl + messageHubMessagePath)
                .header("X-API-KEY", messageHubApiKey)
                .bodyValue(message)
                .retrieve()
                .onStatus(status -> status.isError(), response -> response.bodyToMono(String.class).flatMap(body -> Mono.error(new RuntimeException("Error from server: " + body))))
                .bodyToMono(String.class)
                .doOnSuccess(response -> logger.info("Message sent successfully: {}", response))
                .doOnError(error -> logger.error("Error sending message: ", error));
    }

    protected static org.springframework.web.reactive.function.client.ExchangeFilterFunction logRequest() {
        return org.springframework.web.reactive.function.client.ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            logger.info("Sending request to URL: {} Method: {}", clientRequest.url(), clientRequest.method());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> logger.info("{}: {}", name, value)));
            return Mono.just(clientRequest);
        });
    }

    protected static org.springframework.web.reactive.function.client.ExchangeFilterFunction logResponse() {
        return org.springframework.web.reactive.function.client.ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            logger.info("Received response with status code: {}", clientResponse.statusCode());
            clientResponse.headers().asHttpHeaders().forEach((name, values) -> values.forEach(value -> logger.info("{}: {}", name, value)));
            return Mono.just(clientResponse);
        });
    }
}