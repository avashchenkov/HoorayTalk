package delivery.hooray.botadapterspringbootstarter.service;

import delivery.hooray.botadapterspringbootstarter.bot.Bot;
import delivery.hooray.botadapterspringbootstarter.bot.MessageToCustomerRequestData;
import delivery.hooray.botadapterspringbootstarter.model.BotModel;
import org.springframework.stereotype.Service;

@Service
public interface BotBehavior {
    public void run(Bot bot);

    public void sendMsgToClient(Bot bot, MessageToCustomerRequestData request);

    public Class<? extends Bot> getBotClass();
}
