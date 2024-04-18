package delivery.hooray.telegramadapter.telegram;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class TelegramUpdateProcessor {

    public String processUpdate(Update update) {
        String update_details = "";

        if (update.hasMessage()) {
            update_details += "Message: " + update.getMessage().getText();
        }

        return update_details;
    }
}

