package delivery.hooray.telegramadapter.service;

import delivery.hooray.telegramadapter.model.BotConfig;
import delivery.hooray.telegramadapter.repository.BotConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing bot configs.
 *
 * The service is responsible for management operations on bot configs like
 * fetching, registering, updating or removing bot configs.
 */
@Service
public class BotConfigService {
    @Autowired
    protected BotConfigRepository botConfigRepository;

    @Autowired
    protected EncryptionService encryptionService;

    /**
     * Adds a new bot config.
     *
     * @param token the token of the bot config
     * @return the id of the added bot config
     */
    public UUID addBotConfig(String token) {
        String encryptedToken = encryptionService.encrypt(token);
        BotConfig botConfig = new BotConfig(encryptedToken);

        botConfigRepository.save(botConfig);

        return botConfig.getId();
    }

    /**
     * Updates the token of a bot config.
     *
     * @param id the id of the bot config
     * @param token the new token of the bot config
     */
    public void updateBotConfigToken(UUID id, String token) {
        String encryptedToken = encryptionService.encrypt(token);
        BotConfig botConfig = botConfigRepository.findById(id).orElseThrow();

        botConfig.setToken(encryptedToken);

        botConfigRepository.save(botConfig);
    }

    /**
     * Deletes a bot config.
     *
     * @param id the id of the bot config
     */
    public void deleteBotConfig(UUID id) {
        botConfigRepository.deleteById(id);
    }

    /**
     * Gets all bots.
     *
     * @return list of all bot configs
     */
    public List<BotConfigDto> getAllBotConfigs() {
        List<BotConfig> botConfigs = botConfigRepository.findAll();

        List<BotConfigDto> botConfigDtos = botConfigs.stream()
            .map(botConfig -> new BotConfigDto(botConfig.getId(),
                    encryptionService.decrypt(botConfig.getToken()),
                    botConfig.getUsername()))
            .collect(Collectors.toList());

        return botConfigDtos;
        }
}
