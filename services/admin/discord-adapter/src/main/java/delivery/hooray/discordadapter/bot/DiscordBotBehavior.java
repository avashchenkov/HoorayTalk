package delivery.hooray.discordadapter.bot;

import delivery.hooray.botadapterspringbootstarter.bot.Bot;
import delivery.hooray.botadapterspringbootstarter.bot.MessageToBotEndUserRequestData;
import delivery.hooray.botadapterspringbootstarter.service.BotBehavior;
import org.springframework.stereotype.Service;

@Service
public class DiscordBotBehavior implements BotBehavior {

    /**
     * @param bot
     */
    @Override
    public void run(Bot bot) {
        bot.run();
    }

    /**
     * @param bot
     * @param request
     */
    @Override
    public void sendMsgToBotApi(Bot bot, MessageToBotEndUserRequestData request) {
        DiscordBot discordBot = (DiscordBot) bot;
        MessageToDiscordBotEndUserRequestData discordBotRequest = (MessageToDiscordBotEndUserRequestData) request;

        discordBot.getDiscordBotImpl().sendMsgToAdmins(discordBotRequest);
    }

    @Override
    public Class<? extends Bot> getBotClass() {
        return DiscordBot.class;
    }
}
