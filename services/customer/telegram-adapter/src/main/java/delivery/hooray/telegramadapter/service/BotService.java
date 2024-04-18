package delivery.hooray.telegramadapter.service;

import delivery.hooray.telegramadapter.telegram.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BotService {
    protected final BotConfigService botConfigService;
    protected final List<TelegramBot> runningBots = new ArrayList<>();

    @Autowired
    public BotService(BotConfigService botConfigService) {
        this.botConfigService = botConfigService;
    }

    public void runBots() throws Exception {
        List<BotConfigDto> botConfigs = botConfigService.getAllBotConfigs();
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

        for (BotConfigDto botConfigDto : botConfigs) {
            UUID id = botConfigDto.getId();
            String token = botConfigDto.getToken();
            String username = botConfigDto.getUsername();
            TelegramBot bot = new TelegramBot(id, username, token);

            botsApi.registerBot(bot);
            runningBots.add(bot);
        }
    }
}