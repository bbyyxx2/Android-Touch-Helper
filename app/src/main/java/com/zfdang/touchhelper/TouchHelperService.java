package com.zfdang.touchhelper;

import android.accessibilityservice.AccessibilityService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;

import androidx.core.app.NotificationCompat;

import java.lang.ref.WeakReference;

public class TouchHelperService extends AccessibilityService {

    public final static int ACTION_REFRESH_KEYWORDS = 1;
    public final static int ACTION_REFRESH_PACKAGE = 2;
    public final static int ACTION_REFRESH_CUSTOMIZED_ACTIVITY = 3;
    public final static int ACTION_ACTIVITY_CUSTOMIZATION = 4;
    public final static int ACTION_STOP_SERVICE = 5;
    public final static int ACTION_START_SKIPAD = 6;
    public final static int ACTION_STOP_SKIPAD = 7;

    private static WeakReference<TouchHelperService> sServiceRef;
    private TouchHelperServiceImpl serviceImpl;

    private final String TAG = getClass().getName();

    private String channelId = "app notification";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    // 在服务的 onCreate 方法中创建通知通道
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    getResources().getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    // 在服务的 onStartCommand 方法中设置为前台服务
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();

        Intent startIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_touch_helper_icon)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("服务正在运行中")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);
//                .addAction(R.drawable.stop, "Stop Service", );

        startForeground(1, builder.build());

        return START_STICKY;
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        sServiceRef = new WeakReference<>(this);
        if (serviceImpl == null) {
            serviceImpl = new TouchHelperServiceImpl(this);
        }
        if (serviceImpl != null) {
            serviceImpl.onServiceConnected();
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (serviceImpl != null) {
            serviceImpl.onAccessibilityEvent(event);
        }
    }

    @Override
    public void onInterrupt() {
        if (serviceImpl != null) {
            serviceImpl.onInterrupt();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (serviceImpl != null) {
            serviceImpl.onUnbind(intent);
            serviceImpl = null;
        }
        sServiceRef = null;
        return super.onUnbind(intent);
    }

    public static boolean dispatchAction(int action) {
        final TouchHelperService service = sServiceRef != null ? sServiceRef.get() : null;
        if (service == null || service.serviceImpl == null) {
            return false;
        }
        service.serviceImpl.receiverHandler.sendEmptyMessage(action);
        return true;
    }

    public static boolean isServiceRunning() {
        final TouchHelperService service = sServiceRef != null ? sServiceRef.get() : null;
        return service != null && service.serviceImpl != null;
    }
}
