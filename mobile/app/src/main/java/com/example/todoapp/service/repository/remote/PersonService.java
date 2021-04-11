package com.example.todoapp.service.repository.remote;

import com.example.todoapp.service.HeaderModel;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface PersonService {

    @POST("signin")
    public Call<HeaderModel> login(@Body RequestBody body);
}
