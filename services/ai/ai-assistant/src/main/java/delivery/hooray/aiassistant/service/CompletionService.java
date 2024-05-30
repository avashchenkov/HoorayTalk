package delivery.hooray.aiassistant.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CompletionService {
    private final String openAiApiKey;

    public CompletionService(@Value("${openai.api-key}") String openAiApiKey) {
        this.openAiApiKey = openAiApiKey;
    }
}
