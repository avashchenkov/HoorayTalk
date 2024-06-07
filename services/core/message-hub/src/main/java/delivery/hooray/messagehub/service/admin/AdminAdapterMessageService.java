package delivery.hooray.messagehub.service.admin;

import delivery.hooray.adminadapter.model.SendMessageRequest;
import delivery.hooray.adminadapter.model.SendMessageResponse;
import delivery.hooray.messagehub.enums.AdminAdapterType;
import delivery.hooray.messagehub.model.admin.DiscordServerBotModel;
import delivery.hooray.messagehub.model.common.AdminBotModel;
import delivery.hooray.messagehub.repository.common.AdminBotRepository;
import delivery.hooray.messagehub.service.admin.discord.DiscordAdapterMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static delivery.hooray.messagehub.enums.AdminAdapterType.DISCORD;
import static delivery.hooray.messagehub.enums.AdminAdapterType.UNKNOWN;

@Service
public class AdminAdapterMessageService implements AdminAdapterMessageServiceInterface {
    private final AdminBotRepository adminBotRepository;
    private final DiscordAdapterMessageService discordAdapterMessageService;

    @Autowired
    public AdminAdapterMessageService(AdminBotRepository adminBotRepository,
                                      DiscordAdapterMessageService discordAdapterMessageService) {
        this.adminBotRepository = adminBotRepository;
        this.discordAdapterMessageService = discordAdapterMessageService;
    }

    @Override
    public SendMessageResponse sendMessageToAdmin(SendMessageRequest message) {
        AdminAdapterType adapterType = determineAdapterType(UUID.fromString(message.getBotId()));

        if (adapterType == DISCORD) {
            return discordAdapterMessageService.sendMessageToAdmin(message);
        } else {
            throw new IllegalArgumentException("Unknown adapter type");
        }
    }

    private AdminAdapterType determineAdapterType(UUID id) {
        AdminBotModel adminBotModel = adminBotRepository.findById(id).orElse(null);

        if (adminBotModel instanceof DiscordServerBotModel) {
            return DISCORD;
        } else {
            return UNKNOWN;
        }
    }
}
