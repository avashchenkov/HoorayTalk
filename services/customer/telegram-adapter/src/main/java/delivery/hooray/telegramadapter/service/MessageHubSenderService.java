package delivery.hooray.telegramadapter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class MessageHubSenderService {
    private final WebClient webClient;

    @Autowired
    public MessageHubSenderService(WebClient webClient) {
        this.webClient = webClient;
    }

    public void send(Object messagePayload) {
        webClient.post()
                .uri("/message-hub")
                .bodyValue(messagePayload)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }
}
