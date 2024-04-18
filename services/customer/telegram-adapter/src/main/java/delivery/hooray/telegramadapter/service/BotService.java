package delivery.hooray.telegramadapter.service;

import delivery.hooray.telegramadapter.config.TelegramBotFactory;
import delivery.hooray.telegramadapter.telegram.TelegramBot;
import delivery.hooray.telegramadapter.telegram.TelegramUpdateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;

@Service
public class BotService {
    private final BotConfigService botConfigService;
    private final TelegramBotFactory botFactory;
    private final TelegramUpdateProcessor updateProcessor;
    private final MessageHubSenderService messageHubSenderService;

    protected final List<TelegramBot> runningBots = new ArrayList<>();

    @Autowired
    public BotService(BotConfigService botConfigService,
                      TelegramBotFactory botFactory,
                      TelegramUpdateProcessor updateProcessor,
                      MessageHubSenderService messageHubSenderService) {
        this.botConfigService = botConfigService;
        this.botFactory = botFactory;
        this.updateProcessor = updateProcessor;
        this.messageHubSenderService = messageHubSenderService;
    }

    public void runBots() throws Exception {
        List<BotConfigDto> botConfigs = botConfigService.getAllBotConfigs();
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

        for (BotConfigDto botConfigDto : botConfigs) {
            TelegramBot bot = botFactory.createBot(botConfigDto.getId(),
                    botConfigDto.getUsername(),
                    botConfigDto.getToken());

            botsApi.registerBot(bot);
            runningBots.add(bot);
        }
    }
}
