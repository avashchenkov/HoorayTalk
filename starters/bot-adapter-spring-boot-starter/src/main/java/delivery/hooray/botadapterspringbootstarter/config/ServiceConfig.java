package delivery.hooray.botadapterspringbootstarter.config;

import delivery.hooray.botadapterspringbootstarter.model.BotModel;
import delivery.hooray.botadapterspringbootstarter.repository.BotModelRepository;
import delivery.hooray.botadapterspringbootstarter.service.BotConfigService;
import delivery.hooray.botadapterspringbootstarter.service.EncryptionService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

@Configuration
public class ServiceConfig {
    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public BotModelRepository botModelRepository() {
        JpaRepositoryFactory factory = new JpaRepositoryFactory(entityManager);
        return factory.getRepository(BotModelRepository.class);
    }

    @Value("${ENCRYPTION_PASSWORD}")
    private String encryptionPassword;

    @Bean
    public EncryptionService encryptionService() {
        return new EncryptionService(encryptionPassword);
    }

    @Bean
    public BotConfigService botConfigService() {
        BotConfigService service = new BotConfigService(encryptionService(), botModelRepository());

        service.setBotModelClass(BotModel.class);

        return service;
    }
}
