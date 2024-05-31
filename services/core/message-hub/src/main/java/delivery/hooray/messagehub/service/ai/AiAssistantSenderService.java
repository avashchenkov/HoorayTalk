
package delivery.hooray.messagehub.service.ai;

import delivery.hooray.aiassistant.model.CompleteChatRequest;
import delivery.hooray.messagehub.config.WebClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AiAssistantSenderService {
    private static final Logger logger = LoggerFactory.getLogger(AiAssistantSenderService.class);
    protected final WebClient webClient;
    protected String url;
    protected String messageHubApiKey;

    @Autowired
    public AiAssistantSenderService(WebClientConfig webClientConfig,
                                 @Value("${AI_ASSISTANT_URL}") String url,
                                 @Value("${API_KEY}") String apiKey) {
        this.webClient = webClientConfig.getWebClient().mutate()
                .filter(logRequest())
                .filter(logResponse())
                .build();
        this.url = url;
        this.messageHubApiKey = apiKey;
    }

    public Mono<String> sendMessage(CompleteChatRequest completeChatRequest) {
        return webClient.post()
                .uri(url)
                .header("X-API-KEY", messageHubApiKey)
                .bodyValue(completeChatRequest)
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