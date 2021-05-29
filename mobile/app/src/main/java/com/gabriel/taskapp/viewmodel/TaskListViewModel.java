package com.gabriel.taskapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gabriel.taskapp.service.models.local.AlarmModel;
import com.gabriel.taskapp.service.models.local.LocalTaskModel;
import com.gabriel.taskapp.service.repositories.AlarmRepository;
import com.gabriel.taskapp.service.repositories.ImageRepository;
import com.gabriel.taskapp.service.repositories.local.LocalAlarmsRepository;
import com.gabriel.taskapp.service.repositories.local.LocalTasksRepository;

import java.util.List;

import static com.gabriel.taskapp.view.TasksWidget.sendRefreshBroadcast;

public class TaskListViewModel extends AndroidViewModel {
    private final LocalTasksRepository mTasksRepository = LocalTasksRepository.getRealmRepository();
    private final AlarmRepository mAlarmRepository;
    private ImageRepository mImageRepository;

    private MutableLiveData<List<LocalTaskModel>> mTodoList = new MutableLiveData();
    public LiveData<List<LocalTaskModel>> todoList = mTodoList;

    private MutableLiveData<List<LocalTaskModel>> mCompletedList = new MutableLiveData();
    public LiveData<List<LocalTaskModel>> completedList = mCompletedList;

    public TaskListViewModel(@NonNull Application application) {
        super(application);
        mAlarmRepository = new AlarmRepository(this.getApplication().getApplicationContext());
        mImageRepository = ImageRepository.getRepository(this.getApplication().getApplicationContext());
    }

    public void load() {
        mTodoList.setValue(mTasksRepository.getOpenTasks());
        mCompletedList.setValue(mTasksRepository.getCompleted());
        sendRefreshBroadcast(getApplication().getApplicationContext());
    }

    public void delete(LocalTaskModel task) {
        mTasksRepository.delete(task.getId(), false);
        mImageRepository.deleteImages(task.getImagePaths());
        mAlarmRepository.removeAlarmFromTask(task);
    }

    public void complete(LocalTaskModel task) {
        mTasksRepository.complete(task);
        mAlarmRepository.removeAlarmFromTask(task);
    }

}