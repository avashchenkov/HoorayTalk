package delivery.hooray.messagehub.service.ai;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AiAssistantResponse {
    private String message;

    @JsonProperty("isAdminActionRequired")
    private boolean adminActionRequired;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAdminActionRequired() {
        return adminActionRequired;
    }

    public void setAdminActionRequired(boolean adminActionRequired) {
        this.adminActionRequired = adminActionRequired;
    }
}