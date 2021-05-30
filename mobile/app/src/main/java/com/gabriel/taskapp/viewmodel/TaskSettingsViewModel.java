package com.gabriel.taskapp.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gabriel.taskapp.service.repositories.AlarmRepository;
import com.gabriel.taskapp.service.repositories.ImageRepository;
import com.gabriel.taskapp.service.repositories.local.LocalAlarmsRepository;
import com.gabriel.taskapp.service.repositories.local.LocalTasksRepository;
import com.gabriel.taskapp.service.repositories.local.SecurityPreferences;

public class TaskSettingsViewModel extends AndroidViewModel {
    private final LocalTasksRepository mTasksRepository = LocalTasksRepository.getRealmRepository();
    private LocalAlarmsRepository mLocalAlarmsRepository = LocalAlarmsRepository.getRealmRepository();
    private AlarmRepository mAlarmRepository;
    private SecurityPreferences mSharedPreferences;
    private ImageRepository mImageRepository;

    private MutableLiveData<Boolean> mIsLoggedOut = new MutableLiveData();
    public LiveData<Boolean> loggedOut = mIsLoggedOut;

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
}