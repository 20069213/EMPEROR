package com.emperor.transfer;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Gravity;
import android.graphics.Color;
import android.graphics.Typeface;

public class MainActivity extends Activity {

    private static final int PICK_FOLDER = 100;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(30, 40, 30, 40);
        layout.setBackgroundColor(Color.rgb(15, 18, 30));

        TextView title = new TextView(this);
        title.setText("EMPEROR");
        title.setTextSize(34);
        title.setTextColor(Color.WHITE);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);

        TextView subtitle = new TextView(this);
        subtitle.setText("نظام نقل الملفات");
        subtitle.setTextSize(18);
        subtitle.setTextColor(Color.LTGRAY);
        subtitle.setGravity(Gravity.CENTER);

        Button chooseFolder = new Button(this);
        chooseFolder.setText("اختيار مجلد الملفات");

        statusText = new TextView(this);
        statusText.setText("لم يتم اختيار مجلد بعد");
        statusText.setTextSize(15);
        statusText.setTextColor(Color.WHITE);
        statusText.setGravity(Gravity.CENTER);

        Button infoButton = new Button(this);
        infoButton.setText("حالة التطبيق");

        infoButton.setOnClickListener(v ->
            statusText.setText("EMPEROR جاهز للإعداد")
        );

        chooseFolder.setOnClickListener(v -> openFolderPicker());

        layout.addView(title);
        layout.addView(subtitle);

        addSpace(layout, 35);

        layout.addView(chooseFolder);
        layout.addView(statusText);
        layout.addView(infoButton);

        setContentView(layout);
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

        if (requestCode == PICK_FOLDER && resultCode == RESULT_OK
                && data != null) {

            Uri folderUri = data.getData();

            if (folderUri != null) {
                getContentResolver().takePersistableUriPermission(
                    folderUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION |
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                );

                getSharedPreferences("EMPEROR", MODE_PRIVATE)
                    .edit()
                    .putString("folder_uri", folderUri.toString())
                    .apply();

                statusText.setText("تم اختيار المجلد بنجاح");
            }
        }
    }

    private void addSpace(LinearLayout layout, int height) {
        TextView space = new TextView(this);
        space.setHeight(height);
        layout.addView(space);
    }
}
