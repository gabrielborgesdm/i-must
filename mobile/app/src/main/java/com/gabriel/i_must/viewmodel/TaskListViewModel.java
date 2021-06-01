package com.gabriel.i_must.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gabriel.i_must.service.models.local.LocalTaskModel;
import com.gabriel.i_must.service.repositories.AlarmRepository;
import com.gabriel.i_must.service.repositories.ImageRepository;
import com.gabriel.i_must.service.repositories.local.LocalTasksRepository;

import java.util.List;


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