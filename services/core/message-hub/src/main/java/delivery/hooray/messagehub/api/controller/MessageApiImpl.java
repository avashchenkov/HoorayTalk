package delivery.hooray.messagehub.api.controller;

import delivery.hooray.messagehub.api.MessageApi;
import delivery.hooray.messagehub.model.HandleAdminMessageRequest;
import delivery.hooray.messagehub.model.HandleCustomerMessageRequest;
import delivery.hooray.messagehub.service.admin.MessageFromAdminAdapterDto;
import delivery.hooray.messagehub.service.customer.MessageFromCustomerAdapterDto;
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
        MessageFromAdminAdapterDto messageFromAdminAdapterDto = new MessageFromAdminAdapterDto(handleAdminMessageRequest.getBotId(),
                                                              handleAdminMessageRequest.getAdminChatId(),
                                                              handleAdminMessageRequest.getMessage());

        messageService.handleAdminMessage(messageFromAdminAdapterDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> handleCustomerMessage(HandleCustomerMessageRequest handleCustomerMessageRequest) {
        MessageFromCustomerAdapterDto messageFromCustomerAdapterDto = new MessageFromCustomerAdapterDto(handleCustomerMessageRequest.getBotId(),
                                                                        handleCustomerMessageRequest.getCustomerChatId(),
                                                                        handleCustomerMessageRequest.getMessage(),
                                                                        handleCustomerMessageRequest.getCustomerDisplayName());

        messageService.handleCustomerMessage(messageFromCustomerAdapterDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
