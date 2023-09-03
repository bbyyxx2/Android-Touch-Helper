package com.zfdang.notificationhelper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class ForeGroundService extends Service {

    private static final String TAG = ForeGroundService.class.getSimpleName();

    private void startForegroundService() {
        NotificationHelper.createNotificationChannel(getApplicationContext());
        startForeground(1, NotificationHelper.createNotification(getApplicationContext()));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForegroundService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
