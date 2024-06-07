package delivery.hooray.messagehub.service.customer;

import delivery.hooray.customeradapter.model.SendMessageRequest;

public interface CustomerAdapterMessageServiceInterface {
    void sendMessageToCustomer(SendMessageRequest message);
}
