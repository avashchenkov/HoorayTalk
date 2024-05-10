package delivery.hooray.botadapterspringbootstarter.service;

import delivery.hooray.sharedlib.TextEncrypter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {
    private final TextEncrypter encryptor;

    public EncryptionService(@Value("${ENCRYPTION_PASSWORD}") String encryptionPassword) {
        this.encryptor = new TextEncrypter(encryptionPassword);
    }

    public String encrypt(String data) {
        return encryptor.encrypt(data);
    }

    public String decrypt(String encryptedData) {
        return encryptor.decrypt(encryptedData);
    }
}
