package delivery.hooray.messagehub.service.admin;

import delivery.hooray.adminadapter.model.SendMessageRequest;
import delivery.hooray.adminadapter.model.SendMessageResponse;

public interface AdminAdapterMessageServiceInterface {
    public SendMessageResponse sendMessageToAdmin(SendMessageRequest message);
}
