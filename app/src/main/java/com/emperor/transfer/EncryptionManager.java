package com.emperor.transfer;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionManager {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int KEY_SIZE = 256;
    private static final int IV_SIZE = 12;
    private static final int TAG_SIZE = 128;

    // Generate a secure random AES key
    public static String generateKey() throws Exception {

        KeyGenerator generator =
                KeyGenerator.getInstance("AES");

        generator.init(KEY_SIZE);

        SecretKey key = generator.generateKey();

        return Base64.encodeToString(
                key.getEncoded(),
                Base64.NO_WRAP
        );
    }

    // Encrypt text
    public static String encrypt(
            String plainText,
            String base64Key
    ) throws Exception {

        byte[] keyBytes = Base64.decode(
                base64Key,
                Base64.NO_WRAP
        );

        SecretKeySpec key = new SecretKeySpec(
                keyBytes,
                "AES"
        );

        byte[] iv = new byte[IV_SIZE];

        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM);

        cipher.init(
                Cipher.ENCRYPT_MODE,
                key,
                new GCMParameterSpec(TAG_SIZE, iv)
        );

        byte[] encrypted = cipher.doFinal(
                plainText.getBytes(StandardCharsets.UTF_8)
        );

        // Store IV + encrypted data together
        byte[] output = new byte[
                iv.length + encrypted.length
        ];

        System.arraycopy(
                iv, 0,
                output, 0,
                iv.length
        );

        System.arraycopy(
                encrypted, 0,
                output, iv.length,
                encrypted.length
        );

        return Base64.encodeToString(
                output,
                Base64.NO_WRAP
        );
    }

    // Decrypt text
    public static String decrypt(
            String encryptedText,
            String base64Key
    ) throws Exception {

        byte[] keyBytes = Base64.decode(
                base64Key,
                Base64.NO_WRAP
        );

        SecretKeySpec key = new SecretKeySpec(
                keyBytes,
                "AES"
        );

        byte[] input = Base64.decode(
                encryptedText,
                Base64.NO_WRAP
        );

        if (input.length <= IV_SIZE) {
            throw new IllegalArgumentException(
                    "Invalid encrypted data"
            );
        }

        byte[] iv = new byte[IV_SIZE];

        byte[] encrypted = new byte[
                input.length - IV_SIZE
        ];

        System.arraycopy(
                input, 0,
                iv, 0,
                IV_SIZE
        );

        System.arraycopy(
                input, IV_SIZE,
                encrypted, 0,
                encrypted.length
        );

        Cipher cipher = Cipher.getInstance(ALGORITHM);

        cipher.init(
                Cipher.DECRYPT_MODE,
                key,
                new GCMParameterSpec(TAG_SIZE, iv)
        );

        byte[] decrypted = cipher.doFinal(encrypted);

        return new String(
                decrypted,
                StandardCharsets.UTF_8
        );
    }
}
