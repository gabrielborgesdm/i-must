package com.gabriel.i_must.viewmodel;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gabriel.i_must.R;
import com.gabriel.i_must.service.constants.APIConstants;
import com.gabriel.i_must.service.listeners.APIListener;
import com.gabriel.i_must.service.models.remote.HeaderModel;
import com.gabriel.i_must.service.repositories.remote.PersonRepository;
import com.gabriel.i_must.service.repositories.local.SecurityPreferences;

import static com.gabriel.i_must.service.constants.APIConstants.API_MESSAGE;
import static com.gabriel.i_must.service.constants.APIConstants.API_OPERATION_EXECUTED;
import static com.gabriel.i_must.service.constants.APIConstants.API_SUCCESS;
import static com.gabriel.i_must.service.constants.PersonConstants.PERSON_EMAIL;
import static com.gabriel.i_must.service.constants.PersonConstants.PERSON_TOKEN;

public class SignInViewModel extends AndroidViewModel {
    PersonRepository mPersonRepository = new PersonRepository(getApplication().getApplicationContext());
    private final SecurityPreferences mSharedPreferences;

    private final MutableLiveData<Bundle> mLogin = new MutableLiveData<Bundle>();
    public LiveData<Bundle> login = mLogin;

    private final MutableLiveData<Boolean> mLoggedUser = new MutableLiveData<Boolean>();
    public LiveData<Boolean> loggedUser = mLoggedUser;

    public SignInViewModel(@NonNull Application application) {
        super(application);
        mSharedPreferences = new SecurityPreferences(application.getApplicationContext());
    }

    public void doLogin(String email, String password) {
        mPersonRepository.login(email, password, new APIListener<HeaderModel>() {

            @Override
            public void onSuccess(HeaderModel model) {
                Bundle login = new Bundle();
                if (model.status.equals(API_OPERATION_EXECUTED)) {
                    mSharedPreferences.store(PERSON_TOKEN, model.token);
                    mSharedPreferences.store(PERSON_EMAIL, model.email);
                    login.putBoolean(API_SUCCESS, true);
                    login.putString(API_MESSAGE, getApplication().getApplicationContext().getString(R.string.person_found_with_success));
                } else if (model.status.equals(APIConstants.API_NOT_FOUND)) {
                    login.putBoolean(API_SUCCESS, false);
                    login.putString(API_MESSAGE, getApplication().getApplicationContext().getString(R.string.person_not_found));
                } else {
                    login.putBoolean(API_SUCCESS, false);
                    login.putString(API_MESSAGE, getApplication().getApplicationContext().getString(R.string.something_went_wrong));
                }

                mLogin.setValue(login);
            }

            @Override
            public void onFailure(String message) {
                Bundle login = new Bundle();
                login.putBoolean(API_SUCCESS, false);
                login.putString(APIConstants.API_MESSAGE, message);
                mLogin.setValue(login);
            }
        });
    }

    public void verifyLoggedUser() {
        String token = mSharedPreferences.get(PERSON_TOKEN);
        mLoggedUser.setValue(!token.equals(""));
    }
}
