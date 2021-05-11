package com.gabriel.taskapp.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.gabriel.taskapp.R;
import com.gabriel.taskapp.service.constants.DatabaseConstants;
import com.gabriel.taskapp.service.model.local.TaskModel;
import com.gabriel.taskapp.view.adapter.ImageAdapter;
import com.gabriel.taskapp.viewmodel.TaskFormViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_IMAGE;
import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_TAG;

public class TaskFormActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private static TaskFormViewModel mViewModel;
    private TaskModel mTodo = null;

    private static int mYear;
    private static int mMonth;
    private static int mDayOfMonth;

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
        if (id == R.id.button_form_more_options) {
            mViewModel.toggleCollapsed();
        }
        if (id == R.id.button_form_upload_image) {
            mViewModel.uploadImage();
        }
        if (id == R.id.text_input_form_date) {
            Log.d(TASK_TAG, "onClick: teste");
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        }
        if (id == R.id.button_save) {
            EditText edit_description = findViewById(R.id.task_form_description);
            String description = edit_description.getText().toString();
            if (mTodo == null) {
                mViewModel.saveOrUpdate(null, description, false, 0, false);
            } else {
                mViewModel.saveOrUpdate(mTodo.getId(), description, mTodo.getCompleted(), 0, false);
            }
        }
    }

    private void observe() {
        mViewModel.isCollapsed.observe(this, isCollapsed -> {
            MaterialButton moreOptionsButton = findViewById(R.id.button_form_more_options);
            ConstraintLayout constraintLayout = findViewById(R.id.constraint_layout_form_task);
            if (isCollapsed) {
                moreOptionsButton.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_right_24));
                constraintLayout.setVisibility(View.GONE);
            } else {
                moreOptionsButton.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_drop_down_24));
                constraintLayout.setVisibility(View.VISIBLE);
            }
        });

        mViewModel.imageIds.observe(this, imageIds -> {
            if (imageIds.size() > 0) {
                findViewById(R.id.text_view_no_image).setVisibility(View.GONE);
            } else {
                findViewById(R.id.text_view_no_image).setVisibility(View.VISIBLE);
            }
            ImageAdapter adapter = new ImageAdapter(this, imageIds);
            GridView grid = findViewById(R.id.grid_view_form_images);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener((parent, view, position, id) -> {
                Intent intent = new Intent(this, FullscreenActivity.class);
                final ImageView image = view.findViewById(R.id.grid_item_image);
                image.setDrawingCacheEnabled(true);
                image.buildDrawingCache(true);
                Bitmap bitmapImage = Bitmap.createBitmap(image.getDrawingCache());
                image.setDrawingCacheEnabled(false); // clear drawing cache
                Bundle extras = new Bundle();
                extras.putParcelable(TASK_IMAGE, bitmapImage);
                intent.putExtras(extras);
                startActivity(intent);
            });
        });

        mViewModel.dueDate.observe(this, dueDatetime -> {
            TextInputEditText inputFormDate = findViewById(R.id.text_input_form_date);
            inputFormDate.setText(dueDatetime);
        });

        mViewModel.saveTodo.observe(this, success -> {
            if (success) {
                Toast.makeText(getApplicationContext(), R.string.success, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
            }
            finish();
        });


    }

    private void loadData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mTodo = new TaskModel();
            mTodo.setId(bundle.getString(DatabaseConstants.TASK.ID));
            mTodo.setDescription(bundle.getString(DatabaseConstants.TASK.DESCRIPTION));
            mTodo.setCompleted(bundle.getBoolean(DatabaseConstants.TASK.COMPLETED));
            EditText editDescription = findViewById(R.id.task_form_description);
            editDescription.setText(mTodo.getDescription());
        }
    }

    private void setListeners() {
        findViewById(R.id.button_save).setOnClickListener(this);
        findViewById(R.id.button_form_more_options).setOnClickListener(this);
        findViewById(R.id.text_input_form_date).setOnClickListener(this);
        findViewById(R.id.button_form_upload_image).setOnClickListener(this);
        TextInputLayout textDescription = findViewById(R.id.task_form_text_layout);
        textDescription.setEndIconOnClickListener(view -> {
            speak();
        });
    }

    private void speak() {
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
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                EditText edit_description = findViewById(R.id.task_form_description);
                edit_description.setText(result.get(0));
            }
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, dayOfMonth);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mYear = year;
            mMonth = month;
            mDayOfMonth = dayOfMonth;
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public @NotNull Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mViewModel.updateDatetime(mYear, mMonth, mDayOfMonth, hourOfDay, minute);

        }
    }
}