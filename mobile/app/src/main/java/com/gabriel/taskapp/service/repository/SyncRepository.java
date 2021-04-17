package com.gabriel.taskapp.service.repository;

import android.content.Context;
import android.util.Log;

import com.gabriel.taskapp.service.constants.TaskConstants;
import com.gabriel.taskapp.service.model.local.TaskModel;
import com.gabriel.taskapp.service.repository.local.TaskRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SyncRepository extends BaseRepository {
    private final TaskRepository mRepository = TaskRepository.getRealmRepository();
    public SyncRepository(Context context){
        super(context);
    }

    public void syncTasks() {
        if(!isOnline()){
            return;
        }

        postTasks();


    }

    private void postTasks() {
        List<TaskModel> tasks = mRepository.getAllFiltered(TaskConstants.TASK_FILTER_ALL);
        try {
            JSONObject tasksObject = buildTasksObject(tasks);
            if(tasksObject != null){
                Log.d(TaskConstants.TASK_TAG, "postTasks: " + tasksObject.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private JSONObject buildTasksObject(List<TaskModel> tasks) throws JSONException {
        JSONObject tasksObject = new JSONObject();
        JSONArray tasksArray = new JSONArray();
        if(tasks != null && tasks.size() > 0) {
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
        } else {
            return null;
        }
    }


}
