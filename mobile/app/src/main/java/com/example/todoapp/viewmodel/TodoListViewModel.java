package com.example.todoapp.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class TodoListViewModel extends AndroidViewModel {
    private Context mContext;



    public TodoListViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
    }
}