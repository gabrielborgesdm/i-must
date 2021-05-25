package com.gabriel.taskapp.service.repositories.remote.interceptors;

import android.content.Context;

import com.gabriel.taskapp.service.repositories.local.SecurityPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.gabriel.taskapp.service.constants.PersonConstants.PERSON_TOKEN;

public class AuthInterceptor implements Interceptor {

    private SecurityPreferences mSecurityPreferences;

    public AuthInterceptor(Context mContext) {
        mSecurityPreferences = new SecurityPreferences(mContext);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request().newBuilder();
        String token = mSecurityPreferences.get(PERSON_TOKEN);
        requestBuilder.addHeader("Authorization", "Bearer " + token);
        return chain.proceed(requestBuilder.build());
    }
}