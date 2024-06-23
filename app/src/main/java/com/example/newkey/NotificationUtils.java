package com.example.newkey;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

public class NotificationUtils {

    //간단한 알림을 만들고 수신기로 보내는 코드

    public static void showNotification(Context context, String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title) //알림 제목
                .setContentText(content) //알림 내용
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());

        // 알림을 수신기로 보냄
        Intent intent = new Intent("com.example.newkey.NOTIFICATION_SENT");
        context.sendBroadcast(intent);
    }
}
