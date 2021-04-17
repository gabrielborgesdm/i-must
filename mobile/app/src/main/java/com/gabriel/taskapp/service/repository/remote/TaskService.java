package com.gabriel.taskapp.service.repository.remote;

import com.gabriel.taskapp.service.model.remote.TasksModel;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TaskService {
    @POST("tasks")
    Call<TasksModel> createTasks(@Body RequestBody body);

    @GET("tasks")
    Call<TasksModel> getTasks();
}
