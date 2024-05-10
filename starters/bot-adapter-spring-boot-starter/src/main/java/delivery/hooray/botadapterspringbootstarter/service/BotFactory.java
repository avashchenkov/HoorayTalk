package delivery.hooray.botadapterspringbootstarter.service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import delivery.hooray.botadapterspringbootstarter.bot.Bot;
import delivery.hooray.botadapterspringbootstarter.model.BotModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BotFactory {
    private final MessageHubSenderService messageHubSenderService;
    private final BotBehavior botBehavior;
    private final BotMappingService botMappingService;
    private final EncryptionService encryptionService;

    @Autowired
    public BotFactory(MessageHubSenderService messageHubSenderService,
                      BotBehavior botBehavior,
                      BotMappingService botDataMappingService,
                      EncryptionService encryptionService) {
        this.messageHubSenderService = messageHubSenderService;
        this.botBehavior = botBehavior;
        this.botMappingService = botDataMappingService;
        this.encryptionService = encryptionService;
    }

    public Bot createBot(BotModel botModel) {
        Bot bot = null;

        try {
            Class<? extends Bot> botClass = botBehavior.getBotClass();
            Constructor<? extends Bot> constructor = botClass.getConstructor(MessageHubSenderService.class,
                                                                             BotBehavior.class,
                                                                             EncryptionService.class);
            bot = constructor.newInstance(messageHubSenderService, botBehavior, encryptionService);
            botMappingService.mapConfigToBot(botModel, bot);  // TODO: botModel is not a Bean!
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to create bot instance", e);
        }


        return bot;  // TODO: consider passing TelegramBot & TelegramBotModel here as a parameters via other beans?
    }  // TODO: Or we can Parametrize this factory with generic types and pass the Bot & BotModel classes as parameters?
}
