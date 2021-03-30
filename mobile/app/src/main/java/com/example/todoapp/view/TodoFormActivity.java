package com.example.todoapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todoapp.R;
import com.example.todoapp.service.model.TodoModel;
import com.example.todoapp.viewmodel.TodoFormViewModel;

public class TodoFormActivity extends AppCompatActivity implements View.OnClickListener {

    private TodoFormViewModel mViewModel;
    private TodoModel mTodo = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_form);

        mViewModel = new ViewModelProvider(this).get(TodoFormViewModel.class);
        setListeners();
        observe();
    }

    private void observe() {
        mViewModel.saveTodo.observe(this, success -> {
            if(success){
                Toast.makeText(getApplicationContext(), R.string.success, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
            }
            finish();
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.button_save){
            EditText edit_description = findViewById(R.id.edit_description);
            String description = edit_description.getText().toString();
            if(mTodo == null) {
                mViewModel.saveOrUpdate(null, description, false);
            } else {
                mViewModel.saveOrUpdate(mTodo.getId(), description, mTodo.getCompleted());
            }
        }
    }

    private void setListeners() {
        findViewById(R.id.button_save).setOnClickListener(this);
    }

}