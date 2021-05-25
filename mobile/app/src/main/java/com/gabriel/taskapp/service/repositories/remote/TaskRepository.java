package com.gabriel.taskapp.service.repositories.remote;

import android.content.Context;
import android.util.Log;

import com.gabriel.taskapp.R;
import com.gabriel.taskapp.service.listeners.APIListener;
import com.gabriel.taskapp.service.models.remote.ResponseTaskModel;
import com.gabriel.taskapp.service.models.remote.ResponseTasksModel;

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

    public void createTasks(JSONObject tasksBody, APIListener<ResponseTasksModel> listener) {
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), tasksBody.toString());
        Call<ResponseTasksModel> call = mRemote.createTasks(requestBody);
        call.enqueue(new Callback<ResponseTasksModel>() {
            @Override
            public void onResponse(@NotNull Call<ResponseTasksModel> call, @NotNull Response<ResponseTasksModel> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    Log.d(TASK_TAG, "onResponse: " +  response.message());
                    listener.onFailure(mContext.getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseTasksModel> call, @NotNull Throwable t) {
                Log.d(TASK_TAG, "onFailure: " + t.getMessage());
                listener.onFailure(mContext.getString(R.string.something_went_wrong));
            }
        });
    }

    public void getTasks(APIListener<ResponseTasksModel> listener) {
        Call<ResponseTasksModel> call = mRemote.getTasks();
        call.enqueue(new Callback<ResponseTasksModel>() {
            @Override
            public void onResponse(@NotNull Call<ResponseTasksModel> call, @NotNull Response<ResponseTasksModel> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(mContext.getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseTasksModel> call, @NotNull Throwable t) {
                Log.d(TASK_TAG, "onFailure: " + t.getMessage());
                listener.onFailure(mContext.getString(R.string.something_went_wrong));
            }
        });
    }

    public void removeTask(String id, APIListener<ResponseTaskModel> listener) {
        Call<ResponseTaskModel> call = mRemote.removeTask(id);
        call.enqueue(new Callback<ResponseTaskModel>() {
            @Override
            public void onResponse(@NotNull Call<ResponseTaskModel> call, @NotNull Response<ResponseTaskModel> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(mContext.getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseTaskModel> call, @NotNull Throwable t) {
                Log.d(TASK_TAG, "onFailure: " + t.getMessage());
                listener.onFailure(mContext.getString(R.string.something_went_wrong));
            }
        });
    }
}
