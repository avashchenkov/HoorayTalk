package delivery.hooray.telegramadapter.model;

import delivery.hooray.botadapterspringbootstarter.model.BotModel;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Telegram")
public class TelegramBotModel extends BotModel {
    @Column
    protected String username;

    protected TelegramBotModel() {}

    public TelegramBotModel(String tokenEncrypted) {
        super(tokenEncrypted);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}