package delivery.hooray.messagehub.service;

import delivery.hooray.messagehub.model.ChatModel;
import delivery.hooray.messagehub.model.MessageModel;
import delivery.hooray.messagehub.repository.ChatRepository;
import delivery.hooray.messagehub.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(ChatRepository chatRepository,
                          MessageRepository messageRepository) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
    }

    public void handleCustomerMessage(CustomerMessageDto messageDto) {
        ChatModel chatModel = getChatModel(messageDto);
        MessageModel messageModel = new MessageModel(chatModel, messageDto.getCustomerChatId(), messageDto.getMessage());

        messageRepository.save(messageModel);

        System.out.println("Handling customer message: " + messageDto.getMessage());
    }

    public void handleAdminMessage(AdminMessageDto messageDto) {
        ChatModel chatModel = getChatModel(messageDto);
        MessageModel messageModel = new MessageModel(chatModel, messageDto.getAdminChatId(), messageDto.getMessage());

        messageRepository.save(messageModel);

        System.out.println("Handling admin message: " + messageDto.getMessage());
    }

    protected ChatModel getChatModel(CustomerMessageDto customerMessageDto) {
        ChatModel chatModel = chatRepository.findByCustomerChatId(customerMessageDto.getCustomerChatId());

        if (chatModel != null) {
            return chatModel;
        } else {
            throw new RuntimeException("Chat not found");  // TODO: handle this case
        }
    }

    protected ChatModel getChatModel(AdminMessageDto adminMessageDto) {
        ChatModel chatModel = chatRepository.findByAdminChatId(adminMessageDto.getAdminChatId());

        if (chatModel != null) {
            return chatModel;
        } else {
            throw new RuntimeException("Chat not found");  // TODO: handle this case
        }
    }
}
