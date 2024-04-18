package delivery.hooray.telegramadapter;

import delivery.hooray.telegramadapter.service.BotConfigDto;
import delivery.hooray.telegramadapter.service.EncryptionService;
import delivery.hooray.telegramadapter.telegram.TelegramBot;
import delivery.hooray.telegramadapter.service.BotManagementService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;
import java.util.UUID;

@Component
public class BotInitializerRunner implements CommandLineRunner {

    protected final BotManagementService botManagementService;

    public BotInitializerRunner(BotManagementService botManagementService) {
        this.botManagementService = botManagementService;
    }

    @Override
    public void run(String... args) throws Exception {
        List<BotConfigDto> bots = botManagementService.getAllBots();

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        for (BotConfigDto botConfigDto : bots) {
            UUID id = botConfigDto.getId();
            String token = botConfigDto.getToken();
            String username = botConfigDto.getUsername();

            TelegramBot bot = new TelegramBot(id, username, token);
            botsApi.registerBot(bot);
        }
    }
}
