package com.datavault.personal_data_vault.encryption;

import com.datavault.personal_data_vault.encryption.EncryptionService;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EncryptionServiceTest {
    private final String testKey = Base64.getEncoder().encodeToString(new byte[32]);

    EncryptionServiceTest() {
    }

    @Test
    void encryptsAndDecryptsUtf8TextUsingRandomIv() {
        EncryptionService service = new EncryptionService(this.testKey);
        String first = service.encrypt("sensitive value \u03c0");
        String second = service.encrypt("sensitive value \u03c0");
        Assertions.assertNotEquals((Object)"sensitive value \u03c0", (Object)first);
        Assertions.assertNotEquals((Object)first, (Object)second);
        Assertions.assertEquals((Object)"sensitive value \u03c0", (Object)service.decrypt(first));
    }

    @Test
    void rejectsInvalidAesKeyLength() {
        String invalidKey = Base64.getEncoder().encodeToString("too-short".getBytes(StandardCharsets.UTF_8));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new EncryptionService(invalidKey));
    }

    @Test
    void detectsCiphertextTampering() {
        EncryptionService service = new EncryptionService(this.testKey);
        byte[] cipher = Base64.getDecoder().decode(service.encrypt("vault"));
        int n = cipher.length - 1;
        cipher[n] = (byte)(cipher[n] ^ 1);
        String tampered = Base64.getEncoder().encodeToString(cipher);
        Assertions.assertThrows(IllegalStateException.class, () -> service.decrypt(tampered));
    }
}

