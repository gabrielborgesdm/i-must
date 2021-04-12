package com.gabriel.todoapp.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gabriel.todoapp.service.model.local.TodoModel;
import com.gabriel.todoapp.service.repository.local.TodoRepository;

import java.util.List;

import static com.gabriel.todoapp.service.constants.TodoConstants.TODO_TAG;

public class TodoListViewModel extends AndroidViewModel {
    private Context mContext;
    private TodoRepository mRepository = TodoRepository.getRealmRepository();

    private MutableLiveData<List<TodoModel>> mTodoList = new MutableLiveData();
    public LiveData<List<TodoModel>> todoList = mTodoList;

    private MutableLiveData<List<TodoModel>> mCompletedList = new MutableLiveData();
    public LiveData<List<TodoModel>> completedList = mCompletedList;

    public TodoListViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
    }

    public void load() {
        Log.d(TODO_TAG, "load: ");
        mTodoList.setValue(mRepository.getTodo());
        mCompletedList.setValue(mRepository.getCompleted());
    }

    public void delete(TodoModel todo) {
        mRepository.delete(todo.getId());
    }

    public void complete(TodoModel todo) {
        mRepository.complete(todo);
    }
}