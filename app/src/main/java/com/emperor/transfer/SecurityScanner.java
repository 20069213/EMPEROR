package com.emperor.transfer;

import android.content.Context;
import android.app.KeyguardManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SecurityScanner {

    public static class ScanResult {
        public String title;
        public String status;
        public String details;
        public boolean warning;

        public ScanResult(String title, String status,
                          String details, boolean warning) {
            this.title = title;
            this.status = status;
            this.details = details;
            this.warning = warning;
        }
    }

    public static List<ScanResult> scanDevice(Context context) {

        List<ScanResult> results = new ArrayList<>();

        // 1. Android version
        results.add(new ScanResult(
                "Android Version",
                Build.VERSION.RELEASE,
                "SDK: " + Build.VERSION.SDK_INT,
                false
        ));

        // 2. Device information
        results.add(new ScanResult(
                "Device",
                Build.MANUFACTURER + " " + Build.MODEL,
                "Hardware: " + Build.HARDWARE,
                false
        ));

        // 3. Security patch
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String patch = Build.VERSION.SECURITY_PATCH;

            results.add(new ScanResult(
                    "Security Patch",
                    patch,
                    "Android security patch level",
                    false
            ));
        }

        // 4. Screen lock
        KeyguardManager keyguard =
                (KeyguardManager) context.getSystemService(
                        Context.KEYGUARD_SERVICE
                );

        if (keyguard != null) {
            boolean secure = keyguard.isDeviceSecure();

            results.add(new ScanResult(
                    "Screen Lock",
                    secure ? "PROTECTED" : "NOT PROTECTED",
                    secure
                            ? "A secure screen lock is configured."
                            : "Set a PIN, password, or pattern.",
                    !secure
            ));
        }

        // 5. Developer options
        try {
            int developerOptions = Settings.Global.getInt(
                    context.getContentResolver(),
                    Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
                    0
            );

            results.add(new ScanResult(
                    "Developer Options",
                    developerOptions == 1 ? "ENABLED" : "DISABLED",
                    "Developer settings status",
                    developerOptions == 1
            ));

        } catch (Exception e) {
            results.add(new ScanResult(
                    "Developer Options",
                    "UNKNOWN",
                    "Could not read this setting.",
                    false
            ));
        }

        // 6. USB debugging
        try {
            int adb = Settings.Global.getInt(
                    context.getContentResolver(),
                    Settings.Global.ADB_ENABLED,
                    0
            );

            results.add(new ScanResult(
                    "USB Debugging",
                    adb == 1 ? "ENABLED" : "DISABLED",
                    "ADB debugging status",
                    adb == 1
            ));

        } catch (Exception e) {
            results.add(new ScanResult(
                    "USB Debugging",
                    "UNKNOWN",
                    "Could not read this setting.",
                    false
            ));
        }

        // 7. Storage
        try {
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());

            long total = stat.getTotalBytes();
            long free = stat.getAvailableBytes();
            long used = total - free;

            results.add(new ScanResult(
                    "Internal Storage",
                    formatSize(used) + " used",
                    formatSize(free) + " available of "
                            + formatSize(total),
                    false
            ));

        } catch (Exception e) {
            results.add(new ScanResult(
                    "Internal Storage",
                    "UNKNOWN",
                    "Storage information unavailable.",
                    false
            ));
        }

        // 8. Root indicators (heuristic only)
        boolean rootDetected = checkRootIndicators();

        results.add(new ScanResult(
                "Root Indicators",
                rootDetected ? "POSSIBLE ROOT" : "NOT DETECTED",
                "This is a basic indicator check, not proof of root status.",
                rootDetected
        ));

        return results;
    }

    private static boolean checkRootIndicators() {

        String[] paths = {
                "/system/bin/su",
                "/system/xbin/su",
                "/sbin/su",
                "/system/app/Superuser.apk",
                "/data/adb/magisk"
        };

        for (String path : paths) {
            if (new File(path).exists()) {
                return true;
            }
        }

        return false;
    }

    private static String formatSize(long bytes) {

        double mb = bytes / (1024.0 * 1024.0);

        if (mb >= 1024) {
            return String.format(
                    Locale.US,
                    "%.2f GB",
                    mb / 1024.0
            );
        }

        return String.format(
                Locale.US,
                "%.2f MB",
                mb
        );
    }
}
