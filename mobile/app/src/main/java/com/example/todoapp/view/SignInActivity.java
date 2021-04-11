package com.example.todoapp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.todoapp.R;
import com.example.todoapp.viewmodel.SignInViewModel;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    SignInViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        setListeners();
        observe();
        loadData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.button_sign_in){
            handleLogin();
        }
        if(id == R.id.text_dont_have_an_account_sign_up){
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        }
    }

    private void handleLogin() {
        EditText edit_email = findViewById(R.id.email_sign_in);
        String email = edit_email.getText().toString();

        EditText edit_password = findViewById(R.id.password_sign_in);
        String password = edit_password.getText().toString();
        mViewModel.doLogin(email, password);
    }

    private void loadData() {
    }

    private void observe() {
    }

    private void setListeners() {
        findViewById(R.id.button_sign_in).setOnClickListener(this);
        findViewById(R.id.text_dont_have_an_account_sign_up).setOnClickListener(this);
    }


}