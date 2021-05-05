package com.gabriel.taskapp.service.repository.remote;

import android.content.Context;
import android.util.Log;

import com.gabriel.taskapp.R;
import com.gabriel.taskapp.service.listener.APIListener;
import com.gabriel.taskapp.service.model.remote.TaskModel;
import com.gabriel.taskapp.service.model.remote.TasksModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_TAG;

public class TaskRepository {

    private final TaskService mRemote;
    private final Context mContext;

    public TaskRepository(Context context) {
        mContext = context;
        mRemote = RetrofitClient.createService(mContext, TaskService.class);
    }

    public void createTasks(JSONObject tasksBody, APIListener<TasksModel> listener) {
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), tasksBody.toString());
        Call<TasksModel> call = mRemote.createTasks(requestBody);
        call.enqueue(new Callback<TasksModel>() {
            @Override
            public void onResponse(@NotNull Call<TasksModel> call, @NotNull Response<TasksModel> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    Log.d(TASK_TAG, "onResponse: " +  response.message());
                    listener.onFailure(mContext.getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void onFailure(@NotNull Call<TasksModel> call, @NotNull Throwable t) {
                Log.d(TASK_TAG, "onFailure: " + t.getMessage());
                listener.onFailure(mContext.getString(R.string.something_went_wrong));
            }
        });
    }

    public void getTasks(APIListener<TasksModel> listener) {
        Call<TasksModel> call = mRemote.getTasks();
        call.enqueue(new Callback<TasksModel>() {
            @Override
            public void onResponse(@NotNull Call<TasksModel> call, @NotNull Response<TasksModel> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(mContext.getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void onFailure(@NotNull Call<TasksModel> call, @NotNull Throwable t) {
                Log.d(TASK_TAG, "onFailure: " + t.getMessage());
                listener.onFailure(mContext.getString(R.string.something_went_wrong));
            }
        });
    }

    public void removeTask(String id, APIListener<TaskModel> listener) {
        Call<TaskModel> call = mRemote.removeTask(id);
        call.enqueue(new Callback<TaskModel>() {
            @Override
            public void onResponse(@NotNull Call<TaskModel> call, @NotNull Response<TaskModel> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(mContext.getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void onFailure(@NotNull Call<TaskModel> call, @NotNull Throwable t) {
                Log.d(TASK_TAG, "onFailure: " + t.getMessage());
                listener.onFailure(mContext.getString(R.string.something_went_wrong));
            }
        });
    }
}
