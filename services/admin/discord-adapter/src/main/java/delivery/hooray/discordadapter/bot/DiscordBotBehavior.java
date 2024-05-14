package delivery.hooray.discordadapter.bot;

import delivery.hooray.botadapterspringbootstarter.bot.Bot;
import delivery.hooray.botadapterspringbootstarter.bot.MessageToCustomerRequestData;
import delivery.hooray.botadapterspringbootstarter.service.BotBehavior;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.stereotype.Service;

@Service
public class DiscordBotBehavior implements BotBehavior {

    /**
     * @param bot
     */
    @Override
    public void run(Bot bot) {
        DiscordBot discordBot = (DiscordBot) bot;

        JDABuilder builder = JDABuilder.createDefault(discordBot.getBotToken());

        builder.enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT);

        builder.addEventListeners(discordBot.getDiscordBotImpl());
        builder.build();
    }

    /**
     * @param bot
     * @param request
     */
    @Override
    public void sendMsgToClient(Bot bot, MessageToCustomerRequestData request) {
        DiscordBot discordBot = (DiscordBot) bot;

        discordBot.getDiscordBotImpl().sendMsgToClient(request);
    }

    @Override
    public Class<? extends Bot> getBotClass() {
        return DiscordBot.class;
    }
}
