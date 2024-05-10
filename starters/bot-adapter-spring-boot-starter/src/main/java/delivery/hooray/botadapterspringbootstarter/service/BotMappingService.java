package delivery.hooray.botadapterspringbootstarter.service;

import delivery.hooray.botadapterspringbootstarter.bot.Bot;
import delivery.hooray.botadapterspringbootstarter.model.BotModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class BotMappingService {
    private final ModelMapper modelMapper;

    public BotMappingService() {
        this.modelMapper = new ModelMapper();
    }

    public void mapConfigToBot(BotModel botModel, Bot bot) {
        modelMapper.map(botModel, bot);
    }
}
