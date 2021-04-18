package com.gabriel.taskapp.view;

import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.taskapp.R;
import com.gabriel.taskapp.service.constants.DatabaseConstants;
import com.gabriel.taskapp.service.model.local.TaskModel;
import com.gabriel.taskapp.service.services.SyncService;
import com.gabriel.taskapp.view.adapter.TaskAdapter;
import com.gabriel.taskapp.service.listener.TaskListener;
import com.gabriel.taskapp.viewmodel.TaskListViewModel;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_TAG;

public class TaskListFragment extends Fragment {

    TaskListViewModel taskListViewModel;
    TaskAdapter mTaskAdapter = new TaskAdapter();
    TaskAdapter mCompletedAdapter = new TaskAdapter();
    private SyncService mService;
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
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        taskListViewModel = new ViewModelProvider(this).get(TaskListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_todo_list, container, false);

        RecyclerView todoRecycler = root.findViewById(R.id.todo_open_view);
        todoRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        todoRecycler.setAdapter(mTaskAdapter);
        mTaskAdapter.attachListener(mListener);

        RecyclerView completedRecycler = root.findViewById(R.id.todo_completed_view);
        completedRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        completedRecycler.setAdapter(mCompletedAdapter);
        mCompletedAdapter.attachListener(mListener);

        observer();
        bindService();

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
        taskListViewModel.getBinder().observe(getViewLifecycleOwner(), myBinder -> {
            if(myBinder != null) {
                mService = myBinder.getService();
                mService.startSyncing();
                taskListViewModel.load();
            }
        });

        taskListViewModel.todoList.observe(getViewLifecycleOwner(), list -> {
            mTaskAdapter.updateTodo(list);
        });

        taskListViewModel.completedList.observe(getViewLifecycleOwner(), list -> {
            mCompletedAdapter.updateTodo(list);
        });
    }

    private void bindService(){
        Intent intent = new Intent(getContext(), SyncService.class);
        getActivity().bindService(intent, taskListViewModel.getServiceConnection(), BIND_AUTO_CREATE);
    }

}