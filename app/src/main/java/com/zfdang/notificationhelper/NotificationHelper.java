package com.zfdang.notificationhelper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.zfdang.touchhelper.MainActivity;
import com.zfdang.touchhelper.R;

public class NotificationHelper {
    private static final String CHANNEL_ID = "MyChannelId";
    private static final int NOTIFICATION_ID = 1;

    public static Notification createNotification(Context context) {
        // 创建通知渠道
//        createNotificationChannel(context);

        // 创建点击通知时要启动的 Intent
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 创建通知对象
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_touch_helper_icon)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("前台服务正在运行")
                .setContentIntent(pendingIntent)
                .setAutoCancel(false);

        // 显示通知
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());

        Notification notification = builder.build();
        return notification;
    }

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "My Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
    }
}

