package com.gabriel.todoapp.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gabriel.todoapp.service.model.local.TaskModel;
import com.gabriel.todoapp.service.repository.local.TodoRepository;

public class TodoFormViewModel extends AndroidViewModel {
    private Context mContext;
    private TodoRepository mRepository = TodoRepository.getRealmRepository();

    private MutableLiveData<Boolean> mSaveTodo = new MutableLiveData();
    public LiveData<Boolean> saveTodo = mSaveTodo;

    public TodoFormViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
    }

    public void saveOrUpdate(final String id, final String description, final boolean completed){
        TaskModel todo = new TaskModel();
        if(id != null) {
            todo.setId(id);
        }
        todo.setDescription(description);
        todo.setCompleted(completed);
        mSaveTodo.setValue(mRepository.saveOrUpdate(todo));
    }
}