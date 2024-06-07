package delivery.hooray.messagehub.service.customer;

import delivery.hooray.customeradapter.model.SendMessageRequest;
import delivery.hooray.messagehub.enums.CustomerAdapterType;
import delivery.hooray.messagehub.model.common.CustomerBotModel;
import delivery.hooray.messagehub.model.customer.TelegramBotModel;
import delivery.hooray.messagehub.repository.common.CustomerBotRepository;
import delivery.hooray.messagehub.service.customer.telegram.TelegramAdapterMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static delivery.hooray.messagehub.enums.CustomerAdapterType.TELEGRAM;
import static delivery.hooray.messagehub.enums.CustomerAdapterType.UNKNOWN;

@Service
public class CustomerAdapterMessageService implements CustomerAdapterMessageServiceInterface {
    private final TelegramAdapterMessageService telegramAdapterMessageService;
    private final CustomerBotRepository customerBotRepository;

    @Autowired
    public CustomerAdapterMessageService(TelegramAdapterMessageService telegramAdapterMessageService,
                                         CustomerBotRepository customerBotRepository) {
        this.telegramAdapterMessageService = telegramAdapterMessageService;
        this.customerBotRepository = customerBotRepository;
    }

    @Override
    public void sendMessageToCustomer(SendMessageRequest message) {
        CustomerAdapterType adapterType = determineAdapterType(UUID.fromString(message.getBotId()));

        if (adapterType == TELEGRAM) {
            telegramAdapterMessageService.sendMessageToCustomer(message);
        } else {
            throw new IllegalArgumentException("Unknown adapter type");
        }
    }

    private CustomerAdapterType determineAdapterType(UUID id) {
        CustomerBotModel customerBotModel = customerBotRepository.findById(id).orElse(null);

        if (customerBotModel instanceof TelegramBotModel) {
            return TELEGRAM;
        } else {
            return UNKNOWN;
        }
    }
}
