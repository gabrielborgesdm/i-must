package com.example.todoapp.service.repository;

import com.example.todoapp.service.HeaderModel;
import com.example.todoapp.service.listener.ApiListener;
import com.example.todoapp.service.repository.remote.PersonService;
import com.example.todoapp.service.repository.remote.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.todoapp.service.constants.PersonConstants.PERSON_EMAIL;
import static com.example.todoapp.service.constants.PersonConstants.PERSON_PASSWORD;
import static com.example.todoapp.service.constants.PersonConstants.PERSON_USER;

public class PersonRepository {

    private PersonService mRemote =  RetrofitClient.createService(PersonService.class);

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
            public void onResponse(Call<HeaderModel> call, Response<HeaderModel> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<HeaderModel> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }
}
