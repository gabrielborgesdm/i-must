package com.gabriel.taskapp.service.repositories.remote;

import com.gabriel.taskapp.service.models.remote.ResponseTaskModel;
import com.gabriel.taskapp.service.models.remote.ResponseTasksModel;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TaskService {
    @POST("tasks")
    Call<ResponseTasksModel> createTasks(@Body RequestBody body);

    @GET("tasks")
    Call<ResponseTasksModel> getTasks();

    @DELETE("task/{id}")
    Call<ResponseTaskModel> removeTask(@Path("id") String id);
}
