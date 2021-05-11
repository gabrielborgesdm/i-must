package com.gabriel.taskapp.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gabriel.taskapp.R;
import com.gabriel.taskapp.service.model.local.TaskModel;
import com.gabriel.taskapp.service.repository.DateRepository;
import com.gabriel.taskapp.service.repository.local.TaskRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_TAG;
import static java.util.Collections.emptyList;

public class TaskFormViewModel extends AndroidViewModel {
    private Context mContext;
    private TaskRepository mRepository = TaskRepository.getRealmRepository();

    private MutableLiveData<Boolean> mSaveTodo = new MutableLiveData();
    public LiveData<Boolean> saveTodo = mSaveTodo;

    private MutableLiveData<List<Integer>> mImageIds = new MutableLiveData(emptyList());
    public LiveData<List<Integer>> imageIds = mImageIds;

    private MutableLiveData<Boolean> mIsCollapsed = new MutableLiveData(true);
    public LiveData<Boolean> isCollapsed = mIsCollapsed;

    private MutableLiveData<String> mDueDate = new MutableLiveData();
    public LiveData<String> dueDate = mDueDate;


    public TaskFormViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
    }

    public void saveOrUpdate(
            final String id,
            final String description,
            final boolean completed,
            final long lastSync,
            final boolean removed) {
        TaskModel todo = new TaskModel();
        if (id != null) {
            todo.setId(id);
        }
        todo.setDescription(description);
        todo.setCompleted(completed);
        todo.setLastSync(lastSync);
        mSaveTodo.setValue(mRepository.saveOrUpdate(todo));
    }

    public void uploadImage() {
        List<Integer> localImageIds =  new ArrayList<>();
        localImageIds.add( R.drawable.widget_preview);
        localImageIds.add( R.drawable.widget_preview);
        localImageIds.add( R.drawable.widget_preview);
        mImageIds.setValue(localImageIds);
        Log.d(TASK_TAG, "uploadImage: " + localImageIds.toString());
    }



    public void toggleCollapsed(){
        mIsCollapsed.setValue(!mIsCollapsed.getValue());
    }

    public void updateDatetime(int year, int month, int dayOfMonth, int hourOfDay, int minute) {
        mDueDate.setValue(DateRepository.getFormattedDatetime(year, month, dayOfMonth, hourOfDay, minute));
    }
}
