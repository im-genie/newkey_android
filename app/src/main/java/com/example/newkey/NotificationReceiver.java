package com.example.newkey;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "newkey_channel";

    @Override
    public void onReceive(Context context, Intent intent) {

        createNotificationChannel(context);

        // 알림 클릭 시 실행될 Intent 생성
        Intent notificationIntent = new Intent(context, notification1.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // 새 Task를 생성하거나 기존 Task를 클리어

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.noti_icon_2) // 작은 아이콘 설정
                .setContentTitle("Newkey 뉴키")
                .setContentText("HOT 뉴스를 확인해보세요!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // 알림 클릭 시 실행될 Intent
                .setAutoCancel(true); // 알림을 클릭하면 자동으로 제거되도록 설정

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            // 알림 ID를 고유하게 설정
            int notificationId = (int) System.currentTimeMillis();
            notificationManager.notify(notificationId, builder.build());
            Log.d("Notification", "Notification sent by AlarmManager with ID: " + notificationId);
        } else {
            Log.e("Notification", "NotificationManager is null");
        }
    }

    private void createNotificationChannel(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                NotificationChannel existingChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
                if (existingChannel == null) {
                    CharSequence name = "NewKeyChannel";
                    String description = "Channel for NewKey notifications";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                    channel.setDescription(description);
                    notificationManager.createNotificationChannel(channel);
                    Log.d("Notification", "Notification channel created by AlarmManager");
                } else {
                    Log.d("Notification", "Notification channel already exists");
                }
            } else {
                Log.e("Notification", "NotificationManager is null, can't create notification channel");
            }
        }
    }
}
