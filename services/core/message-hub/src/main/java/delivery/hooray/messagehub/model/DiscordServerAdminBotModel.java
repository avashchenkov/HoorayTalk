package delivery.hooray.messagehub.model;

import jakarta.persistence.*;

import java.util.UUID;


@Entity
@Table(name = "discord_server_admin_bot")
public class DiscordServerAdminBotModel extends AdminBotModel {

    protected DiscordServerAdminBotModel() {}

    @Column(name= "guild_id", nullable = false)
    private String guildId;

    public String getGuildId() {
        return guildId;
    }
}
