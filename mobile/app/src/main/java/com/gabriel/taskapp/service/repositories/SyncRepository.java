package com.gabriel.taskapp.service.repositories;

import android.content.Context;
import android.util.Log;

import com.gabriel.taskapp.BuildConfig;
import com.gabriel.taskapp.service.constants.TaskConstants;
import com.gabriel.taskapp.service.listeners.APIListener;
import com.gabriel.taskapp.service.models.local.LocalTaskModel;
import com.gabriel.taskapp.service.models.remote.RemoteTaskModel;
import com.gabriel.taskapp.service.models.remote.ResponseTaskModel;
import com.gabriel.taskapp.service.models.remote.ResponseTasksModel;
import com.gabriel.taskapp.service.repositories.local.LocalTasksRepository;
import com.gabriel.taskapp.service.repositories.local.SecurityPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.gabriel.taskapp.service.constants.APIConstants.API_OPERATION_EXECUTED;
import static com.gabriel.taskapp.service.constants.SyncConstants.LAST_SYNC_SHARED_PREFERENCE;
import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_TAG;

public class SyncRepository extends BaseRepository {

    private final com.gabriel.taskapp.service.repositories.remote.TaskRepository mRemoteTaskRepository;
    private final LocalTasksRepository mLocalRepository = LocalTasksRepository.getRealmRepository();
    private final AlarmRepository mAlarmRepository;
    private final ImageRepository mImageRepository;
    private SecurityPreferences mSharedPreferences;
    private Boolean isPostFinished = false;
    private Boolean isGetFinished = false;
    private Boolean isDeleteFinished = false;

    public SyncRepository(Context context) {
        super(context);
        mRemoteTaskRepository = new com.gabriel.taskapp.service.repositories.remote.TaskRepository(context);
        mImageRepository = ImageRepository.getRepository(context);
        mAlarmRepository = new AlarmRepository(context);
        mSharedPreferences = new SecurityPreferences(context);

    }


