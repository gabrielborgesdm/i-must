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
import com.gabriel.taskapp.service.model.local.LocalTaskModel;
import com.gabriel.taskapp.service.repository.DateRepository;
import com.gabriel.taskapp.service.repository.ImageRepository;
import com.gabriel.taskapp.service.repository.local.TaskRepository;

import org.json.JSONArray;

import java.util.ArrayList;

public class TaskFormViewModel extends AndroidViewModel {
    private Context mContext;
    private ImageRepository mImageRepository;
    private TaskRepository mRepository = TaskRepository.getRealmRepository();

    private MutableLiveData<Boolean> mSaveTodo = new MutableLiveData();
    public LiveData<Boolean> saveTodo = mSaveTodo;

    public ArrayList<String> localImagesPath = new ArrayList<>();
    private MutableLiveData<ArrayList<String>> mImagePaths = new MutableLiveData(localImagesPath);
    public LiveData<ArrayList<String>> imagePaths = mImagePaths;

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
            final ArrayList<String> imagePaths,
            final long lastSync,
            final boolean removed) {
        LocalTaskModel todo = new LocalTaskModel();
        if (id != null) {
            todo.setId(id);
        }
        todo.setDescription(description);
        todo.setCompleted(completed);
        todo.setLastSync(lastSync);
        todo.setImagePaths(imagePaths);
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

        localImagesPath.add(imageName);
        mImagePaths.setValue(localImagesPath);
    }

    public void removeImage(int position) {
        localImagesPath.remove(position);
        mImagePaths.setValue(localImagesPath);
    }


    public void toggleCollapsed() {
        if (!mIsCollapsed.getValue()) {
            loadImages(new ArrayList<>());
            mDueDate.setValue("");
        }
        mIsCollapsed.setValue(!mIsCollapsed.getValue());
    }

    public void updateDatetime(int year, int month, int dayOfMonth, int hourOfDay, int minute) {
        mDueDate.setValue(DateRepository.getFormattedDatetime(year, month, dayOfMonth, hourOfDay, minute));
    }


    public void loadImages(ArrayList<String> imagePaths) {
        localImagesPath = imagePaths;
        mImagePaths.setValue(localImagesPath);
    }
}
