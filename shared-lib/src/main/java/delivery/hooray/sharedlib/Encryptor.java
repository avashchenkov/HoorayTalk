package delivery.hooray.sharedlib;

import org.jasypt.util.text.AES256TextEncryptor;

public class Encryptor {
    private final AES256TextEncryptor encryptor = new AES256TextEncryptor();

    public Encryptor(String encryptionPassword) {
        encryptor.setPassword(encryptionPassword);
    }

    public String encrypt(String data) {
        return encryptor.encrypt(data);
    }

    public String decrypt(String encryptedData) {
        return encryptor.decrypt(encryptedData);
    }
}
