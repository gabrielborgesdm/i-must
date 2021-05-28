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
import com.gabriel.taskapp.service.models.local.AlarmModel;
import com.gabriel.taskapp.service.models.local.LocalTaskModel;
import com.gabriel.taskapp.service.repositories.AlarmRepository;
import com.gabriel.taskapp.service.repositories.DateRepository;
import com.gabriel.taskapp.service.repositories.ImageRepository;
import com.gabriel.taskapp.service.repositories.local.LocalAlarmsRepository;
import com.gabriel.taskapp.service.repositories.local.LocalTasksRepository;

import java.util.ArrayList;

public class TaskFormViewModel extends AndroidViewModel {
    private Context mContext;
    private ImageRepository mImageRepository;
    private LocalTasksRepository mRealmRepository = LocalTasksRepository.getRealmRepository();
    private AlarmRepository mAlarmRepository;

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
        mAlarmRepository = new AlarmRepository(mContext);
    }

    public void saveOrUpdate(
            final String id,
            final String description,
            final boolean completed,
            final String datetime,
            final ArrayList<String> imagePaths,
            final long lastSync,
            final boolean removed) {
        LocalTaskModel task = new LocalTaskModel();
        if (id != null) {
            task.setId(id);
        }
        task.setDescription(description);
        task.setCompleted(completed);
        task.setLastSync(lastSync);
        task.setImagePaths(imagePaths);
        task.setDatetime(datetime);
        task.setRemoved(removed);

        boolean isOkay = mRealmRepository.saveOrUpdate(task);

        if(isOkay){
            mAlarmRepository.setOrUpdateAlarmFromTask(task);
        }
        mSaveTodo.setValue(isOkay);
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
