package com.gabriel.taskapp.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gabriel.taskapp.R;
import com.gabriel.taskapp.service.model.local.TaskModel;
import com.gabriel.taskapp.service.repository.DateRepository;
import com.gabriel.taskapp.service.repository.ImageRepository;
import com.gabriel.taskapp.service.repository.local.TaskRepository;

import java.util.ArrayList;
import java.util.List;

import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_TAG;
import static java.util.Collections.emptyList;

public class TaskFormViewModel extends AndroidViewModel {
    private Context mContext;
    private ImageRepository mImageRepository;
    private TaskRepository mRepository = TaskRepository.getRealmRepository();

    private MutableLiveData<Boolean> mSaveTodo = new MutableLiveData();
    public LiveData<Boolean> saveTodo = mSaveTodo;

    private List<String> mLocalImagePaths = new ArrayList<>();
    private MutableLiveData<List<String>> mImagePaths = new MutableLiveData(mLocalImagePaths);
    public LiveData<List<String>> imagePaths = mImagePaths;

    private MutableLiveData<Boolean> mIsCollapsed = new MutableLiveData(true);
    public LiveData<Boolean> isCollapsed = mIsCollapsed;

    private MutableLiveData<String> mDueDate = new MutableLiveData();
    public LiveData<String> dueDate = mDueDate;


    public TaskFormViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        mImageRepository = ImageRepository.getRepository(mContext);
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

    public void uploadImage(Uri imageURI) {
        String imageName = mImageRepository.writeImage(imageURI);
        if(imageName == null) {
            Toast.makeText(mContext, mContext.getString(R.string.wait_before_trying_again), Toast.LENGTH_SHORT).show();
            return;
        }

        mLocalImagePaths.add(imageName);
        mImagePaths.setValue(mLocalImagePaths);
    }


    public void toggleCollapsed() {
        mIsCollapsed.setValue(!mIsCollapsed.getValue());
    }

    public void updateDatetime(int year, int month, int dayOfMonth, int hourOfDay, int minute) {
        mDueDate.setValue(DateRepository.getFormattedDatetime(year, month, dayOfMonth, hourOfDay, minute));
    }
}
