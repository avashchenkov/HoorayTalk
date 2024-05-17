package delivery.hooray.messagehub.model;

import jakarta.persistence.*;

import java.util.UUID;


@Entity
@Table(name = "telegram_customer_bot")
public class TelegramCustomerBotModel extends CustomerBotModel {

    protected TelegramCustomerBotModel() {}
}
