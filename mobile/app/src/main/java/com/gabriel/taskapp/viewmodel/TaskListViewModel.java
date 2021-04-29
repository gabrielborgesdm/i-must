package com.gabriel.taskapp.viewmodel;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gabriel.taskapp.service.model.local.TaskModel;
import com.gabriel.taskapp.service.repository.local.TaskRepository;
import com.gabriel.taskapp.service.services.SyncService;

import java.util.List;

import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_TAG;
import static com.gabriel.taskapp.view.TasksWidget.sendRefreshBroadcast;

public class TaskListViewModel extends AndroidViewModel {
    private TaskRepository mRepository = TaskRepository.getRealmRepository();

    private MutableLiveData<List<TaskModel>> mTodoList = new MutableLiveData();
    public LiveData<List<TaskModel>> todoList = mTodoList;

    private MutableLiveData<List<TaskModel>> mCompletedList = new MutableLiveData();
    public LiveData<List<TaskModel>> completedList = mCompletedList;

    public TaskListViewModel(@NonNull Application application) {
        super(application);
    }

    public void load() {
        mTodoList.setValue(mRepository.getOpenTasks());
        mCompletedList.setValue(mRepository.getCompleted());
        sendRefreshBroadcast(getApplication().getApplicationContext());
    }

    public void delete(TaskModel todo) {
        mRepository.delete(todo.getId(), false);
    }

    public void complete(TaskModel todo) {
        mRepository.complete(todo);
    }

}