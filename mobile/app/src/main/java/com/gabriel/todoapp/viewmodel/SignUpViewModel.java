package com.gabriel.todoapp.viewmodel;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gabriel.todoapp.R;
import com.gabriel.todoapp.service.constants.PersonConstants;
import com.gabriel.todoapp.service.listener.SignUpListener;
import com.gabriel.todoapp.service.model.remote.SignUpModel;
import com.gabriel.todoapp.service.repository.PersonRepository;
import com.gabriel.todoapp.service.repository.local.SecurityPreferences;

import static com.gabriel.todoapp.service.constants.PersonConstants.PERSON_MESSAGE;
import static com.gabriel.todoapp.service.constants.PersonConstants.PERSON_SUCCESS;

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
        mPersonRepository.signUp(name, email, password, new SignUpListener() {

            @Override
            public void onSuccess(SignUpModel model) {
                Bundle signUp = new Bundle();
                if(model.status.equals(PersonConstants.PERSON_OPERATION_EXECUTED)){
                    signUp.putBoolean(PERSON_SUCCESS, true);
                    signUp.putString(PERSON_MESSAGE, model.message);
                } else {
                    signUp.putBoolean(PERSON_SUCCESS, false);
                    signUp.putString(PERSON_MESSAGE, getApplication().getApplicationContext().getString(R.string.something_went_wrong));
                }

                mSignUp.setValue(signUp);
            }

            @Override
            public void onFailure(String message) {
                Bundle signUp = new Bundle();
                signUp.putBoolean(PERSON_SUCCESS, false);
                signUp.putString(PersonConstants.PERSON_MESSAGE, message);
                mSignUp.setValue(signUp);
            }
        });
    }
}
