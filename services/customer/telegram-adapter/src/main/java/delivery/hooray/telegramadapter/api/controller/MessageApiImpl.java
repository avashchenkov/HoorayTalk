package delivery.hooray.telegramadapter.api.controller;

import delivery.hooray.telegramadapter.api.MessageApi;
import delivery.hooray.telegramadapter.model.SendMessageRequest;
import delivery.hooray.telegramadapter.service.BotService;
import delivery.hooray.telegramadapter.telegram.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RestController
public class MessageApiImpl implements MessageApi {
    protected final BotService botService;

    @Autowired
    public MessageApiImpl(BotService botService) {
        this.botService = botService;
    }

    @Override
    public ResponseEntity<Void> sendMessage(SendMessageRequest sendMessageRequest) {
        try {
            SendMessage message = new SendMessage();

            message.setChatId(sendMessageRequest.getCustomerChatId());
            message.setText(sendMessageRequest.getMessage());

            TelegramBot bot = botService.getBot(sendMessageRequest.getBotId());

            bot.execute(message);

            return ResponseEntity.ok().build();
        } catch (TelegramApiException e) {
            e.printStackTrace();

            return ResponseEntity.badRequest().build();
        }
    }
}