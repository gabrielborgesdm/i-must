package com.example.todoapp.service.repository;

import android.content.Context;

import com.example.todoapp.R;
import com.example.todoapp.service.HeaderModel;
import com.example.todoapp.service.constants.PersonConstants;
import com.example.todoapp.service.listener.ApiListener;
import com.example.todoapp.service.repository.remote.PersonService;
import com.example.todoapp.service.repository.remote.RetrofitClient;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.todoapp.service.constants.PersonConstants.PERSON_EMAIL;
import static com.example.todoapp.service.constants.PersonConstants.PERSON_PASSWORD;
import static com.example.todoapp.service.constants.PersonConstants.PERSON_USER;

public class PersonRepository {

    private PersonService mRemote =  RetrofitClient.createService(PersonService.class);
    private Context mContext;

    public  PersonRepository(Context context){
        mContext = context;
    }

    public void login(String email, String password, ApiListener listener){
        JSONObject body = new JSONObject();
        JSONObject user = new JSONObject();
        try {
            user.put(PERSON_EMAIL, email);
            user.put(PERSON_PASSWORD, password);
            body.put(PERSON_USER, user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), body.toString());
        Call<HeaderModel> call = mRemote.login(requestBody);
        call.enqueue(new Callback<HeaderModel>() {
            @Override
            public void onResponse(@NotNull Call<HeaderModel> call, @NotNull Response<HeaderModel> response) {
               String s = "";
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(mContext.getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void onFailure(@NotNull Call<HeaderModel> call, Throwable t) {
                listener.onFailure(mContext.getString(R.string.something_went_wrong));
            }
        });
    }
}
