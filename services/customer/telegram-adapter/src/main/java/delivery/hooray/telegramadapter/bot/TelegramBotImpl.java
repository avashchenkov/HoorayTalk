package delivery.hooray.telegramadapter.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import delivery.hooray.botadapterspringbootstarter.service.EncryptionService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import delivery.hooray.sharedlib.Message;

public class TelegramBotImpl extends TelegramLongPollingBot {
    private final TelegramBot telegramBot;
    private final EncryptionService encryptionService;

    public TelegramBotImpl(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
        this.encryptionService = telegramBot.getEncryptionService();
    }


    /**
     * @param update
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            String botId = this.getBotId().toString();
            String chatId = update.getMessage().getChatId().toString();
            TelegramMessageToMessageHubRequestData requestData;
            Message message = new Message();

            User user = update.getMessage().getFrom();
            String username = user.getUserName();

            if (update.getMessage().hasPhoto()) {
                if (update.getMessage().getCaption() != null) {
                    message.setText(update.getMessage().getCaption());
                }

                List<PhotoSize> photos = update.getMessage().getPhoto();

                PhotoSize lastPhoto = photos.getLast();

                try {
                    GetFile getFileRequest = new GetFile();
                    getFileRequest.setFileId(lastPhoto.getFileId());

                    File file = execute(getFileRequest);

                    if (file.getFilePath() != null) {
                        String fileUrl = "https://api.telegram.org/file/bot" + getBotToken() + "/" + file.getFilePath();
                        String encryptedFileUrl = encryptionService.encrypt(fileUrl);
                        message.setMediaUrl(encryptedFileUrl);
                    } else {
                        System.out.println("Failed to get file path for the photo");
                    }
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                    System.out.println("Error occurred: " + e.getMessage());
                }
            } else if (update.getMessage().hasText()) {
                message.setText(update.getMessage().getText());
            }

            String messageText;

            ObjectMapper mapper = new ObjectMapper();
            try {
                messageText =  mapper.writeValueAsString(message);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            requestData = new TelegramMessageToMessageHubRequestData(botId, messageText, chatId);

            if (username != null) {
                requestData.setCustomerDisplayName(username);
            } else {
                requestData.setCustomerDisplayName(chatId);
            }

            telegramBot.sendMsgToMessageHub(requestData);
        }
    }


    /**
     * @return
     */
    public void sendMsgToCustomer(MessageToTelegramBotEndUserRequestData data) {
        System.out.println(data.getMessage());  // TODO: it is not used anywhere
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

    private void saveFileFromTelegram(File telegramFile, String localPath) {
        try {
            String fileUrl = "https://api.telegram.org/file/bot" + getBotToken() + "/" + telegramFile.getFilePath();
            InputStream in = new URL(fileUrl).openStream();
            java.nio.file.Files.copy(in, Paths.get(localPath), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
