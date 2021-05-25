package com.gabriel.taskapp.service.repositories.remote;

import android.content.Context;

import com.gabriel.taskapp.R;
import com.gabriel.taskapp.service.listeners.APIListener;
import com.gabriel.taskapp.service.models.remote.HeaderModel;
import com.gabriel.taskapp.service.models.remote.SignUpModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gabriel.taskapp.service.constants.PersonConstants.PERSON_EMAIL;
import static com.gabriel.taskapp.service.constants.PersonConstants.PERSON_NAME;
import static com.gabriel.taskapp.service.constants.PersonConstants.PERSON_PASSWORD;
import static com.gabriel.taskapp.service.constants.PersonConstants.PERSON_USER;

public class PersonRepository {

    private final PersonService mRemote;
    private final Context mContext;

    public  PersonRepository(Context context){
        mContext = context;
        mRemote = RetrofitClient.createService(mContext, PersonService.class);
    }

    public void login(String email, String password, APIListener<HeaderModel> listener){
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

    public void signUp(String name, String email, String password, APIListener<SignUpModel> listener){
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
