package com.gabriel.taskapp.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.taskapp.R;
import com.gabriel.taskapp.service.constants.DatabaseConstants;
import com.gabriel.taskapp.service.listener.TaskListener;
import com.gabriel.taskapp.service.model.local.TaskModel;
import com.gabriel.taskapp.service.repository.local.SecurityPreferences;
import com.gabriel.taskapp.service.services.SyncService;
import com.gabriel.taskapp.view.adapter.TaskAdapter;
import com.gabriel.taskapp.viewmodel.TaskListViewModel;

import java.util.concurrent.TimeUnit;

import static com.gabriel.taskapp.service.constants.SyncConstants.BUNDLED_LISTENER;
import static com.gabriel.taskapp.service.constants.SyncConstants.LAST_SYNC_SHARED_PREFERENCE;
import static com.gabriel.taskapp.service.constants.SyncConstants.SYNC_SERVICE_MESSAGE;
import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_TAG;

public class TaskListFragment extends Fragment {

    TaskListViewModel taskListViewModel;
    TaskAdapter mTaskAdapter = new TaskAdapter();
    TaskAdapter mCompletedAdapter = new TaskAdapter();
    private SecurityPreferences mSharedPreferences;

    TaskListener mListener = new TaskListener() {
        @Override
        public void onEdit(TaskModel task) {
            Intent intent = new Intent(getContext(), TaskFormActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(DatabaseConstants.TASK.ID, task.getId());
            bundle.putString(DatabaseConstants.TASK.DESCRIPTION, task.getDescription());
            bundle.putBoolean(DatabaseConstants.TASK.COMPLETED, task.isCompleted());
            intent.putExtras(bundle);
            startActivity(intent);
        }

        @Override
        public void onDelete(TaskModel task) {
            taskListViewModel.delete(task);
            taskListViewModel.load();
        }

        @Override
        public void onComplete(TaskModel task) {
            taskListViewModel.complete(task);
            taskListViewModel.load();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        taskListViewModel = new ViewModelProvider(this).get(TaskListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_todo_list, container, false);

        mSharedPreferences = new SecurityPreferences(getContext());

        RecyclerView todoRecycler = root.findViewById(R.id.todo_open_view);
        todoRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        todoRecycler.setAdapter(mTaskAdapter);
        mTaskAdapter.attachListener(mListener);

        RecyclerView completedRecycler = root.findViewById(R.id.todo_completed_view);
        completedRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        completedRecycler.setAdapter(mCompletedAdapter);
        mCompletedAdapter.attachListener(mListener);

        observer();
        syncTasks();

        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        taskListViewModel.load();
    }

    private void observer() {
        taskListViewModel.todoList.observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                Log.d(TASK_TAG, "todo: " + list.size());
            }

            mTaskAdapter.updateTodo(list);
        });

        taskListViewModel.completedList.observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                Log.d(TASK_TAG, "completed: " + list.size());
            }
            mCompletedAdapter.updateTodo(list);
        });
    }

    private void syncTasks() {
        Long lastSync = mSharedPreferences.getLong(LAST_SYNC_SHARED_PREFERENCE);
        Long differenceInMillis = System.currentTimeMillis() - lastSync;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(differenceInMillis);
        if (minutes > 2) {
            startSyncService();
        }
    }

    private void startSyncService() {
        Intent serviceIntent = new Intent(getContext(), SyncService.class);
        serviceIntent.putExtra("logName", "MAIN_ACTIVITY");
        serviceIntent.putExtra(BUNDLED_LISTENER, new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                if (resultCode == Activity.RESULT_OK) {
                    taskListViewModel.load();
                } else {
                    Toast.makeText(getContext(), SYNC_SERVICE_MESSAGE, Toast.LENGTH_SHORT).show();
                }
            }
        });
        getActivity().startForegroundService(serviceIntent);
    }

}