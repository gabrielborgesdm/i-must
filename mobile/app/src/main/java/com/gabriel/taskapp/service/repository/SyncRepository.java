package com.gabriel.taskapp.service.repository;

import android.content.Context;
import android.util.Log;

import com.gabriel.taskapp.service.constants.TaskConstants;
import com.gabriel.taskapp.service.listener.APIListener;
import com.gabriel.taskapp.service.model.local.TaskModel;
import com.gabriel.taskapp.service.model.remote.TasksModel;
import com.gabriel.taskapp.service.repository.local.TaskRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.gabriel.taskapp.service.constants.APIConstants.API_OPERATION_EXECUTED;
import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_TAG;

public class SyncRepository extends BaseRepository {

    private final com.gabriel.taskapp.service.repository.remote.TaskRepository mRemoteTaskRepository;
    private final TaskRepository mLocalRepository = TaskRepository.getRealmRepository();
    private Boolean isPostFinished = false;
    private Boolean isGetFinished = false;
    private Boolean isDeleteFinished = false;

    public SyncRepository(Context context) {
        super(context);
        mRemoteTaskRepository = new com.gabriel.taskapp.service.repository.remote.TaskRepository(context);
    }


    public void postNewOrUpdatedTasks() {
        List<TaskModel> tasks = mLocalRepository.getAllFiltered(TaskConstants.TASK_FILTER_ALL);
        List<TaskModel> filteredTasks = filterNonSyncedTasks(tasks);

        if (filteredTasks.size() == 0) {
            isPostFinished = true;
            return;
        }

        try {
            JSONObject tasksObject = buildTasksObject(filteredTasks);
            Log.d(TASK_TAG, "postNewOrUpdatedTasks: " + tasksObject.toString());
            mRemoteTaskRepository.createTasks(tasksObject, new APIListener<TasksModel>() {
                @Override
                public void onSuccess(TasksModel model) {
                    boolean isOkay = true;
                    if (!model.status.equals(API_OPERATION_EXECUTED)) isOkay = false;
                    if (model.tasks == null || model.tasks.size() == 0) isOkay = false;
                    if(isOkay){
                        model.tasks.forEach(taskModel -> {
                            Log.d(TASK_TAG, "postNewOrUpdatedTasks: " + taskModel.getDescription());
                            taskModel.setLastSync(System.currentTimeMillis());
                            taskModel.setRemoved(false);
                            mLocalRepository.saveOrUpdate(taskModel);
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

    private List<TaskModel> filterNonSyncedTasks(List<TaskModel> tasks) {
        List<TaskModel> filteredTasks = new ArrayList<>();
        if (tasks != null && tasks.size() > 0) {
            tasks.forEach(task -> {
                if (task.getLastSync() == 0) {
                    filteredTasks.add(task);
                }
            });
        }

        return filteredTasks;
    }

    private JSONObject buildTasksObject(List<TaskModel> tasks) throws JSONException {
        JSONObject tasksObject = new JSONObject();
        JSONArray tasksArray = new JSONArray();

        tasks.forEach(task -> {
            JSONObject taskObject = new JSONObject();
            try {
                taskObject.put(TaskConstants.TASK_DESCRIPTION, task.getDescription());
                taskObject.put(TaskConstants.TASK_COMPLETED, task.getCompleted());
                taskObject.put(TaskConstants.TASK_LAST_UPDATED, task.getLastUpdated());
                taskObject.put(TaskConstants.TASK_ID, task.getId());
                tasksArray.put(taskObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        tasksObject.put(TaskConstants.TASK_TASKS, tasksArray);
        return tasksObject;
    }

    public void getNonSyncedTasks() {
        List<TaskModel> localTasks = mLocalRepository.getAllFiltered(TaskConstants.TASK_FILTER_ALL);
        mRemoteTaskRepository.getTasks(new APIListener<TasksModel>() {
            @Override
            public void onSuccess(TasksModel model) {
                boolean isOkay = true;
                if (!model.status.equals(API_OPERATION_EXECUTED)) isOkay = false;
                if (model.tasks == null || model.tasks.size() == 0) isOkay = false;
                if(isOkay) {
                    model.tasks.forEach(taskModel -> {
                        if (!checkTaskIsSynced(taskModel, localTasks)) {
                            taskModel.setLastSync(System.currentTimeMillis());
                            Log.d(TASK_TAG, "getNonSyncedTasks: " + taskModel.getDescription());
                            Log.d(TASK_TAG, "getNonSyncedTasks: " + taskModel.getLastUpdated());
                            Log.d(TASK_TAG, "getNonSyncedTasks: " + taskModel.getCompleted());
                            Log.d(TASK_TAG, "getNonSyncedTasks: ----------------------------");
                            mLocalRepository.saveOrUpdate(taskModel);
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

    private Boolean checkTaskIsSynced(TaskModel remoteTask, List<TaskModel> localTasks) {
        boolean isSynced = false;
        if (localTasks == null || localTasks.size() == 0) return false;

        for (TaskModel localTask : localTasks) {
            if (localTask.getId().equals(remoteTask.getId())) {
                isSynced = true;
                break;
            }
        }
        return isSynced;
    }

    public void deleteRemovedTasks() {
        List<TaskModel> localTasks = mLocalRepository.getAllFiltered(TaskConstants.TASK_FILTER_REMOVED);
        if (localTasks != null && localTasks.size() > 0) {
            localTasks.forEach(localTask -> {
                mRemoteTaskRepository.removeTask(localTask.getId(), new APIListener<com.gabriel.taskapp.service.model.remote.TaskModel>() {
                    @Override
                    public void onSuccess(com.gabriel.taskapp.service.model.remote.TaskModel model) {
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

    public Boolean isSyncFinished() {
        return isDeleteFinished && isGetFinished && isPostFinished;
    }
}
