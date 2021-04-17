package com.gabriel.taskapp.service.repository;

import android.content.Context;
import android.util.Log;

import com.gabriel.taskapp.service.constants.APIConstants;
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

    private final TaskRepository mRepository = TaskRepository.getRealmRepository();
    private final com.gabriel.taskapp.service.repository.remote.TaskRepository mRemoteTaskRepository;
    private TaskRepository mLocalRepository = TaskRepository.getRealmRepository();

    public SyncRepository(Context context){
        super(context);
        mRemoteTaskRepository = new com.gabriel.taskapp.service.repository.remote.TaskRepository(context);
    }

    public void syncTasks() {
        if(!isOnline()){
            return;
        }
        postTasks();
    }

    private void postTasks() {
        List<TaskModel> tasks = mRepository.getAllFiltered(TaskConstants.TASK_FILTER_ALL);
        List<TaskModel> filteredTasks = getNotSyncedTasks(tasks);

        if(filteredTasks == null || filteredTasks.size() == 0) return;

        try {
            JSONObject tasksObject = buildTasksObject(filteredTasks);
            mRemoteTaskRepository.createTasks(tasksObject, new APIListener<TasksModel>() {
                @Override
                public void onSuccess(TasksModel model) {
                    if(!model.status.equals(API_OPERATION_EXECUTED)) return;
                    if(model.tasks == null || model.tasks.size() == 0) return;
                    model.tasks.forEach( taskModel -> {
                        Log.d(TASK_TAG, "syncyng: " + taskModel);
                        taskModel.setLastSync(System.currentTimeMillis());
                        mLocalRepository.saveOrUpdate(taskModel);
                    });
                }

                @Override
                public void onFailure(String message) {
                    Log.d(TASK_TAG, "onFailure: " + message);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private List<TaskModel> getNotSyncedTasks(List<TaskModel> tasks){
        List<TaskModel> filteredTasks = new ArrayList<>();
        if(tasks != null && tasks.size() > 0){
            tasks.forEach( task -> {
                if (task.getLastSync() == 0) {
                    filteredTasks.add(task);
                }
            });
        }

        return  filteredTasks;
    }

    private JSONObject buildTasksObject(List<TaskModel> tasks) throws JSONException {
        JSONObject tasksObject = new JSONObject();
        JSONArray tasksArray = new JSONArray();

        tasks.forEach( task -> {
            JSONObject taskObject = new JSONObject();
            try {
                taskObject.put(TaskConstants.TASK_DESCRIPTION, task.getDescription());
                taskObject.put(TaskConstants.TASK_COMPLETED, task.getCompleted());
                taskObject.put(TaskConstants.TASK_ID, task.getId());
                tasksArray.put(taskObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        tasksObject.put(TaskConstants.TASK_TASKS, tasksArray);
        return tasksObject;
    }
}
