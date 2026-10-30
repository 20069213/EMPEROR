package com.emperor.transfer;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;
import android.view.Gravity;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {

    private static final int PICK_FOLDER = 100;
    private static final int PICK_FILE = 200;

    private TextView statusText;
    private TextView filesText;
    private EditText ipInput;

    private Uri selectedFolder;
    private Uri selectedFile;

    private TcpServer tcpServer;

    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tcpServer = new TcpServer();

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(25, 35, 25, 35);
        layout.setBackgroundColor(Color.rgb(15, 18, 30));

        TextView title = new TextView(this);
        title.setText("EMPEROR");
        title.setTextSize(32);
        title.setTextColor(Color.WHITE);
        title.setGravity(Gravity.CENTER);

        statusText = new TextView(this);
        statusText.setText("مرحباً بك في EMPEROR");
        statusText.setTextColor(Color.WHITE);
        statusText.setTextSize(16);

        ipInput = new EditText(this);
        ipInput.setHint("IP الجهاز المستقبل");
        ipInput.setSingleLine(true);
        ipInput.setTextColor(Color.WHITE);
        ipInput.setHintTextColor(Color.LTGRAY);

        Button chooseFolder = new Button(this);
        chooseFolder.setText("اختيار مجلد الملفات");

        Button refreshButton = new Button(this);
        refreshButton.setText("عرض الملفات");

        Button startButton = new Button(this);
        startButton.setText("تشغيل الاستقبال");

        Button stopButton = new Button(this);
        stopButton.setText("إيقاف الاستقبال");

        Button chooseFile = new Button(this);
        chooseFile.setText("اختيار ملف للإرسال");

        Button sendButton = new Button(this);
        sendButton.setText("إرسال الملف");

        filesText = new TextView(this);
        filesText.setTextColor(Color.WHITE);
        filesText.setTextSize(15);

        chooseFolder.setOnClickListener(v -> openFolderPicker());

        refreshButton.setOnClickListener(v -> showFiles());

        startButton.setOnClickListener(v -> startServer());

        stopButton.setOnClickListener(v -> {
            tcpServer.stop();
        });

        chooseFile.setOnClickListener(v -> openFilePicker());

        sendButton.setOnClickListener(v -> sendFile());

        layout.addView(title);
        layout.addView(statusText);
        layout.addView(chooseFolder);
        layout.addView(refreshButton);
        layout.addView(startButton);
        layout.addView(stopButton);
        layout.addView(ipInput);
        layout.addView(chooseFile);
        layout.addView(sendButton);
        layout.addView(filesText);

        setContentView(layout);

        String saved = getSharedPreferences(
                "EMPEROR", MODE_PRIVATE
        ).getString("folder_uri", null);

        if (saved != null) {
            selectedFolder = Uri.parse(saved);
        }
    }

    private void openFolderPicker() {

        Intent intent = new Intent(
                Intent.ACTION_OPEN_DOCUMENT_TREE
        );

        intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION |
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        );

        startActivityForResult(intent, PICK_FOLDER);
    }

    private void openFilePicker() {

        Intent intent = new Intent(
                Intent.ACTION_OPEN_DOCUMENT
        );

        intent.setType("*/*");

        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
        );

        startActivityForResult(intent, PICK_FILE);
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data
    ) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (resultCode != RESULT_OK || data == null) {
            return;
        }

        Uri uri = data.getData();

        if (uri == null) {
            return;
        }

        if (requestCode == PICK_FOLDER) {

            selectedFolder = uri;

            getContentResolver().takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION |
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            );

            getSharedPreferences("EMPEROR", MODE_PRIVATE)
                    .edit()
                    .putString("folder_uri", uri.toString())
                    .apply();

            statusText.setText("تم اختيار مجلد الملفات");

        } else if (requestCode == PICK_FILE) {

            selectedFile = uri;

            statusText.setText("تم اختيار الملف للإرسال");
        }
    }

    private void showFiles() {

        if (selectedFolder == null) {
            statusText.setText("اختار مجلد الملفات أولاً");
            return;
        }

        ArrayList<String> files =
                FolderManager.getFiles(this, selectedFolder);

        StringBuilder result = new StringBuilder();

        result.append("عدد الملفات: ")
                .append(files.size())
                .append("\n\n");

        for (String name : files) {
            result.append("• ")
                    .append(name)
                    .append("\n");
        }

        filesText.setText(result.toString());
    }

    private void startServer() {

        if (tcpServer.isRunning()) {
            statusText.setText("السيرفر يعمل بالفعل");
            return;
        }

        File receivedFolder = getExternalFilesDir(
                "received_files"
        );

        if (receivedFolder == null) {
            statusText.setText("تعذر تجهيز مجلد الاستقبال");
            return;
        }

        tcpServer.start(
                receivedFolder,
                TcpTransfer.TOKEN,
                message -> runOnUiThread(
                        () -> statusText.setText(message)
                )
        );
    }

    private void sendFile() {

        String host = ipInput.getText().toString().trim();

        if (host.isEmpty()) {
            statusText.setText("اكتب IP الجهاز المستقبل");
            return;
        }

        if (selectedFile == null) {
            statusText.setText("اختار الملف أولاً");
            return;
        }

        statusText.setText("جاري الاتصال والإرسال...");

        executor.execute(() -> {

            try {

                TcpDocumentSender.send(
                        getApplicationContext(),
                        host,
                        selectedFile
                );

                runOnUiThread(() ->
                        statusText.setText("تم إرسال الملف بنجاح")
                );

            } catch (Exception e) {

                runOnUiThread(() ->
                        statusText.setText(
                                "فشل الإرسال: " + e.getMessage()
                        )
                );
            }
        });
    }

    @Override
    protected void onDestroy() {

        if (tcpServer != null) {
            tcpServer.stop();
        }

        executor.shutdownNow();

        super.onDestroy();
    }
                }
