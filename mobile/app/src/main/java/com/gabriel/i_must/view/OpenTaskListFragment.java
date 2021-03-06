package com.gabriel.i_must.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.i_must.R;
import com.gabriel.i_must.service.constants.DatabaseConstants;
import com.gabriel.i_must.service.listeners.TaskListener;
import com.gabriel.i_must.service.models.local.LocalTaskModel;
import com.gabriel.i_must.service.repositories.SyncRepository;
import com.gabriel.i_must.service.services.SyncService;
import com.gabriel.i_must.view.adapter.TaskAdapter;
import com.gabriel.i_must.viewmodel.TaskListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.gabriel.i_must.service.constants.SyncConstants.BUNDLED_LISTENER;
import static com.gabriel.i_must.service.constants.SyncConstants.SYNC_SERVICE_MESSAGE;
import static io.realm.Realm.getApplicationContext;

public class OpenTaskListFragment extends Fragment {

    TaskListViewModel taskListViewModel;
    TaskAdapter mTaskAdapter = new TaskAdapter();
    SyncRepository mSyncRepository;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        taskListViewModel = new ViewModelProvider(this).get(TaskListViewModel.class);
        mSyncRepository = new SyncRepository(this.getContext());
        View root = inflater.inflate(R.layout.fragment_open_task_list, container, false);

        RecyclerView todoRecycler = root.findViewById(R.id.todo_open_view);
        todoRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        todoRecycler.setAdapter(mTaskAdapter);
        mTaskAdapter.attachListener(mListener);

        observer();
        syncTasks();

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), TaskFormActivity.class)));

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

            if (list == null || list.size() == 0) {
                TextView textNoTodo = getActivity().findViewById(R.id.text_no_todo);
                textNoTodo.setVisibility(View.VISIBLE);
            } else {
                TextView textNoTodo = getActivity().findViewById(R.id.text_no_todo);
                textNoTodo.setVisibility(View.GONE);
            }

            mTaskAdapter.updateTodo(list);
        });
    }

    private void syncTasks() {
        if (mSyncRepository.checkShouldSync()) {
            startSyncService();
        }
    }

    private void startSyncService() {
        Intent serviceIntent = new Intent(getContext(), SyncService.class);
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

    TaskListener mListener = new TaskListener() {
        @Override
        public void onEdit(LocalTaskModel task) {
            Intent intent = new Intent(getContext(), TaskFormActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(DatabaseConstants.TASK.ID, task.getId());
            bundle.putString(DatabaseConstants.TASK.DESCRIPTION, task.getDescription());
            bundle.putBoolean(DatabaseConstants.TASK.COMPLETED, task.getCompleted());
            bundle.putString(DatabaseConstants.TASK.DATETIME, task.getDatetime());
            if (task.getImagePaths() != null) {
                bundle.putStringArrayList(DatabaseConstants.TASK.IMAGES_PATHS, task.getImagePaths());
            }
            intent.putExtras(bundle);
            startActivity(intent);
        }

        @Override
        public void onDelete(LocalTaskModel task) {
            taskListViewModel.delete(task);
            taskListViewModel.load();
        }

        @Override
        public void onComplete(LocalTaskModel task) {
            taskListViewModel.complete(task);
            taskListViewModel.load();
        }
    };
}