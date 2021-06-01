package com.gabriel.i_must.service.repositories.remote;

import android.content.Context;

import com.gabriel.i_must.service.repositories.remote.interceptors.AuthInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    public static String token = null;
    static final String BASE_URL = "http://192.168.25.2:3333/";

    private RetrofitClient() {
    }

    public static <S> S createService(Context context, Class<S> serviceClass) {
        return getRetrofitInstance(context).create(serviceClass);
    }


    private static OkHttpClient getOkhttpClient(Context context) {
        return new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(context))
                .build();
    }


    public static Retrofit getRetrofitInstance(Context context) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getOkhttpClient(context))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
