package delivery.hooray.telegramadapter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class MessageHubSenderService {
    private final WebClient webClient;

    @Autowired
    public MessageHubSenderService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> sendMessage(String message) {
        return webClient.post()
                .uri("https://fabf0576-fb3d-4d74-b8af-3c8d33986607.mock.pstmn.io/customer-message")
                .bodyValue(message)
                .retrieve()
                .bodyToMono(String.class);
    }
}
