package delivery.hooray.telegramadapter.bot;

import delivery.hooray.botadapterspringbootstarter.bot.Bot;
import delivery.hooray.botadapterspringbootstarter.bot.MessageToBotEndUserRequestData;
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
    public void sendMsgToBotApi(Bot bot, MessageToBotEndUserRequestData request) {
        TelegramBot telegramBot = (TelegramBot) bot;
        MessageToTelegramBotEndUserRequestData requestData = (MessageToTelegramBotEndUserRequestData) request;

        telegramBot.getTelegramBotImpl().sendMsgToClient(requestData);
    }

    @Override
    public Class<? extends Bot> getBotClass() {
        return TelegramBot.class;
    }
}
