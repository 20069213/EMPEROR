package com.emperor.transfer;

import android.app.Activity;
import android.net.Uri;
import android.database.Cursor;
import android.provider.DocumentsContract;

import java.util.ArrayList;

public class FolderManager {

    public static ArrayList<String> getFiles(
            Activity activity,
            Uri folderUri
    ) {

        ArrayList<String> files = new ArrayList<>();

        if (folderUri == null) {
            return files;
        }

        Uri childrenUri =
                DocumentsContract.buildChildDocumentsUriUsingTree(
                        folderUri,
                        DocumentsContract.getTreeDocumentId(folderUri)
                );

        String[] projection = {
                DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                DocumentsContract.Document.COLUMN_MIME_TYPE
        };

        Cursor cursor = null;

        try {
            cursor = activity.getContentResolver().query(
                    childrenUri,
                    projection,
                    null,
                    null,
                    null
            );

            if (cursor != null) {
                while (cursor.moveToNext()) {

                    String name = cursor.getString(0);
                    String mimeType = cursor.getString(1);

                    if (name != null &&
                            !DocumentsContract.Document.MIME_TYPE_DIR
                                    .equals(mimeType)) {

                        files.add(name);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return files;
    }
}
