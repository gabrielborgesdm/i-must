package com.example.todoapp.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.todoapp.service.HeaderModel;
import com.example.todoapp.service.listener.ApiListener;
import com.example.todoapp.service.repository.PersonRepository;

public class SignInViewModel extends AndroidViewModel {
    Context mContext;
    PersonRepository mPersonRepository = new PersonRepository();

    public SignInViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
    }

    public void doLogin(String email, String password) {
        mPersonRepository.login(email, password, new ApiListener() {

            @Override
            public void onSuccess(HeaderModel model) {
                String s = "";
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}
