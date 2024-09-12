package delivery.hooray.discordadapter.service;

import delivery.hooray.botadapterspringbootstarter.config.WebClientConfig;
import delivery.hooray.discordadapter.bot.InstructionToMessageHubRequestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AdminAiAssistantInstructionSenderService{
    protected static final Logger logger = LoggerFactory.getLogger(AdminAiAssistantInstructionSenderService.class);
    protected String messageHubInstructionPath;
    protected String messageHubUrl;
    protected String messageHubApiKey;
    protected WebClient webClient;

    public AdminAiAssistantInstructionSenderService(WebClientConfig webClientConfig,
                                            @Value("${MESSAGE_HUB_URL}") String messageHubUrl,
                                            @Value("${MESSAGE_HUB_INSTRUCTION_PATH}") String messageHubInstructionPath,
                                            @Value("${MESSAGE_HUB_API_KEY}") String messageHubApiKey) {
        this.webClient = webClientConfig.getWebClient();
        this.messageHubUrl = messageHubUrl;
        this.messageHubInstructionPath = messageHubInstructionPath;
        this.messageHubApiKey = messageHubApiKey;
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
