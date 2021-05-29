package com.gabriel.taskapp.service.services;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.gabriel.taskapp.view.MainActivity;
import com.gabriel.taskapp.R;
import com.gabriel.taskapp.service.repositories.SyncRepository;
import com.gabriel.taskapp.service.repositories.local.SecurityPreferences;

import java.util.Timer;
import java.util.TimerTask;

import static android.app.NotificationManager.IMPORTANCE_LOW;
import static com.gabriel.taskapp.service.constants.SyncConstants.ACTION_SHOW_SYNC_FRAGMENT;
import static com.gabriel.taskapp.service.constants.SyncConstants.BUNDLED_LISTENER;
import static com.gabriel.taskapp.service.constants.SyncConstants.LAST_SYNC_SHARED_PREFERENCE;
import static com.gabriel.taskapp.service.constants.SyncConstants.NOTIFICATION_CHANNEL_ID;
import static com.gabriel.taskapp.service.constants.SyncConstants.NOTIFICATION_CHANNEL_NAME;
import static com.gabriel.taskapp.service.constants.SyncConstants.NOTIFICATION_ID;
import static com.gabriel.taskapp.service.constants.SyncConstants.SYNC_SERVICE_MESSAGE;
import static com.gabriel.taskapp.service.constants.SyncConstants.SYNC_SERVICE_SUCCESS;

public class SyncService extends Service {
    public static int timeout;
    private Context mContext;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = this;
        ResultReceiver receiver = intent.getParcelableExtra(BUNDLED_LISTENER);
        SyncRepository sync = new SyncRepository(getApplicationContext());
        Bundle bundle = new Bundle();
        if (!sync.checkIsOnline()) {
            bundle.putBoolean(SYNC_SERVICE_SUCCESS, false);
            bundle.putString(SYNC_SERVICE_MESSAGE, this.getString(R.string.sync_internet_connection_required));
            receiver.send(Activity.RESULT_CANCELED, bundle);
        } else {
            syncTasks(sync);
            bundle.putBoolean(SYNC_SERVICE_SUCCESS, true);

            new Thread(() -> {
                timeout = 30 * 1000;
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        boolean isFinished = sync.isSyncFinished();
                        timeout -= 100;

                        if (isFinished || timeout <= 0) {
                            SecurityPreferences mSharedPreferences = new SecurityPreferences(mContext);
                            mSharedPreferences.storeLong(LAST_SYNC_SHARED_PREFERENCE, System.currentTimeMillis());
                            receiver.send(Activity.RESULT_OK, bundle);
                            stopService();
                            timer.cancel();
                        }
                    }
                }, 0, 100);


            }).start();
        }

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

    private void syncTasks(SyncRepository sync) {
        startOrUpdateNotification(false, this.getString(R.string.sync_notification_getting_started));
        sync.deleteRemovedTasks();
        startOrUpdateNotification(true, this.getString(R.string.sync_notification_syncing));
        sync.postNewOrUpdatedTasks();
        startOrUpdateNotification(true, this.getString(R.string.sync_notification_finishing));
        sync.getNonSyncedTasks();
    }
}