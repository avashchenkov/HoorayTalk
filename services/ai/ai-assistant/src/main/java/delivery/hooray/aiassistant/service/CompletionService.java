package delivery.hooray.aiassistant.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import delivery.hooray.aiassistant.enums.MessageRole;
import delivery.hooray.aiassistant.model.CompleteChatRequestRecentMessagesInner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import delivery.hooray.sharedlib.Message;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static delivery.hooray.aiassistant.enums.MessageRole.*;

@Service
public class CompletionService {
    private final String openAiApiKey;
    private final MessageRoleMapService messageRoleMapService;

    @Autowired
    public CompletionService(@Value("${OPENAI_API_KEY}") String openAiApiKey,
                             MessageRoleMapService messageRoleMapService) {
        this.openAiApiKey = openAiApiKey;
        this.messageRoleMapService = messageRoleMapService;
    }

    public String complete(String systemMessage, List<CompleteChatRequestRecentMessagesInner> recentMessages) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            URI uri = new URI("https://api.openai.com/v1/chat/completions");

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", messageRoleMapService.toOpenAiFormat(SYSTEM), "content", systemMessage));

            for (CompleteChatRequestRecentMessagesInner message : recentMessages) {
                MessageRole messageRole = messageRoleMapService.fromMessageHubFormat(message.getRole());
                String content;

                if (messageRole == USER) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Message messageContent;

                    try {
                        messageContent = objectMapper.readValue(message.getContent(), Message.class);

                        content = messageContent.getText();
                    } catch (IOException e) {
                        e.printStackTrace();

                        throw new RuntimeException("Failed to parse message content", e);
                    }
                } else {
                    content = message.getContent();
                }

                messages.add(Map.of("role", messageRoleMapService.toOpenAiFormat(messageRole), "content", content));
            }

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-4-turbo");
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