package delivery.hooray.telegramadapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"delivery.hooray"})
public class TelegramAdapterApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelegramAdapterApplication.class, args);
	}
}
