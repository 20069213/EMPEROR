package com.emperor.transfer;

import android.app.Service;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

public class EmperorMusicService extends Service {

    private static final String CHANNEL_ID = "emperor_music_channel";
    private static final int NOTIFICATION_ID = 1001;

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();

        Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("EMPEROR CYBER SECURITY")
                .setContentText("الموسيقى تعمل في الخلفية")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .build();

        startForeground(NOTIFICATION_ID, notification);

        mediaPlayer = MediaPlayer.create(this, R.raw.music);

        if (mediaPlayer != null) {
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Emperor Music",
                    NotificationManager.IMPORTANCE_LOW
            );

            NotificationManager manager =
                    getSystemService(NotificationManager.class);

            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }

            mediaPlayer.release();
            mediaPlayer = null;
        }

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
