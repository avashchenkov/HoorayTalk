package delivery.hooray.telegramadapter.service;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {
    private final AES256TextEncryptor encryptor = new AES256TextEncryptor();

    public EncryptionService(@Value("${ENCRYPTION_PASSWORD}") String encryptionPassword) {
        encryptor.setPassword(encryptionPassword);
    }

    public String encrypt(String data) {
        return encryptor.encrypt(data);
    }

    public String decrypt(String encryptedData) {
        return encryptor.decrypt(encryptedData);
    }
}
