package delivery.hooray.telegramadapter.api.controller;

import delivery.hooray.telegramadapter.api.MessageApi;
import delivery.hooray.telegramadapter.model.SendMessageRequest;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageApiImpl implements MessageApi {
        @Override
        public ResponseEntity<Void> sendMessage(SendMessageRequest sendMessageRequest) {
            throw new NotImplementedException("Not implemented");  // TODO: Implement
        }
}