    public void postNewOrUpdatedTasks() {
        List<LocalTaskModel> tasks = mLocalRepository.getAllFiltered(TaskConstants.TASK_FILTER_ALL);
        List<LocalTaskModel> filteredTasks = filterNonSyncedTasks(tasks);
        if (filteredTasks.size() == 0) {
            isPostFinished = true;
            return;
        }

        try {
            JSONObject tasksObject = buildTasksObject(filteredTasks);
            mRemoteTaskRepository.createTasks(tasksObject, new APIListener<ResponseTasksModel>() {
                @Override
                public void onSuccess(ResponseTasksModel model) {
                    boolean isOkay = true;
                    if (!model.status.equals(API_OPERATION_EXECUTED)) isOkay = false;
                    if (model.tasks == null || model.tasks.size() == 0) isOkay = false;
                    if (isOkay) {
                        model.tasks.forEach(taskModel -> {
                            LocalTaskModel localTask = new LocalTaskModel();
                            localTask.setValuesFromRemoteTask(mContext, taskModel);
                            localTask.setLastSync(System.currentTimeMillis());
                            localTask.setRemoved(false);
                            mLocalRepository.saveOrUpdate(localTask);
                            mAlarmRepository.setOrUpdateAlarmFromTask(localTask);
                        });
                    }
                    isPostFinished = true;
                }

                @Override
                public void onFailure(String message) {
                    Log.d(TASK_TAG, "onFailure: " + message);
                    isPostFinished = true;
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            isPostFinished = true;
        }

    }

    private List<LocalTaskModel> filterNonSyncedTasks(List<LocalTaskModel> tasks) {
        List<LocalTaskModel> filteredTasks = new ArrayList<>();
        if (tasks != null && tasks.size() > 0) {
            tasks.forEach(task -> {
                if (task.getLastSync() == 0) {
                    filteredTasks.add(task);
                }
            });
        }

        return filteredTasks;
    }

    private JSONArray getImagesInBase64(ArrayList<String> imagePaths) throws IOException {
        JSONArray images = new JSONArray();
        for (int i = 0; i < imagePaths.size(); i++) {
            String imagePath = imagePaths.get(i);
            String base64 = mImageRepository.convertFileToBase64(imagePath);
            if (base64 != null) images.put(base64);
        }
        return images;
    }

    private JSONObject buildTasksObject(List<LocalTaskModel> tasks) throws JSONException {
        JSONObject tasksObject = new JSONObject();
        JSONArray tasksArray = new JSONArray();

        tasks.forEach(task -> {
            JSONObject taskObject = new JSONObject();
            try {
                taskObject.put(TaskConstants.TASK_DESCRIPTION, task.getDescription());
                taskObject.put(TaskConstants.TASK_COMPLETED, task.getCompleted());
                taskObject.put(TaskConstants.TASK_LAST_UPDATED, task.getLastUpdated());
                taskObject.put(TaskConstants.TASK_DATETIME, task.getDatetime());
                taskObject.put(TaskConstants.TASK_ID, task.getId());
                ArrayList<String> imagePaths = task.getImagePaths();
                if (imagePaths.size() > 0) {
                    JSONArray images = getImagesInBase64(imagePaths);
                    if (images.length() > 0) {
                        taskObject.put(TaskConstants.TASK_IMAGES, images);
                    }
                }

                tasksArray.put(taskObject);
            } catch (JSONException | IOException e) {
                e.printStackTrace();

            }
        });
        tasksObject.put(TaskConstants.TASK_TASKS, tasksArray);
        return tasksObject;
    }

    public void getNonSyncedTasks() {
        List<LocalTaskModel> localTasks = mLocalRepository.getAllFiltered(TaskConstants.TASK_FILTER_ALL);
        mRemoteTaskRepository.getTasks(new APIListener<ResponseTasksModel>() {
            @Override
            public void onSuccess(ResponseTasksModel model) {
                boolean isOkay = true;
                if (!model.status.equals(API_OPERATION_EXECUTED)) isOkay = false;
                if (model.tasks == null || model.tasks.size() == 0) isOkay = false;
                if (isOkay) {
                    model.tasks.forEach(taskModel -> {
                        if (!checkTaskIsSynced(taskModel, localTasks)) {
                            LocalTaskModel localTask = new LocalTaskModel();
                            localTask.setValuesFromRemoteTask(mContext, taskModel);
                            localTask.setLastSync(System.currentTimeMillis());
                            mLocalRepository.saveOrUpdate(localTask);
                            mAlarmRepository.setOrUpdateAlarmFromTask(localTask);
                        }
                    });
                }
                isGetFinished = true;
            }

            @Override
            public void onFailure(String message) {
                Log.d(TASK_TAG, "onFailure: " + message);
                isGetFinished = true;
            }
        });
    }

    private Boolean checkTaskIsSynced(RemoteTaskModel remoteTask, List<LocalTaskModel> localTasks) {
        boolean isSynced = false;
        if (localTasks == null || localTasks.size() == 0) return false;

        for (LocalTaskModel localTask : localTasks) {
            if (localTask.getId().equals(remoteTask.getId())) {
                isSynced = true;
                break;
            }
        }
        return isSynced;
    }

    public void deleteRemovedTasks() {
        List<LocalTaskModel> localTasks = mLocalRepository.getAllFiltered(TaskConstants.TASK_FILTER_REMOVED);
        if (localTasks != null && localTasks.size() > 0) {
            localTasks.forEach(localTask -> {
                mRemoteTaskRepository.removeTask(localTask.getId(), new APIListener<ResponseTaskModel>() {
                    @Override
                    public void onSuccess(ResponseTaskModel model) {
                        if (model.status.equals(API_OPERATION_EXECUTED) && model.task != null) {
                            mLocalRepository.delete(model.task.getId(), true);
                        }
                        isDeleteFinished = true;
                    }

                    @Override
                    public void onFailure(String message) {
                        Log.d(TASK_TAG, "onFailure: " + message);
                        isDeleteFinished = true;
                    }
                });
            });
        } else {
            isDeleteFinished = true;
        }
    }

    public boolean checkShouldSync() {
        long lastSync = mSharedPreferences.getLong(LAST_SYNC_SHARED_PREFERENCE);
        long differenceInMillis = System.currentTimeMillis() - lastSync;
        long days = TimeUnit.MILLISECONDS.toDays(differenceInMillis);
        int daysInterval = BuildConfig.SYNC_DAYS_INTERVAL;

        return days > daysInterval;
    }

    public Boolean isSyncFinished() {
        return isDeleteFinished && isGetFinished && isPostFinished;
    }
}
