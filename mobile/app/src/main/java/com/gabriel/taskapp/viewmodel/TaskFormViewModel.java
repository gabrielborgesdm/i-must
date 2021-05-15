package com.gabriel.taskapp.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
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

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class TaskFormViewModel extends AndroidViewModel {
    private Context mContext;
    private ImageRepository mImageRepository;
    private TaskRepository mRepository = TaskRepository.getRealmRepository();

    private MutableLiveData<Boolean> mSaveTodo = new MutableLiveData();
    public LiveData<Boolean> saveTodo = mSaveTodo;

    public JSONArray localImagesPath = new JSONArray();
    private MutableLiveData<JSONArray> mImagesPaths = new MutableLiveData(localImagesPath);
    public LiveData<JSONArray> imagesPaths = mImagesPaths;

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
            final String datetime,
            final JSONArray imagesPaths,
            final long lastSync,
            final boolean removed) {
        TaskModel todo = new TaskModel();
        if (id != null) {
            todo.setId(id);
        }
        todo.setDescription(description);
        todo.setCompleted(completed);
        todo.setLastSync(lastSync);
        todo.setImagesPaths(imagesPaths);
        todo.setDatetime(datetime);
        todo.setRemoved(removed);
        mSaveTodo.setValue(mRepository.saveOrUpdate(todo));
    }

    public void uploadImage(Uri imageURI) {
        String imageName = mImageRepository.writeImage(imageURI);
        if (imageName == null) {
            Toast.makeText(mContext, mContext.getString(R.string.wait_before_trying_again), Toast.LENGTH_SHORT).show();
            return;
        }

        localImagesPath.put(imageName);
        mImagesPaths.setValue(localImagesPath);
    }

    public void removeImage(int position) {
        localImagesPath.remove(position);
        mImagesPaths.setValue(localImagesPath);
    }


    public void toggleCollapsed() {
        if (!mIsCollapsed.getValue()) {
            loadImages(new JSONArray());
            mDueDate.setValue("");
        }
        mIsCollapsed.setValue(!mIsCollapsed.getValue());
    }

    public void updateDatetime(int year, int month, int dayOfMonth, int hourOfDay, int minute) {
        mDueDate.setValue(DateRepository.getFormattedDatetime(year, month, dayOfMonth, hourOfDay, minute));
    }


    public void loadImages(JSONArray imagesPaths) {
        localImagesPath = imagesPaths;
        mImagesPaths.setValue(localImagesPath);
    }
}
