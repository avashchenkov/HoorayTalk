package delivery.hooray.messagehub.service.customer.telegram;

import com.fasterxml.jackson.databind.ObjectMapper;
import delivery.hooray.messagehub.enums.TelegramCommand;
import delivery.hooray.messagehub.model.common.AiAssistantInstructionModel;
import delivery.hooray.messagehub.model.common.ChatModel;
import delivery.hooray.messagehub.model.common.TenantModel;
import delivery.hooray.messagehub.repository.common.ChatRepository;
import delivery.hooray.messagehub.repository.common.TenantRepository;
import delivery.hooray.messagehub.service.customer.MessageFromCustomerAdapterDto;
import delivery.hooray.sharedlib.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TelegramCommandService {
    private final ChatRepository chatRepository;
    private final TenantRepository tenantRepository;

    @Autowired
    public TelegramCommandService(ChatRepository chatRepository, TenantRepository tenantRepository) {
        this.chatRepository = chatRepository;
        this.tenantRepository = tenantRepository;
    }

    public void handleBotCommand(MessageFromCustomerAdapterDto messageDto) {
        Message message;

        ObjectMapper mapper = new ObjectMapper();
        try {
            message = mapper.readValue(messageDto.getMessage(), Message.class);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Optional<TelegramCommand> command = TelegramCommand.fromString(message.getText());

        if (command.isPresent()) {
            if (command.get() == TelegramCommand.START) {
                handleStartCommand(messageDto);
            } else if (command.get() == TelegramCommand.NEWORDER) {
                handleNewOrderCommand(messageDto);
            }
        }
    }

    private void handleStartCommand(MessageFromCustomerAdapterDto messageDto) {
        ChatModel chatModel = getChatModel(messageDto);
        TenantModel tenantModel = tenantRepository.findByCustomerBot_Id(messageDto.getBotId());

        if (chatModel != null && tenantModel != null) {
            resetAiAssistantInstruction(chatModel, tenantModel.getAiAssistantStartInstruction());
        } else {
            throw new RuntimeException("Chat or tenant not found");
        }
    }

    private void handleNewOrderCommand(MessageFromCustomerAdapterDto messageDto) {
        ChatModel chatModel = getChatModel(messageDto);
        TenantModel tenantModel = tenantRepository.findByCustomerBot_Id(messageDto.getBotId());

        if (chatModel != null && tenantModel != null) {
            resetAiAssistantInstruction(chatModel, tenantModel.getAiAssistantStartInstruction());
        } else {
            throw new RuntimeException("Chat or tenant not found");
        }
    }

    private ChatModel getChatModel(MessageFromCustomerAdapterDto messageDto) {
        String customerChatId = messageDto.getCustomerChatId();
        TenantModel tenantModel = tenantRepository.findByCustomerBot_Id(messageDto.getBotId());

        return chatRepository.findByCustomerChatIdAndTenant(customerChatId, tenantModel);
    }

    private void resetAiAssistantInstruction(ChatModel chatModel, AiAssistantInstructionModel instruction) {
        chatModel.setAiAssistantInstruction(instruction);

        try {
            chatRepository.save(chatModel);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error saving chat model", e);
        }
    }
}
