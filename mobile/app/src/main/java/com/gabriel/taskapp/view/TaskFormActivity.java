package com.gabriel.taskapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gabriel.taskapp.R;
import com.gabriel.taskapp.service.constants.DatabaseConstants;
import com.gabriel.taskapp.service.model.local.TaskModel;
import com.gabriel.taskapp.viewmodel.TaskFormViewModel;

public class TaskFormActivity extends AppCompatActivity implements View.OnClickListener {

    private TaskFormViewModel mViewModel;
    private TaskModel mTodo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_form);

        mViewModel = new ViewModelProvider(this).get(TaskFormViewModel.class);
        setListeners();
        observe();
        loadData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.button_save){
            EditText edit_description = findViewById(R.id.edit_description);
            String description = edit_description.getText().toString();
            if(mTodo == null) {
                mViewModel.saveOrUpdate(null, description, false, 0, false);
            } else {
                mViewModel.saveOrUpdate(mTodo.getId(), description, mTodo.isCompleted(), 0, false);
            }
        }
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

    private void loadData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            mTodo = new TaskModel();
            mTodo.setId(bundle.getString(DatabaseConstants.TASK.ID));
            mTodo.setDescription(bundle.getString(DatabaseConstants.TASK.DESCRIPTION));
            mTodo.setCompleted(bundle.getBoolean(DatabaseConstants.TASK.COMPLETED));
            EditText editDescription = findViewById(R.id.edit_description);
            editDescription.setText(mTodo.getDescription());
        }
    }

    private void setListeners() {
        findViewById(R.id.button_save).setOnClickListener(this);
    }

}