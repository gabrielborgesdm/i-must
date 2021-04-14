package com.gabriel.todoapp.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gabriel.todoapp.service.model.local.TaskModel;
import com.gabriel.todoapp.service.repository.local.TodoRepository;

import java.util.List;

import static com.gabriel.todoapp.service.constants.TaskConstants.TASK_TAG;

public class TodoListViewModel extends AndroidViewModel {
    private Context mContext;
    private TodoRepository mRepository = TodoRepository.getRealmRepository();

    private MutableLiveData<List<TaskModel>> mTodoList = new MutableLiveData();
    public LiveData<List<TaskModel>> todoList = mTodoList;

    private MutableLiveData<List<TaskModel>> mCompletedList = new MutableLiveData();
    public LiveData<List<TaskModel>> completedList = mCompletedList;

    public TodoListViewModel(@NonNull Application application) {
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