package com.gabriel.taskapp.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gabriel.taskapp.service.model.local.TaskModel;
import com.gabriel.taskapp.service.repository.local.TaskRepository;

import java.util.List;

import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_TAG;

public class TaskListViewModel extends AndroidViewModel {
    private Context mContext;
    private TaskRepository mRepository = TaskRepository.getRealmRepository();

    private MutableLiveData<List<TaskModel>> mTodoList = new MutableLiveData();
    public LiveData<List<TaskModel>> todoList = mTodoList;

    private MutableLiveData<List<TaskModel>> mCompletedList = new MutableLiveData();
    public LiveData<List<TaskModel>> completedList = mCompletedList;

    public TaskListViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
    }

    public void load() {
        Log.d(TASK_TAG, "load: ");
        mTodoList.setValue(mRepository.getTodo());
        mCompletedList.setValue(mRepository.getCompleted());
    }

    public void delete(TaskModel todo) {
        mRepository.delete(todo.getId());
    }

    public void complete(TaskModel todo) {
        mRepository.complete(todo);
    }
}