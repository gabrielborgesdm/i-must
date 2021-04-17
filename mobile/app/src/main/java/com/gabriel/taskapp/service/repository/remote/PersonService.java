package com.gabriel.taskapp.service.repository.remote;

import com.gabriel.taskapp.service.model.remote.HeaderModel;
import com.gabriel.taskapp.service.model.remote.SignUpModel;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PersonService {

    @POST("signin")
    public Call<HeaderModel> login(@Body RequestBody body);

    @POST("signup")
    public Call<SignUpModel> signUp(@Body RequestBody body);
}
