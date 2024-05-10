package delivery.hooray.botadapterspringbootstarter;

import delivery.hooray.botadapterspringbootstarter.service.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BotInitializerRunner implements CommandLineRunner {
    protected final BotService botService;

    @Autowired
    public BotInitializerRunner(BotService botService) {
        this.botService = botService;
    }

    @Override
    public void run(String... args) throws Exception {
        botService.runBots();
    }
}
