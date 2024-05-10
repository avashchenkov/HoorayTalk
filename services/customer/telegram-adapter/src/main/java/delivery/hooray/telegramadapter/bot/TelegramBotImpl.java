package delivery.hooray.telegramadapter.bot;

import delivery.hooray.botadapterspringbootstarter.bot.MessageToCustomerRequestData;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.UUID;

public class TelegramBotImpl extends TelegramLongPollingBot {
    private final TelegramBot telegramBot;


    public TelegramBotImpl(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }


    /**
     * @param update
     */
    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update.getMessage().getText());
    }


    /**
     * @return
     */
    public void sendMsgToClient(MessageToCustomerRequestData data) {
        System.out.println(data.getMessage());
    }


    /**
     * @return
     */
    @Override
    public String getBotUsername() {
        return getTelegramBot().getUsername();  // TODO: rename Bot to BotData or BotDto everywhere
    }


    @Override
    public String getBotToken() {
        return this.getTelegramBot().getBotToken();
    }


    public UUID getBotId() {  // TODO: move it to the parent interface if the ID is needed for the class (?)
        return this.getTelegramBot().getBotId();
    }


    private TelegramBot getTelegramBot() {
        return this.telegramBot;
    }
}
