package com.example.newkey;

import android.app.NotificationChannel;
import android.app.NotificationManager;
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

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.real_icon) // 작은 아이콘 설정
                .setContentTitle("정기 알림")
                .setContentText("지정된 시간이 되었습니다.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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
