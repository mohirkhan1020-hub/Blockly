package com.example.blocklyn.service;

import android.app.*;
import android.content.Intent;
import android.os.*;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.blocklyn.MainActivity;
import com.example.blocklyn.R;

public class TimerService extends Service {
    public static final String ACTION_TOGGLE = "TOGGLE";
    public static final String ACTION_STOP   = "STOP";
    private static final String CHANNEL_ID   = "focus_timer";
    private static final int    NOTIF_ID     = 1001;

    private CountDownTimer timer;
    private long remainingMs = 25 * 60 * 1000L;
    private boolean running = true;

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIF_ID, buildNotification());
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(ACTION_TOGGLE)) toggle();
            if (intent.getAction().equals(ACTION_STOP))   stopSelf();
        } else {
            startTimer();
        }
        return START_STICKY;
    }

    private void toggle() {
        if (running) { if (timer != null) timer.cancel(); running = false; }
        else { startTimer(); }
        updateNotification();
    }

    private void startTimer() {
        running = true;
        timer = new CountDownTimer(remainingMs, 1000) {
            @Override public void onTick(long ms) {
                remainingMs = ms;
                updateNotification();
            }
            @Override public void onFinish() { stopSelf(); }
        }.start();
    }

    private Notification buildNotification() {
        createChannel();
        Intent open = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, open,
            PendingIntent.FLAG_IMMUTABLE);

        long m = remainingMs / 60000;
        long s = (remainingMs / 1000) % 60;

        return new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Focus Session")
            .setContentText(String.format("%02d:%02d remaining", m, s))
            .setSmallIcon(R.drawable.ic_focus)
            .setContentIntent(pi)
            .setOngoing(true)
            .build();
    }

    private void updateNotification() {
        NotificationManager nm = getSystemService(NotificationManager.class);
        nm.notify(NOTIF_ID, buildNotification());
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ch = new NotificationChannel(CHANNEL_ID,
                "Focus Timer", NotificationManager.IMPORTANCE_LOW);
            getSystemService(NotificationManager.class).createNotificationChannel(ch);
        }
    }

    @Nullable @Override public IBinder onBind(Intent i) { return null; }
}
