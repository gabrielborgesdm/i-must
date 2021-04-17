package com.gabriel.taskapp.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gabriel.taskapp.service.model.local.TaskModel;
import com.gabriel.taskapp.service.repository.local.TaskRepository;

public class TaskFormViewModel extends AndroidViewModel {
    private Context mContext;
    private TaskRepository mRepository = TaskRepository.getRealmRepository();

    private MutableLiveData<Boolean> mSaveTodo = new MutableLiveData();
    public LiveData<Boolean> saveTodo = mSaveTodo;

    public TaskFormViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
    }

    public void saveOrUpdate(final String id, final String description, final boolean completed, final long lastSync){
        TaskModel todo = new TaskModel();
        if(id != null) {
            todo.setId(id);
        }
        todo.setDescription(description);
        todo.setCompleted(completed);
        todo.setLastSync(lastSync);
        mSaveTodo.setValue(mRepository.saveOrUpdate(todo));
    }
}
