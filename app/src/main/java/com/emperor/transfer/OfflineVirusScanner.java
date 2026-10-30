package com.emperor.transfer;

import android.content.Context;
import android.net.Uri;

import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class OfflineVirusScanner {

    public enum Status {
        KNOWN_THREAT,
        SUSPICIOUS,
        NO_INDICATORS,
        ERROR
    }

    public static class ScanResult {
        public final String fileName;
        public final String sha256;
        public final Status status;
        public final String details;

        public ScanResult(
                String fileName,
                String sha256,
                Status status,
                String details
        ) {
            this.fileName = fileName;
            this.sha256 = sha256;
            this.status = status;
            this.details = details;
        }
    }

    /*
     * قاعدة التوقيعات المحلية.
     * أضف بصمات SHA-256 موثوقة لملفات ضارة معروفة.
     * لا توجد توقيعات جاهزة هنا؛ القائمة فارغة افتراضيًا.
     */
    private static final Set<String> MALWARE_HASHES =
            new HashSet<>();

    public static void addKnownThreatHash(String hash) {
        if (hash == null) return;

        String value = hash.trim().toLowerCase(Locale.US);

        if (value.matches("[a-f0-9]{64}")) {
            MALWARE_HASHES.add(value);
        }
    }

    public static ScanResult scanFile(
            Context context,
            Uri fileUri
    ) {
        String fileName = "Unknown file";

        try {
            String name = context.getContentResolver()
                    .query(fileUri, null, null, null, null) != null
                    ? getFileName(context, fileUri)
                    : getFileName(context, fileUri);

            if (name != null) {
                fileName = name;
            }

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] buffer = new byte[8192];

            long totalBytes = 0;
            int read;

            try (InputStream input =
                         context.getContentResolver()
                                 .openInputStream(fileUri)) {

                if (input == null) {
                    return new ScanResult(
                            fileName,
                            "",
                            Status.ERROR,
                            "تعذر فتح الملف."
                    );
                }

                while ((read = input.read(buffer)) != -1) {
                    digest.update(buffer, 0, read);
                    totalBytes += read;

                    // حماية من الملفات الضخمة جدًا.
                    if (totalBytes > 100L * 1024L * 1024L) {
                        return new ScanResult(
                                fileName,
                                "",
                                Status.ERROR,
                                "الملف أكبر من الحد المسموح للفحص (100 MB)."
                        );
                    }
                }
            }

            String hash = toHex(digest.digest());

            synchronized (MALWARE_HASHES) {
                if (MALWARE_HASHES.contains(hash)) {
                    return new ScanResult(
                            fileName,
                            hash,
                            Status.KNOWN_THREAT,
                            "تطابق SHA-256 مع توقيع ضار موجود في قاعدة البيانات المحلية."
                    );
                }
            }

            String lowerName =
                    fileName.toLowerCase(Locale.US);

            if (lowerName.endsWith(".apk")
                    || lowerName.endsWith(".exe")
                    || lowerName.endsWith(".bat")
                    || lowerName.endsWith(".scr")) {

                return new ScanResult(
                        fileName,
                        hash,
                        Status.SUSPICIOUS,
                        "نوع الملف قابل لتنفيذ تعليمات. هذا ليس دليلًا على وجود فيروس."
                );
            }

            return new ScanResult(
                    fileName,
                    hash,
                    Status.NO_INDICATORS,
                    "لم يتم العثور على تطابق مع التوقيعات المحلية أو مؤشر الاسم المحدود."
            );

        } catch (Exception e) {
            return new ScanResult(
                    fileName,
                    "",
                    Status.ERROR,
                    "حدث خطأ أثناء الفحص: " + e.getMessage()
            );
        }
    }

    private static String getFileName(
            Context context,
            Uri uri
    ) {
        android.database.Cursor cursor = null;

        try {
            cursor = context.getContentResolver()
                    .query(uri, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(
                        android.provider.OpenableColumns.DISPLAY_NAME
                );

                if (index >= 0) {
                    return cursor.getString(index);
                }
            }
        } catch (Exception ignored) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return uri.getLastPathSegment();
    }

    private static String toHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();

        for (byte b : bytes) {
            result.append(
                    String.format(Locale.US, "%02x", b & 0xff)
            );
        }

        return result.toString();
    }
        }
