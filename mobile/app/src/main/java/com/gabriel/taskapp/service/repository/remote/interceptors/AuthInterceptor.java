package com.gabriel.taskapp.service.repository.remote.interceptors;

import android.content.Context;
import android.util.Log;

import com.gabriel.taskapp.service.repository.local.SecurityPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.gabriel.taskapp.service.constants.PersonConstants.PERSON_TOKEN;
import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_TAG;

public class AuthInterceptor implements Interceptor {

    private SecurityPreferences mSecurityPreferences;

    public AuthInterceptor(Context mContext) {
        mSecurityPreferences = new SecurityPreferences(mContext);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request().newBuilder();
        String token = mSecurityPreferences.get(PERSON_TOKEN);
        Log.d(TASK_TAG, "intercept: " + token);
        requestBuilder.addHeader("Authorization", "Bearer " + token);
        return chain.proceed(requestBuilder.build());
    }
}