package delivery.hooray.aiassistant.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CompletionService {
    private final String openAiApiKey;

    public CompletionService(@Value("${OPENAI_API_KEY}") String openAiApiKey) {
        this.openAiApiKey = openAiApiKey;
    }
}
