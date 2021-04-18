package com.gabriel.taskapp.service.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.gabriel.taskapp.MainActivity;
import com.gabriel.taskapp.R;
import com.gabriel.taskapp.service.repository.SyncRepository;

import static android.app.NotificationManager.IMPORTANCE_LOW;
import static com.gabriel.taskapp.service.constants.SyncConstants.ACTION_SHOW_SYNC_FRAGMENT;
import static com.gabriel.taskapp.service.constants.SyncConstants.NOTIFICATION_CHANNEL_ID;
import static com.gabriel.taskapp.service.constants.SyncConstants.NOTIFICATION_CHANNEL_NAME;
import static com.gabriel.taskapp.service.constants.SyncConstants.NOTIFICATION_ID;
import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_TAG;

public class SyncService extends Service {

    private final IBinder mBinder = new MyBinder();

    public class MyBinder extends Binder {

        public SyncService getService(){
            return SyncService.this;
        }

    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void startSyncing() {
        SyncRepository sync = new SyncRepository(getApplicationContext());
        if(!sync.checkIsOnline()) return;

        startOrUpdateNotification(false, this.getString(R.string.sync_notification_getting_started));
        sync.deleteRemovedTasks();
        startOrUpdateNotification(true, this.getString(R.string.sync_notification_syncing));
        sync.postNewOrUpdatedTasks();
        startOrUpdateNotification(true, this.getString(R.string.sync_notification_finishing));
        sync.getNonSyncedTasks();
        stopService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService();
    }

    private void stopService(){
        stopForeground(true);
        stopSelf();
    }

    private void startOrUpdateNotification(Boolean isUpdating, String text) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(isUpdating){
            notificationManager.notify(NOTIFICATION_ID, getNotification(text));
        } else {
            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(notificationManager);
            }
            startForeground(NOTIFICATION_ID, getNotification(text));
        }

    }

    private Notification getNotification(String text) {
        return new Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(this.getString(R.string.sync_notification_title))
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_baseline_sync_24)
                .setContentIntent(getPendingIntent())
                .build();
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(ACTION_SHOW_SYNC_FRAGMENT);
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