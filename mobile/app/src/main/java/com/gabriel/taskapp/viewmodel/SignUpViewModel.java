package com.gabriel.taskapp.viewmodel;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gabriel.taskapp.R;
import com.gabriel.taskapp.service.listeners.APIListener;
import com.gabriel.taskapp.service.models.remote.SignUpModel;
import com.gabriel.taskapp.service.repositories.remote.PersonRepository;
import com.gabriel.taskapp.service.repositories.local.SecurityPreferences;

import static com.gabriel.taskapp.service.constants.APIConstants.API_MESSAGE;
import static com.gabriel.taskapp.service.constants.APIConstants.API_OPERATION_EXECUTED;
import static com.gabriel.taskapp.service.constants.APIConstants.API_SUCCESS;

public class SignUpViewModel extends AndroidViewModel {

    PersonRepository mPersonRepository = new PersonRepository(getApplication().getApplicationContext());
    private final SecurityPreferences mSharedPreferences;

    private final MutableLiveData<Bundle> mSignUp = new MutableLiveData<Bundle>();
    public LiveData<Bundle> signUp = mSignUp;


    public SignUpViewModel(@NonNull Application application) {
        super(application);
        mSharedPreferences = new SecurityPreferences(application.getApplicationContext());
    }

    public void signUp(String name, String email, String password) {
        mPersonRepository.signUp(name, email, password, new APIListener<SignUpModel>() {

            @Override
            public void onSuccess(SignUpModel model) {
                Bundle signUp = new Bundle();
                if (model.status.equals(API_OPERATION_EXECUTED)) {
                    signUp.putBoolean(API_SUCCESS, true);
                    signUp.putString(API_MESSAGE, model.message);
                } else {
                    signUp.putBoolean(API_SUCCESS, false);
                    signUp.putString(API_MESSAGE, getApplication().getApplicationContext().getString(R.string.something_went_wrong));
                }

                mSignUp.setValue(signUp);
            }

            @Override
            public void onFailure(String message) {
                Bundle signUp = new Bundle();
                signUp.putBoolean(API_SUCCESS, false);
                signUp.putString(API_MESSAGE, message);
                mSignUp.setValue(signUp);
            }
        });
    }
}
