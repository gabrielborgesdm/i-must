package com.gabriel.i_must.service.repositories.remote;

import com.gabriel.i_must.service.models.remote.HeaderModel;
import com.gabriel.i_must.service.models.remote.SignUpModel;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PersonService {

    @POST("signin")
    Call<HeaderModel> login(@Body RequestBody body);

    @POST("signup")
    Call<SignUpModel> signUp(@Body RequestBody body);
}
