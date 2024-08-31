package delivery.hooray.discordadapter.service;

import delivery.hooray.botadapterspringbootstarter.config.WebClientConfig;
import delivery.hooray.botadapterspringbootstarter.service.MessageHubSenderService;
import delivery.hooray.discordadapter.bot.InstructionToMessageHubRequestData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DiscordToMessageHubSenderService extends MessageHubSenderService {
    protected String messageHubInstructionPath;

    public DiscordToMessageHubSenderService(WebClientConfig webClientConfig,
                                            @Value("${MESSAGE_HUB_URL}") String messageHubUrl,
                                            @Value("${MESSAGE_HUB_MESSAGE_PATH}") String messageHubMessagePath,
                                            @Value("${MESSAGE_HUB_INSTRUCTION_PATH}") String messageHubInstructionPath,
                                            @Value("${MESSAGE_HUB_API_KEY}") String messageHubApiKey) {
        super(webClientConfig, messageHubUrl, messageHubMessagePath, messageHubApiKey);

        this.messageHubInstructionPath = messageHubInstructionPath;
    }

    public Mono<String> sendInstruction(InstructionToMessageHubRequestData message) {
        return webClient.post()
                .uri(messageHubUrl + messageHubInstructionPath)
                .header("X-API-KEY", messageHubApiKey)
                .bodyValue(message)
                .retrieve()
                .onStatus(status -> status.isError(), response -> response.bodyToMono(String.class).flatMap(body -> Mono.error(new RuntimeException("Error from server: " + body))))
                .bodyToMono(String.class)
                .doOnSuccess(response -> logger.info("Instruction sent successfully: {}", response))
                .doOnError(error -> logger.error("Error sending message: ", error));
    }
}
