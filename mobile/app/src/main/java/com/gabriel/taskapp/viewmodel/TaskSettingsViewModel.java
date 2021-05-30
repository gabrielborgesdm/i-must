package com.gabriel.taskapp.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gabriel.taskapp.BuildConfig;
import com.gabriel.taskapp.R;
import com.gabriel.taskapp.service.repositories.AlarmRepository;
import com.gabriel.taskapp.service.repositories.ImageRepository;
import com.gabriel.taskapp.service.repositories.local.LocalAlarmsRepository;
import com.gabriel.taskapp.service.repositories.local.LocalTasksRepository;
import com.gabriel.taskapp.service.repositories.local.SecurityPreferences;
import com.gabriel.taskapp.service.services.SyncService;

import java.util.concurrent.TimeUnit;

import static com.gabriel.taskapp.service.constants.SyncConstants.BUNDLED_LISTENER;
import static com.gabriel.taskapp.service.constants.SyncConstants.LAST_SYNC_SHARED_PREFERENCE;
import static com.gabriel.taskapp.service.constants.SyncConstants.SYNC_SERVICE_MESSAGE;

public class TaskSettingsViewModel extends AndroidViewModel {
    private final LocalTasksRepository mTasksRepository = LocalTasksRepository.getRealmRepository();
    private LocalAlarmsRepository mLocalAlarmsRepository = LocalAlarmsRepository.getRealmRepository();
    private AlarmRepository mAlarmRepository;
    private SecurityPreferences mSharedPreferences;
    private ImageRepository mImageRepository;

    private MutableLiveData<Boolean> mIsLoggedOut = new MutableLiveData();
    public LiveData<Boolean> isLoggedOut = mIsLoggedOut;

    private MutableLiveData<Boolean> mIsSynced = new MutableLiveData();
    public LiveData<Boolean> isSynced = mIsSynced;

    public TaskSettingsViewModel(@NonNull Application application) {
        super(application);
        mAlarmRepository = new AlarmRepository(getApplication().getApplicationContext());
        mSharedPreferences = new SecurityPreferences(getApplication().getApplicationContext());
        mImageRepository = ImageRepository.getRepository(getApplication().getApplicationContext());
    }

    public void logout() {
        mAlarmRepository.removeAllAlarms();
        mTasksRepository.removeAllTasks();
        mLocalAlarmsRepository.deleteAll();
        mImageRepository.deleteAllImages();
        mSharedPreferences.logout();
        mIsLoggedOut.setValue(true);
    }

    public void syncTasks() {
        Intent serviceIntent = new Intent(getApplication().getApplicationContext(), SyncService.class);
        serviceIntent.putExtra(BUNDLED_LISTENER, new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getApplication().getApplicationContext(), getApplication().getString(R.string.tasks_synced_with_success), Toast.LENGTH_SHORT).show();
                    mIsSynced.setValue(true);
                } else {
                    Toast.makeText(getApplication().getApplicationContext(), getApplication().getString(R.string.wasnt_possible_to_sync_tasks), Toast.LENGTH_SHORT).show();
                }
            }
        });
        getApplication().getApplicationContext().startForegroundService(serviceIntent);
    }
}