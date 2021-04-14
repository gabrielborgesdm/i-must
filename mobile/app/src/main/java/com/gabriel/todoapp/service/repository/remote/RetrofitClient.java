package com.gabriel.todoapp.service.repository.remote;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    public static String token = null;
    static final String BASE_URL = "http://192.168.25.2:3333/";

    public static Retrofit getRetrofitInstance() {
        OkHttpClient httpClient;
        if(token != null){
            httpClient = new OkHttpClient().newBuilder()
                    .addInterceptor(chain -> {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Authorization", "Bearer" + token)
                                .build();
                        return chain.proceed(request);
                    })
                    .build();
        } else {
            httpClient = new OkHttpClient().newBuilder().build();
        }
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static <S> S createService(Class<S> serviceClass) {
        return getRetrofitInstance().create(serviceClass);
    }

    public static void addToken(String token_string) {
        token = token_string;
    }
}
