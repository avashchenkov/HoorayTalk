package delivery.hooray.aiassistant.api.controller;

import delivery.hooray.aiassistant.api.ChatApi;
import delivery.hooray.aiassistant.model.CompleteChatRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatApiImpl implements ChatApi {

    @Override
    public ResponseEntity<String> generateResponse(CompleteChatRequest completeChatRequest) {
        if (completeChatRequest == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            String response = "{\n" +
                    "  \"message\":\"This is the AI Assistant response.\",\n" +
                    "  \"isAdminActionRequired\": true\n" +
                    "}";

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}