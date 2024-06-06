package delivery.hooray.aiassistant.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import delivery.hooray.aiassistant.enums.MessageRole;
import delivery.hooray.aiassistant.model.CompleteChatRequestRecentMessagesInner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static delivery.hooray.aiassistant.enums.MessageRole.SYSTEM;

@Service
public class CompletionService {
    private final String openAiApiKey;
    private final MessageRoleService messageRoleService;

    @Autowired
    public CompletionService(@Value("${OPENAI_API_KEY}") String openAiApiKey,
                             MessageRoleService messageRoleService) {
        this.openAiApiKey = openAiApiKey;
        this.messageRoleService = messageRoleService;
    }

    public String complete(String systemMessage, List<CompleteChatRequestRecentMessagesInner> recentMessages) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            URI uri = new URI("https://api.openai.com/v1/chat/completions");

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", messageRoleService.toOpenAiFormat(SYSTEM), "content", systemMessage));

            for (CompleteChatRequestRecentMessagesInner message : recentMessages) {
                MessageRole messageRole = messageRoleService.fromMessageHubFormat(message.getRole());

                messages.add(Map.of("role", messageRoleService.toOpenAiFormat(messageRole), "content", message.getContent()));
            }

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-3.5-turbo");
            requestBody.put("messages", messages);

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBodyString = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + openAiApiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBodyString))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to get response from OpenAI: " + response.body());
            }

            JsonNode responseBody = objectMapper.readTree(response.body());
            JsonNode choices = responseBody.get("choices");
            if (choices != null && choices.size() > 0) {
                JsonNode messageNode = choices.get(0).get("message");
                if (messageNode != null) {
                    return messageNode.get("content").asText();
                }
            }
            throw new RuntimeException("Invalid response structure from OpenAI: " + response.body());
        } catch (Exception e) {
            throw new RuntimeException("Error during completion request", e);
        }
    }
}