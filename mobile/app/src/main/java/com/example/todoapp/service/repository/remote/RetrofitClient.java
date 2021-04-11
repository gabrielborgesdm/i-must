package com.example.todoapp.service.repository.remote;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    static final String BASE_URL = "http://192.168.25.2:3333/";

    public static Retrofit getRetrofitInstance(){
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
        if(retrofit == null){
             retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                     .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static <S> S createService(Class<S> serviceClass) {
        return getRetrofitInstance().create(serviceClass);
    }
}
