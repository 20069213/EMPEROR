package com.emperor.transfer;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;
import android.view.Gravity;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final int PICK_FOLDER = 100;

    private TextView statusText;
    private TextView filesText;

    private Uri selectedFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        statusText.setText("اختر مجلد الملفات للبدء");
        statusText.setTextColor(Color.WHITE);
        statusText.setTextSize(16);

        Button chooseButton = new Button(this);
        chooseButton.setText("اختيار مجلد");

        Button refreshButton = new Button(this);
        refreshButton.setText("عرض الملفات");

        filesText = new TextView(this);
        filesText.setTextColor(Color.WHITE);
        filesText.setTextSize(15);
        filesText.setPadding(5, 20, 5, 20);

        chooseButton.setOnClickListener(v -> openFolderPicker());

        refreshButton.setOnClickListener(v -> showFiles());

        layout.addView(title);
        layout.addView(statusText);
        layout.addView(chooseButton);
        layout.addView(refreshButton);
        layout.addView(filesText);

        setContentView(layout);

        String saved = getSharedPreferences(
                "EMPEROR", MODE_PRIVATE
        ).getString("folder_uri", null);

        if (saved != null) {
            selectedFolder = Uri.parse(saved);
            statusText.setText("يوجد مجلد محفوظ");
        }
    }

    private void openFolderPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);

        intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION |
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        );

        startActivityForResult(intent, PICK_FOLDER);
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FOLDER &&
                resultCode == RESULT_OK &&
                data != null) {

            selectedFolder = data.getData();

            if (selectedFolder != null) {

                getContentResolver().takePersistableUriPermission(
                        selectedFolder,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION |
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                );

                getSharedPreferences("EMPEROR", MODE_PRIVATE)
                        .edit()
                        .putString("folder_uri", selectedFolder.toString())
                        .apply();

                statusText.setText("تم اختيار المجلد بنجاح");

                showFiles();
            }
        }
    }

    private void showFiles() {

        if (selectedFolder == null) {
            statusText.setText("اختار مجلد الأول");
            return;
        }

        ArrayList<String> files =
                FolderManager.getFiles(this, selectedFolder);

        StringBuilder result = new StringBuilder();

        result.append("عدد الملفات: ")
                .append(files.size())
                .append("\n\n");

        for (String file : files) {
            result.append("• ")
                    .append(file)
                    .append("\n");
        }

        filesText.setText(result.toString());
    }
}
