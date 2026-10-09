package com.datavault.personal_data_vault.encryption;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int IV_BYTES = 12;
    private static final int TAG_BITS = 128;
    private final SecretKey secretKey;
    private final SecureRandom secureRandom = new SecureRandom();

    public EncryptionService(@Value(value="${app.encryption.key}") String base64Key) {
        byte[] keyBytes;
        try {
            keyBytes = Base64.getDecoder().decode(base64Key);
        }
        catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("ENCRYPTION_KEY must be base64-encoded AES-256 key material", ex);
        }
        if (keyBytes.length != 32) {
            throw new IllegalArgumentException("ENCRYPTION_KEY must decode to exactly 32 bytes");
        }
        this.secretKey = new SecretKeySpec(keyBytes, "AES");
    }

    public String encrypt(String plaintext) {
        if (plaintext == null) {
            throw new IllegalArgumentException("Plaintext must not be null");
        }
        try {
            byte[] iv = new byte[12];
            this.secureRandom.nextBytes(iv);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(1, (Key)this.secretKey, new GCMParameterSpec(128, iv));
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            ByteBuffer payload = ByteBuffer.allocate(12 + ciphertext.length);
            payload.put(iv).put(ciphertext);
            return Base64.getEncoder().encodeToString(payload.array());
        }
        catch (GeneralSecurityException ex) {
            throw new IllegalStateException("Unable to encrypt personal data", ex);
        }
    }

    public String decrypt(String encryptedValue) {
        try {
            byte[] payload = Base64.getDecoder().decode(encryptedValue);
            if (payload.length < 28) {
                throw new IllegalArgumentException("Invalid encrypted data format");
            }
            byte[] iv = new byte[12];
            byte[] ciphertext = new byte[payload.length - 12];
            System.arraycopy(payload, 0, iv, 0, 12);
            System.arraycopy(payload, 12, ciphertext, 0, ciphertext.length);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(2, (Key)this.secretKey, new GCMParameterSpec(128, iv));
            return new String(cipher.doFinal(ciphertext), StandardCharsets.UTF_8);
        }
        catch (IllegalArgumentException | GeneralSecurityException ex) {
            throw new IllegalStateException("Unable to decrypt personal data", ex);
        }
    }
}

