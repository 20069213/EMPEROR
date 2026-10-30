package com.emperor.transfer;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TransferProtocol {

    public static final int PORT = 5001;
    public static final int BUFFER_SIZE = 4096;

    public static final String MESSAGE_READY = "READY";
    public static final String MESSAGE_DONE = "DONE";
    public static final String MESSAGE_END = "END";

    public static boolean isValidFileName(String name) {

        if (name == null || name.isEmpty()) {
            return false;
        }

        if (name.contains("/") || name.contains("\\")) {
            return false;
        }

        if (name.equals(".") || name.equals("..")) {
            return false;
        }

        return true;
    }

    public static String calculateSHA256(File file)
            throws Exception {

        MessageDigest digest;

        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("SHA-256 غير متاح");
        }

        java.io.FileInputStream input =
                new java.io.FileInputStream(file);

        byte[] buffer = new byte[BUFFER_SIZE];

        int bytesRead;

        try {
            while ((bytesRead = input.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        } finally {
            input.close();
        }

        byte[] hash = digest.digest();

        StringBuilder result = new StringBuilder();

        for (byte b : hash) {
            result.append(String.format("%02x", b));
        }

        return result.toString();
    }

    public static boolean isValidFileSize(long size) {
        return size >= 0 && size <= 1024L * 1024L * 1024L;
    }
}
