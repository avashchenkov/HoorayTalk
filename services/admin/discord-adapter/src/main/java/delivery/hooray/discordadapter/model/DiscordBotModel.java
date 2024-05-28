package delivery.hooray.discordadapter.model;

import delivery.hooray.botadapterspringbootstarter.model.BotModel;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Discord")
public class DiscordBotModel extends BotModel {

    @Column(name = "guild_id")
    private String guildId;

    protected DiscordBotModel() {}

    public DiscordBotModel(String tokenEncrypted) {
        super(tokenEncrypted);
    }

    public String getGuildId() {
        return guildId;
    }
}