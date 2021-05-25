package com.gabriel.taskapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gabriel.taskapp.service.models.local.LocalTaskModel;
import com.gabriel.taskapp.service.repositories.local.LocalTasksRepository;

import java.util.List;

import static com.gabriel.taskapp.view.TasksWidget.sendRefreshBroadcast;

public class TaskListViewModel extends AndroidViewModel {
    private LocalTasksRepository mRepository = LocalTasksRepository.getRealmRepository();

    private MutableLiveData<List<LocalTaskModel>> mTodoList = new MutableLiveData();
    public LiveData<List<LocalTaskModel>> todoList = mTodoList;

    private MutableLiveData<List<LocalTaskModel>> mCompletedList = new MutableLiveData();
    public LiveData<List<LocalTaskModel>> completedList = mCompletedList;

    public TaskListViewModel(@NonNull Application application) {
        super(application);
    }

    public void load() {
        mTodoList.setValue(mRepository.getOpenTasks());
        mCompletedList.setValue(mRepository.getCompleted());
        sendRefreshBroadcast(getApplication().getApplicationContext());
    }

    public void delete(LocalTaskModel todo) {
        mRepository.delete(todo.getId(), false);
    }

    public void complete(LocalTaskModel todo) {
        mRepository.complete(todo);
    }

}