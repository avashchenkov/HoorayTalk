package delivery.hooray.messagehub.model.admin;

import delivery.hooray.messagehub.model.common.AdminBotModel;
import jakarta.persistence.*;


@Entity
@Table(name = "discord_server_admin_bot")
public class DiscordServerBotModel extends AdminBotModel {

    protected DiscordServerBotModel() {}

    @Column(name= "guild_id", nullable = false)
    private String guildId;

    public String getGuildId() {
        return guildId;
    }
}
