package com.example.todoapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.todoapp.R;
import com.example.todoapp.viewmodel.SignUpViewModel;

public class SignUpActivity extends AppCompatActivity {
    SignUpViewModel mViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

    }
}