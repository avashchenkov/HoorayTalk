package delivery.hooray.messagehub.api.controller;

import delivery.hooray.discordadapter.api.MessageApi;
import delivery.hooray.discordadapter.model.HandleAdminMessageRequest;
import delivery.hooray.discordadapter.model.HandleCustomerMessageRequest;
import delivery.hooray.messagehub.service.AdminMessageDto;
import delivery.hooray.messagehub.service.CustomerMessageDto;
import delivery.hooray.messagehub.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageApiImpl implements MessageApi {

    private final MessageService messageService;

    @Autowired
    public MessageApiImpl(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public ResponseEntity<Void> handleAdminMessage(HandleAdminMessageRequest handleAdminMessageRequest) {
        AdminMessageDto adminMessageDto = new AdminMessageDto(handleAdminMessageRequest.getBotId(),
                                                              handleAdminMessageRequest.getAdminChatId(),
                                                              handleAdminMessageRequest.getMessage());

        messageService.handleAdminMessage(adminMessageDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> handleCustomerMessage(HandleCustomerMessageRequest handleCustomerMessageRequest) {
        CustomerMessageDto customerMessageDto = new CustomerMessageDto(handleCustomerMessageRequest.getBotId(),
                                                                        handleCustomerMessageRequest.getCustomerChatId(),
                                                                        handleCustomerMessageRequest.getMessage());

        messageService.handleCustomerMessage(customerMessageDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
