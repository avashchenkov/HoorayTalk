package delivery.hooray.telegramadapter.service;

import delivery.hooray.telegramadapter.model.BotConfig;
import delivery.hooray.telegramadapter.repository.BotConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing bots.
 *
 * The service is responsible for management operations on bots like
 * registering, updating, deleting and launching bots.
 */
@Service
public class BotConfigService {
    @Autowired
    protected BotConfigRepository botConfigRepository;

    @Autowired
    protected EncryptionService encryptionService;

    /**
     * Adds a new bot.
     *
     * @param token the token of the bot
     * @return the id of the added bot
     */
    public UUID addBot(String token) {
        String encryptedToken = encryptionService.encrypt(token);
        BotConfig botConfig = new BotConfig(encryptedToken);

        botConfigRepository.save(botConfig);

        return botConfig.getId();
    }

    /**
     * Updates the token of a bot.
     *
     * @param id the id of the bot
     * @param token the new token of the bot
     */
    public void updateBotToken(UUID id, String token) {
        String encryptedToken = encryptionService.encrypt(token);
        BotConfig botConfig = botConfigRepository.findById(id).orElseThrow();

        botConfig.setToken(encryptedToken);

        botConfigRepository.save(botConfig);
    }

    /**
     * Deletes a bot.
     *
     * @param id the id of the bot
     */
    public void deleteBot(UUID id) {
        botConfigRepository.deleteById(id);
    }

    /**
     * Gets all bots.
     *
     * @return list of all bots
     */
    public List<BotConfigDto> getAllBots() {
        List<BotConfig> botConfigs = botConfigRepository.findAll();

        List<BotConfigDto> botConfigDtos = botConfigs.stream()
            .map(botConfig -> new BotConfigDto(botConfig.getId(),
                    encryptionService.decrypt(botConfig.getToken()),
                    botConfig.getUsername()))
            .collect(Collectors.toList());

        return botConfigDtos;
        }
}
