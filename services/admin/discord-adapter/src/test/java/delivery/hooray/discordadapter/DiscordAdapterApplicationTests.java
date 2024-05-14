package delivery.hooray.discordadapter;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class DiscordAdapterApplicationTests {

	@Autowired
	protected MockMvc mockMvc;

	@Value("${DISCORD_ADAPTER_API_KEY}")
	private String apiKey;

	@BeforeEach
	public void setup() {
		RestAssuredMockMvc.mockMvc(mockMvc);
		RestAssuredMockMvc.requestSpecification = RestAssuredMockMvc
				.given()
				.header("X-API-KEY", apiKey);
	}
}
