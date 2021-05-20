package com.gabriel.taskapp.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
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
import com.gabriel.taskapp.service.model.local.LocalTaskModel;
import com.gabriel.taskapp.service.repository.ImageRepository;
import com.gabriel.taskapp.view.adapter.ImageAdapter;
import com.gabriel.taskapp.viewmodel.TaskFormViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_IMAGE;
import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_TAG;

public class TaskFormActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private static TaskFormViewModel mViewModel;
    private LocalTaskModel mTodo = null;
    private ImageRepository mImageRepository;

    private static int mYear;
    private static int mMonth;
    private static int mDayOfMonth;
    private static int MAX_IMAGES_ALLOWED = 3;
    int SELECT_IMAGE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_form);
        mImageRepository = ImageRepository.getRepository(this);
        mViewModel = new ViewModelProvider(this).get(TaskFormViewModel.class);
        setListeners();
        observe();
        try {
            loadData();
        } catch (JSONException e) {
            e.printStackTrace();
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
        if (requestCode == SELECT_IMAGE) {
            assert data != null;
            Uri selectedImageUri = data.getData();
            if (null != selectedImageUri) {
                mViewModel.uploadImage(selectedImageUri);
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
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mViewModel.updateDatetime(mYear, mMonth, mDayOfMonth, hourOfDay, minute);

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button_form_more_options) {
            if (!mViewModel.isCollapsed.getValue()
                    && ((mViewModel.dueDate.getValue() != null && mViewModel.dueDate.getValue() != "")
                    || mViewModel.localImagesPath.size() > 0)) {
                Log.d(TASK_TAG, "onClick: " + mViewModel.dueDate.getValue());
                new AlertDialog.Builder(this)
                        .setTitle(R.string.modal_collapse_title)
                        .setMessage(R.string.modal_collapse_message)
                        .setPositiveButton(R.string.modal_collapse_button_collapse, (dialog, which) -> {
                            mViewModel.toggleCollapsed();
                        })
                        .setNeutralButton(R.string.cancel, null)
                        .show();
            } else {
                mViewModel.toggleCollapsed();
            }
        }
        if (id == R.id.button_form_upload_image) {
            if (mViewModel.localImagesPath != null && mViewModel.localImagesPath.size() >= MAX_IMAGES_ALLOWED) {
                Toast.makeText(this, getString(R.string.cannot_add_more_images_to_task), Toast.LENGTH_SHORT).show();
                return;
            }
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(i, "Select Image"), SELECT_IMAGE);
        }
        if (id == R.id.text_input_form_date) {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        }
        if (id == R.id.button_save) {
            EditText edit_description = findViewById(R.id.task_form_description);
            EditText edit_date = findViewById(R.id.text_input_form_date);
            String description = edit_description.getText().toString();
            String date = edit_date.getText().toString();

            String todoId = null;
            Boolean todoCompleted = false;
            if (mTodo != null) {
                todoId = mTodo.getId();
                todoCompleted = mTodo.getCompleted();
            }
            mViewModel.saveOrUpdate(todoId, description, todoCompleted, date, mViewModel.localImagesPath, 0, false);
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

        mViewModel.imagePaths.observe(this, imagePaths -> {
            if (imagePaths.size() > 0) {
                findViewById(R.id.text_view_no_image).setVisibility(View.GONE);
            } else {
                findViewById(R.id.text_view_no_image).setVisibility(View.VISIBLE);
            }
            ImageAdapter adapter = new ImageAdapter(this, imagePaths);
            GridView grid = findViewById(R.id.grid_view_form_images);
            grid.setAdapter(adapter);

            grid.setOnItemLongClickListener((parent, view, position, id) -> {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.modal_remove_media_title)
                        .setMessage(R.string.modal_remove_media_message)
                        .setPositiveButton(R.string.modal_remove_media_button_remove, (dialog, which) -> {
                            mViewModel.removeImage(position);
                        })

                        .setNeutralButton(R.string.cancel, null)
                        .show();
                return true;
            });
            grid.setOnItemClickListener((parent, view, position, id) -> {
                Intent intent = new Intent(this, FullscreenActivity.class);
                intent.putExtra(TASK_IMAGE, imagePaths.get(position));
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

    private void loadData() throws JSONException {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mTodo = new LocalTaskModel();
            mTodo.setId(bundle.getString(DatabaseConstants.TASK.ID));
            mTodo.setCompleted(bundle.getBoolean(DatabaseConstants.TASK.COMPLETED));

            EditText editDescription = findViewById(R.id.task_form_description);
            editDescription.setText(bundle.getString(DatabaseConstants.TASK.DESCRIPTION));

            String datetime = bundle.getString(DatabaseConstants.TASK.DATETIME);
            ArrayList<String> imagePaths = bundle.getStringArrayList(DatabaseConstants.TASK.IMAGES_PATHS);
            Boolean isCollapsed = true;

            if (datetime != null) {
                Log.d(TASK_TAG, "loadData: " + datetime);
                EditText editDate = findViewById(R.id.text_input_form_date);
                editDate.setText(datetime);
                isCollapsed = false;
            }

            if (imagePaths.size() > 0) {
                mViewModel.loadImages(imagePaths);
                isCollapsed = false;
            }

            if (!isCollapsed) {
                mViewModel.toggleCollapsed();
            }

        }
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


}