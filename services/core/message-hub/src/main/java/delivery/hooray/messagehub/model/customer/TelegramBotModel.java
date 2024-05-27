package delivery.hooray.messagehub.model.customer;

import delivery.hooray.messagehub.model.common.CustomerBotModel;
import jakarta.persistence.*;


@Entity
@Table(name = "telegram_customer_bot")
public class TelegramBotModel extends CustomerBotModel {

    protected TelegramBotModel() {}
}
