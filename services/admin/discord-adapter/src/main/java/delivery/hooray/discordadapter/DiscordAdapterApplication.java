package delivery.hooray.discordadapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"delivery.hooray"})
public class DiscordAdapterApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscordAdapterApplication.class, args);
	}

}
