package com.gabriel.i_must.service.receivers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.gabriel.i_must.R;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.gabriel.i_must.service.constants.AlarmConstants.ALARM_TASK_DESCRIPTION;
import static com.gabriel.i_must.service.constants.AlarmConstants.NOTIFICATION_ACTION;
import static com.gabriel.i_must.service.constants.AlarmConstants.NOTIFICATION_CHANNEL_ID;
import static com.gabriel.i_must.service.constants.AlarmConstants.NOTIFICATION_CHANNEL_NAME;
import static com.gabriel.i_must.service.constants.AlarmConstants.NOTIFICATION_ID;


public class AlarmReceiver extends BroadcastReceiver {
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        String message = context.getString(R.string.there_is_a_task_that_needs_your_attention);
        if (intent != null && intent.getStringExtra(ALARM_TASK_DESCRIPTION) != null) {
            message = intent.getStringExtra(ALARM_TASK_DESCRIPTION);
        }

        startOrUpdateNotification(message);
    }

    private void startOrUpdateNotification(String text) {
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager);
        }
        notificationManager.notify(NOTIFICATION_ID, getNotification(text));

    }

    private Notification getNotification(String text) {
        return new Notification.Builder(mContext, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(mContext.getString(R.string.time_to_complete_a_task))
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_baseline_calendar_today_24)
                .setContentIntent(getPendingIntent())
                .setAutoCancel(true)
                .build();
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        intent.setAction(NOTIFICATION_ACTION);
        return PendingIntent.getActivity(mContext, 0, intent, 0);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationManager notificationManager) {
        NotificationChannel channel = new NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                IMPORTANCE_DEFAULT
        );
        notificationManager.createNotificationChannel(channel);
    }

}
