package com.emperor.transfer;

import java.io.File;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.io.IOException;

public class TcpServer {

    private ServerSocket serverSocket;
    private Thread serverThread;
    private volatile boolean running = false;

    public interface Listener {
        void onStatus(String message);
    }

    public void start(
            File receivedFolder,
            String token,
            Listener listener
    ) {

        if (running) {
            listener.onStatus("السيرفر يعمل بالفعل");
            return;
        }

        running = true;

        serverThread = new Thread(() -> {

            try {
                serverSocket = new ServerSocket(
                        TcpTransfer.PORT,
                        10,
                        InetAddress.getByName("0.0.0.0")
                );

                listener.onStatus(
                        "السيرفر يعمل على المنفذ 5001"
                );

                while (running) {

                    try {
                        TcpTransfer.receiveOne(
                                serverSocket,
                                token,
                                receivedFolder
                        );

                        listener.onStatus(
                                "تم استقبال ملف بنجاح"
                        );

                    } catch (IOException e) {

                        if (running) {
                            listener.onStatus(
                                    "مشكلة في الاتصال: " +
                                    e.getMessage()
                            );
                        }

                    } catch (Exception e) {

                        if (running) {
                            listener.onStatus(
                                    "تعذر استقبال الملف"
                            );
                        }
                    }
                }

            } catch (Exception e) {

                listener.onStatus(
                        "تعذر تشغيل السيرفر: " +
                        e.getMessage()
                );

            } finally {
                running = false;
                closeServer();
                listener.onStatus("السيرفر متوقف");
            }

        });

        serverThread.start();
    }

    public void stop() {
        running = false;
        closeServer();
    }

    private void closeServer() {
        try {
            if (serverSocket != null &&
                    !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException ignored) {
        }
    }

    public boolean isRunning() {
        return running;
    }
}
