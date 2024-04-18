package delivery.hooray.telegramadapter.service;

import delivery.hooray.telegramadapter.model.BotConfig;
import delivery.hooray.telegramadapter.repository.BotRepository;
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
public class BotManagementService {
    @Autowired
    protected BotRepository botRepository;

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

        botRepository.save(botConfig);

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
        BotConfig botConfig = botRepository.findById(id).orElseThrow();

        botConfig.setToken(encryptedToken);

        botRepository.save(botConfig);
    }

    /**
     * Deletes a bot.
     *
     * @param id the id of the bot
     */
    public void deleteBot(UUID id) {
        botRepository.deleteById(id);
    }

    /**
     * Gets all bots.
     *
     * @return list of all bots
     */
    public List<BotConfigDto> getAllBots() {
        List<BotConfig> botConfigs = botRepository.findAll();

        List<BotConfigDto> botConfigDtos = botConfigs.stream()
            .map(botConfig -> new BotConfigDto(botConfig.getId(),
                    encryptionService.decrypt(botConfig.getToken()),
                    botConfig.getUsername()))
            .collect(Collectors.toList());

        return botConfigDtos;
        }
}
