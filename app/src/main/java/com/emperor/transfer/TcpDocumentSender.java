package com.emperor.transfer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TcpDocumentSender {

    private static void writeLine(
            OutputStream out, String value
    ) throws IOException {

        out.write((value + "\n").getBytes(
                StandardCharsets.UTF_8
        ));
        out.flush();
    }

    private static String readLine(
            InputStream in
    ) throws IOException {

        StringBuilder result = new StringBuilder();
        int value;

        while ((value = in.read()) != -1) {

            if (value == '\n') {
                return result.toString().trim();
            }

            if (result.length() >= 8192) {
                throw new IOException("Message too large");
            }

            result.append((char) value);
        }

        throw new IOException("Connection closed");
    }

    public static void send(
            Context context,
            String host,
            Uri fileUri
    ) throws Exception {

        ContentResolver resolver =
                context.getContentResolver();

        String fileName = null;
        long fileSize = -1;

        try (Cursor cursor = resolver.query(
                fileUri,
                null,
                null,
                null,
                null
        )) {

            if (cursor != null && cursor.moveToFirst()) {

                int nameIndex = cursor.getColumnIndex(
                        OpenableColumns.DISPLAY_NAME
                );

                int sizeIndex = cursor.getColumnIndex(
                        OpenableColumns.SIZE
                );

                if (nameIndex >= 0) {
                    fileName = cursor.getString(nameIndex);
                }

                if (sizeIndex >= 0 && !cursor.isNull(sizeIndex)) {
                    fileSize = cursor.getLong(sizeIndex);
                }
            }
        }

        if (!TransferProtocol.isValidFileName(fileName)) {
            throw new IOException("Invalid filename");
        }

        if (!TransferProtocol.isValidFileSize(fileSize)) {
            throw new IOException("Unknown or invalid file size");
        }

        try (Socket socket = new Socket()) {

            socket.connect(
                    new InetSocketAddress(host, 5001),
                    10000
            );

            socket.setSoTimeout(15000);

            InputStream networkIn = socket.getInputStream();
            OutputStream networkOut = socket.getOutputStream();

            writeLine(networkOut, TcpTransfer.TOKEN);

            if (!"OK".equals(readLine(networkIn))) {
                throw new IOException("Authentication failed");
            }

            JSONObject metadata = new JSONObject();

            metadata.put("name", fileName);
            metadata.put("size", fileSize);

            writeLine(networkOut, metadata.toString());

            if (!"READY".equals(readLine(networkIn))) {
                throw new IOException("Receiver not ready");
            }

            try (InputStream fileIn =
                         resolver.openInputStream(fileUri)) {

                if (fileIn == null) {
                    throw new IOException("Cannot open file");
                }

                byte[] buffer = new byte[4096];

                long remaining = fileSize;

                while (remaining > 0) {

                    int count = fileIn.read(
                            buffer,
                            0,
                            (int) Math.min(
                                    buffer.length,
                                    remaining
                            )
                    );

                    if (count == -1) {
                        throw new IOException("File changed during transfer");
                    }

                    networkOut.write(buffer, 0, count);
                    remaining -= count;
                }

                networkOut.flush();
            }

            if (!"DONE".equals(readLine(networkIn))) {
                throw new IOException("Transfer incomplete");
            }

            writeLine(networkOut, "END");
        }
    }
                          }
