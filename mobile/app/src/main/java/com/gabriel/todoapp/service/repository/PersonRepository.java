package com.gabriel.todoapp.service.repository;

import android.content.Context;

import com.gabriel.todoapp.R;
import com.gabriel.todoapp.service.listener.APIListener;
import com.gabriel.todoapp.service.model.remote.HeaderModel;
import com.gabriel.todoapp.service.listener.APIListener;
import com.gabriel.todoapp.service.model.remote.SignUpModel;
import com.gabriel.todoapp.service.repository.remote.PersonService;
import com.gabriel.todoapp.service.repository.remote.RetrofitClient;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gabriel.todoapp.service.constants.PersonConstants.PERSON_EMAIL;
import static com.gabriel.todoapp.service.constants.PersonConstants.PERSON_NAME;
import static com.gabriel.todoapp.service.constants.PersonConstants.PERSON_PASSWORD;
import static com.gabriel.todoapp.service.constants.PersonConstants.PERSON_USER;

public class PersonRepository {

    private PersonService mRemote =  RetrofitClient.createService(PersonService.class);
    private Context mContext;

    public  PersonRepository(Context context){
        mContext = context;
    }

    public void login(String email, String password, APIListener listener){
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

    public void signUp(String name, String email, String password, APIListener listener){
        JSONObject body = new JSONObject();
        JSONObject user = new JSONObject();
        try {
            user.put(PERSON_NAME, name);
            user.put(PERSON_EMAIL, email);
            user.put(PERSON_PASSWORD, password);
            body.put(PERSON_USER, user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), body.toString());
        Call<SignUpModel> call = mRemote.signUp(requestBody);
        call.enqueue(new Callback<SignUpModel>() {
            @Override
            public void onResponse(@NotNull Call<SignUpModel> call, @NotNull Response<SignUpModel> response) {
                String s = "";
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(mContext.getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void onFailure(@NotNull Call<SignUpModel> call, Throwable t) {
                listener.onFailure(mContext.getString(R.string.something_went_wrong));
            }
        });
    }
}
