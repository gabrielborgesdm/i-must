package com.gabriel.taskapp.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gabriel.taskapp.R;
import com.gabriel.taskapp.service.constants.DatabaseConstants;
import com.gabriel.taskapp.service.model.local.TaskModel;
import com.gabriel.taskapp.viewmodel.TaskFormViewModel;

import java.util.ArrayList;
import java.util.Locale;

public class TaskFormActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private TaskFormViewModel mViewModel;
    private TaskModel mTodo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_form);

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
                mViewModel.saveOrUpdate(mTodo.getId(), description, mTodo.getCompleted(), 0, false);
            }
        }
        if(id == R.id.speech_to_text_button){
            speak();
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
        findViewById(R.id.speech_to_text_button).setOnClickListener(this);
    }

    private void speak(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speak_task_description));
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    EditText edit_description = findViewById(R.id.edit_description);
                    edit_description.setText(result.get(0));
                }
        }
    }
}