package delivery.hooray.telegramadapter.bot;

import delivery.hooray.botadapterspringbootstarter.bot.Bot;
import delivery.hooray.botadapterspringbootstarter.bot.MessageToCustomerRequestData;
import delivery.hooray.botadapterspringbootstarter.service.BotBehavior;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Service
public class TelegramBotBehavior implements BotBehavior {
    private final TelegramBotsApi botsApi;

    public TelegramBotBehavior() {
        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param bot
     */
    @Override
    public void run(Bot bot) {
        TelegramBot telegramBot = (TelegramBot) bot;

        try {
            botsApi.registerBot(telegramBot.getTelegramBotImpl());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param bot
     * @param request
     */
    @Override
    public void sendMsgToClient(Bot bot, MessageToCustomerRequestData request) {
        TelegramBot telegramBot = (TelegramBot) bot;

        telegramBot.getTelegramBotImpl().sendMsgToClient(request);
    }

    @Override
    public Class<? extends Bot> getBotClass() {
        return TelegramBot.class;
    }
}
