package delivery.hooray.messagehub.service.admin.discord;

import delivery.hooray.adminadapter.model.SendMessageRequest;
import delivery.hooray.adminadapter.model.SendMessageResponse;
import delivery.hooray.messagehub.service.admin.AdminAdapterMessageServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscordAdapterMessageService implements AdminAdapterMessageServiceInterface {
    private final DiscordSenderService discordSenderService;

    @Autowired
    public DiscordAdapterMessageService(DiscordSenderService discordSenderService) {
        this.discordSenderService = discordSenderService;
    }

    @Override
    public SendMessageResponse sendMessageToAdmin(SendMessageRequest message) {
        return this.discordSenderService.sendMessage(message).block();
    }
}
