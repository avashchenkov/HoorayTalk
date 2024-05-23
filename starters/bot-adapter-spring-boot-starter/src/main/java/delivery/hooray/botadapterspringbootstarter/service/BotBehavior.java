package delivery.hooray.botadapterspringbootstarter.service;

import delivery.hooray.botadapterspringbootstarter.bot.Bot;
import delivery.hooray.botadapterspringbootstarter.bot.MessageToBotEndUserRequestData;
import org.springframework.stereotype.Service;

@Service
public interface BotBehavior {
    public void run(Bot bot);

    public void sendMsgToBotApi(Bot bot, MessageToBotEndUserRequestData request);

    public Class<? extends Bot> getBotClass();
}
