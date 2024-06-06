package delivery.hooray.aiassistant.api.controller;

import delivery.hooray.aiassistant.api.ChatApi;
import delivery.hooray.aiassistant.model.CompleteChatRequest;
import delivery.hooray.aiassistant.service.CompletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatApiImpl implements ChatApi {
    public final CompletionService completionService;

    @Autowired
    public ChatApiImpl(CompletionService completionService) {
        this.completionService = completionService;
    }

    @Override
    public ResponseEntity<String> generateResponse(CompleteChatRequest completeChatRequest) {
        if (completeChatRequest == null) {
            return ResponseEntity.badRequest().build();
        }

        String response = completionService.complete(completeChatRequest.getSystemMessage(),
                                                     completeChatRequest.getRecentMessages());

        try {
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}