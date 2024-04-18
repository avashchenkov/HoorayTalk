package delivery.hooray.telegramadapter.service;

import java.util.UUID;

public class BotConfigDto {
    protected UUID id;
    protected String token;
    protected String username;

    public BotConfigDto(UUID id, String token, String username) {
        this.id = id;
        this.token = token;
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }
}
