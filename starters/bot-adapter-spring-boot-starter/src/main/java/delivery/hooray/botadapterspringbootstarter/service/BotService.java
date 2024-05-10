package delivery.hooray.botadapterspringbootstarter.service;

import delivery.hooray.botadapterspringbootstarter.bot.Bot;
import delivery.hooray.botadapterspringbootstarter.model.BotModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BotService {
    protected final BotConfigService botConfigService;
    protected final BotFactory botFactory;
    protected final List<Bot> runningBots = new ArrayList<>();

    @Autowired
    public BotService(BotConfigService botConfigService,
                      BotFactory botFactory) {
        this.botConfigService = botConfigService;
        this.botFactory = botFactory;
    }

    public void runBots() {
        List<BotModel> botModels = botConfigService.getAllBotConfigs();
        botModels.forEach(botModel -> {
            Bot bot = botFactory.createBot(botModel);

            bot.run();
            runningBots.add(bot);
        });
    }

    public Bot getBot(UUID botId) {
        return runningBots.stream()
                .filter(bot -> bot.getBotId().equals(botId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Bot not found"));
    }
}