package com.emperor.transfer;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class HashManager {

    // Calculate SHA-256 for text
    public static String hashText(String text) throws Exception {

        MessageDigest digest =
                MessageDigest.getInstance("SHA-256");

        byte[] result = digest.digest(
                text.getBytes("UTF-8")
        );

        return bytesToHex(result);
    }

    // Calculate SHA-256 for files
    public static String hashFile(File file) throws Exception {

        MessageDigest digest =
                MessageDigest.getInstance("SHA-256");

        try (FileInputStream input =
                     new FileInputStream(file)) {

            byte[] buffer = new byte[8192];

            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }

        return bytesToHex(digest.digest());
    }

    // Convert bytes to hexadecimal
    private static String bytesToHex(byte[] bytes) {

        StringBuilder result = new StringBuilder();

        for (byte b : bytes) {

            result.append(
                    String.format("%02x", b & 0xff)
            );
        }

        return result.toString();
    }
}
