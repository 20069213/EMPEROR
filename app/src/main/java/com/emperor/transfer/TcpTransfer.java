package com.emperor.transfer;

import android.util.Log;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class TcpTransfer {

    public static final int PORT = 5001;
    public static final int BUFFER_SIZE = 4096;
    public static final String TOKEN = "EMPEROR_2026";

    private static final long MAX_FILE_SIZE =
            1024L * 1024L * 1024L;

    private TcpTransfer() {}

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

        ByteArrayOutputStream buffer =
                new ByteArrayOutputStream();

        int value;

        while ((value = in.read()) != -1) {
            if (value == '\n') break;

            if (buffer.size() >= 8192) {
                throw new IOException("Message too large");
            }

            buffer.write(value);
        }

        if (value == -1) {
            throw new EOFException("Connection closed");
        }

        return buffer.toString("UTF-8").trim();
    }

    public static void sendFile(
            String host,
            String token,
            File file
    ) throws Exception {

        if (file == null || !file.isFile()) {
            throw new IOException("Invalid file");
        }

        if (file.length() > MAX_FILE_SIZE) {
            throw new IOException("File too large");
        }

        if (!TransferProtocol.isValidFileName(
                file.getName()
        )) {
            throw new IOException("Invalid filename");
        }

        try (
            Socket socket = new Socket()
        ) {
            socket.connect(
                    new InetSocketAddress(host, PORT),
                    10000
            );

            socket.setSoTimeout(15000);

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            writeLine(out, token);

            if (!"OK".equals(readLine(in))) {
                throw new IOException("Authentication failed");
            }

            JSONObject metadata = new JSONObject();
            metadata.put("name", file.getName());
            metadata.put("size", file.length());

            writeLine(out, metadata.toString());

            if (!"READY".equals(readLine(in))) {
                throw new IOException("Receiver not ready");
            }

            try (FileInputStream fileIn =
                         new FileInputStream(file)) {

                byte[] buffer = new byte[BUFFER_SIZE];
                int count;

                while ((count = fileIn.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }

                out.flush();
            }

            if (!"DONE".equals(readLine(in))) {
                throw new IOException("Transfer incomplete");
            }

            writeLine(out, "END");
        }
    }

    public static void receiveOne(
            ServerSocket server,
            String expectedToken,
            File outputDirectory
    ) throws Exception {

        try (
            Socket socket = server.accept()
        ) {
            socket.setSoTimeout(15000);

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            String receivedToken = readLine(in);

            if (!expectedToken.equals(receivedToken)) {
                throw new IOException("Authentication failed");
            }

            writeLine(out, "OK");

            JSONObject metadata =
                    new JSONObject(readLine(in));

            String name = metadata.getString("name");
            long size = metadata.getLong("size");

            if (!TransferProtocol.isValidFileName(name)
                    || !TransferProtocol.isValidFileSize(size)
                    || size > MAX_FILE_SIZE) {
                throw new IOException("Invalid file information");
            }

            if (!outputDirectory.exists()
                    && !outputDirectory.mkdirs()) {
                throw new IOException("Cannot create folder");
            }

            File destination =
                    new File(outputDirectory, name);

            if (destination.exists()) {
                throw new IOException("File already exists");
            }

            writeLine(out, "READY");

            try (
                FileOutputStream fileOut =
                        new FileOutputStream(destination)
            ) {
                byte[] buffer = new byte[BUFFER_SIZE];
                long remaining = size;

                while (remaining > 0) {
                    int count = in.read(
                            buffer,
                            0,
                            (int) Math.min(
                                    buffer.length,
                                    remaining
                            )
                    );

                    if (count == -1) {
                        throw new EOFException(
                                "Transfer interrupted"
                        );
                    }

                    fileOut.write(buffer, 0, count);
                    remaining -= count;
                }

                fileOut.flush();
            }

            writeLine(out, "DONE");

            if (!"END".equals(readLine(in))) {
                throw new IOException("Invalid ending message");
            }

            Log.i("EMPEROR", "Received: " + name);
        }
    }
        }
