package com.gabriel.i_must.service.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.gabriel.i_must.view.MainActivity;
import com.gabriel.i_must.R;
import com.gabriel.i_must.service.models.local.LocalTaskModel;
import com.gabriel.i_must.service.repositories.AlarmRepository;
import com.gabriel.i_must.service.repositories.local.LocalAlarmsRepository;
import com.gabriel.i_must.service.repositories.local.LocalTasksRepository;

import java.util.List;

import static android.app.NotificationManager.IMPORTANCE_LOW;
import static com.gabriel.i_must.service.constants.AlarmConstants.NOTIFICATION_ACTION;
import static com.gabriel.i_must.service.constants.AlarmConstants.NOTIFICATION_CHANNEL_ID;
import static com.gabriel.i_must.service.constants.AlarmConstants.NOTIFICATION_CHANNEL_NAME;
import static com.gabriel.i_must.service.constants.AlarmConstants.NOTIFICATION_ID;
import static com.gabriel.i_must.service.repositories.local.RealmHelpers.startRealmContext;

public class AlarmService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getTasksAndSetAlarms();
        stopService();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void stopService() {
        stopForeground(true);
        stopSelf();
    }

    private void getTasksAndSetAlarms() {
        startOrUpdateNotification(false, this.getString(R.string.alarm_notification_setting_alarms));
        startRealmContext(this);
        AlarmRepository alarmRepository = new AlarmRepository(this);
        LocalAlarmsRepository localAlarmsRepository = LocalAlarmsRepository.getRealmRepository();
        localAlarmsRepository.deleteAll();
        List<LocalTaskModel> tasks = LocalTasksRepository.getRealmRepository().getOpenTasks();
        if(tasks != null && tasks.size() > 0) {
            for(int i = 0; i < tasks.size(); i++){
                LocalTaskModel task = tasks.get(i);
                alarmRepository.setOrUpdateAlarmFromTask(task);
            }

        }
    }

    private void startOrUpdateNotification(Boolean isUpdating, String text) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (isUpdating) {
            notificationManager.notify(NOTIFICATION_ID, getNotification(text));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(notificationManager);
            }
            startForeground(NOTIFICATION_ID, getNotification(text));
        }

    }

    private Notification getNotification(String text) {
        return new Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(this.getString(R.string.alarm_notification_setting_alarms))
                .setContentText(this.getString(R.string.alarm_notification_setting_alarms))
                .setSmallIcon(R.drawable.ic_baseline_calendar_today_24)
                .setContentIntent(getPendingIntent())
                .build();
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(NOTIFICATION_ACTION);
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationManager notificationManager) {
        NotificationChannel channel = new NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                IMPORTANCE_LOW
        );
        notificationManager.createNotificationChannel(channel);
    }

}