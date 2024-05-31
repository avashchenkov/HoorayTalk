package delivery.hooray.aiassistant.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Service
public class CompletionService {
    private final String openAiApiKey;

    public CompletionService(@Value("${OPENAI_API_KEY}") String openAiApiKey) {
        this.openAiApiKey = openAiApiKey;
    }

    public String complete(String systemMessage, String userMessage) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            URI uri = new URI("https://api.openai.com/v1/chat/completions");

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-3.5-turbo");
            requestBody.put("messages", new Object[] {
                    Map.of("role", "system", "content", systemMessage),
                    Map.of("role", "user", "content", userMessage)
            });

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
            return responseBody.get("choices").get(0).get("message").get("content").asText();
        } catch (Exception e) {
            throw new RuntimeException("Error during completion request", e);
        }
    }
}