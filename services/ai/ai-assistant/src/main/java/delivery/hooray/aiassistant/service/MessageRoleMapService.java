package delivery.hooray.aiassistant.service;

import delivery.hooray.aiassistant.enums.MessageRole;
import org.springframework.stereotype.Service;

import static delivery.hooray.aiassistant.enums.MessageRole.*;

@Service
public class MessageRoleMapService {
    public MessageRole fromOpenAiFormat(String openAiRole) {
        if (openAiRole.equals("system")) {
            return SYSTEM;
        } else if (openAiRole.equals("user")) {
            return USER;
        } else if (openAiRole.equals("assistant")) {
            return ASSISTANT;
        } else {
            throw new IllegalArgumentException("Unknown message role: " + openAiRole);
        }
    }

    public MessageRole fromMessageHubFormat(String messageHubRole) {
        if (messageHubRole.equals("CUSTOMER")) {
            return USER;
        } else if (messageHubRole.equals("AI") || messageHubRole.equals("ADMIN")) {
            return ASSISTANT;
        } else if (messageHubRole.equals("SYSTEM")) {
            return SYSTEM;
        } else {
            throw new IllegalArgumentException("Unknown message role: " + messageHubRole);
        }
    }

    public String toOpenAiFormat(MessageRole messageRole) {
        if (messageRole == SYSTEM) {
            return "system";
        } else if (messageRole == USER) {
            return "user";
        } else if (messageRole == ASSISTANT) {
            return "assistant";
        } else {
            throw new IllegalArgumentException("Unknown message role: " + messageRole);
        }
    }
}
