package delivery.hooray.botadapterspringbootstarter.service;

import delivery.hooray.botadapterspringbootstarter.bot.Bot;
import delivery.hooray.botadapterspringbootstarter.model.BotModel;
import delivery.hooray.botadapterspringbootstarter.repository.BotModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for managing bot configs.
 *
 * The service is responsible for management operations on bot configs like
 * fetching, registering, updating or removing bot configs.
 */
@Service
public class BotConfigService {
    protected final BotModelRepository botModelRepository;
    protected final EncryptionService encryptionService;
    protected Class<BotModel> botModelClass;

    @Autowired
    public BotConfigService(EncryptionService encryptionService,
                            BotModelRepository botModelRepository) {
        this.encryptionService = encryptionService;
        this.botModelRepository = botModelRepository;
    }

    /**
     * Gets all bots.
     *
     * @return list of all bot configs
     */
    public List<BotModel> getAllBotConfigs() {
        List<BotModel> botModels = botModelRepository.findAll();

        return botModels;
    }

    public void setBotModelClass(Class<BotModel> botModelClass) {
        this.botModelClass = botModelClass;  // TODO: use it to instantiate a real model
    }
}
