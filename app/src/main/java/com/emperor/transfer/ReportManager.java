package com.emperor.transfer;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportManager {

    // Save a report inside the app's private storage
    public static File saveReport(
            Context context,
            String title,
            String content
    ) throws Exception {

        File directory = new File(
                context.getFilesDir(),
                "reports"
        );

        if (!directory.exists() && !directory.mkdirs()) {
            throw new Exception("Could not create reports folder");
        }

        String timestamp = new SimpleDateFormat(
                "yyyy-MM-dd_HH-mm-ss",
                Locale.US
        ).format(new Date());

        String fileName = "EMPEROR_" + timestamp + ".txt";

        File reportFile = new File(directory, fileName);

        String reportContent =
                "================================\n" +
                "       EMPEROR CYBER SECURITY\n" +
                "================================\n\n" +
                "Report: " + title + "\n" +
                "Date: " + new Date().toString() + "\n\n" +
                "--------------------------------\n" +
                content +
                "\n--------------------------------\n" +
                "Generated locally by EMPEROR.\n";

        try (FileOutputStream output =
                     new FileOutputStream(reportFile)) {

            output.write(
                    reportContent.getBytes(StandardCharsets.UTF_8)
            );

            output.flush();
        }

        return reportFile;
    }

    // Get the reports folder
    public static File getReportsDirectory(Context context) {

        return new File(
                context.getFilesDir(),
                "reports"
        );
    }

    // Count saved reports
    public static int getReportCount(Context context) {

        File directory = getReportsDirectory(context);

        File[] files = directory.listFiles();

        return files == null ? 0 : files.length;
    }
}
