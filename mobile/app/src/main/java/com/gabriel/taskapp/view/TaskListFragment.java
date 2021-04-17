package com.gabriel.taskapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.taskapp.R;
import com.gabriel.taskapp.service.constants.DatabaseConstants;
import com.gabriel.taskapp.service.model.local.TaskModel;
import com.gabriel.taskapp.view.adapter.TaskAdapter;
import com.gabriel.taskapp.service.listener.TaskListener;
import com.gabriel.taskapp.viewmodel.TaskListViewModel;

import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_TAG;

public class TaskListFragment extends Fragment {

    TaskListViewModel taskListViewModel;
    TaskAdapter mTaskAdapter = new TaskAdapter();
    TaskAdapter mCompletedAdapter = new TaskAdapter();
    TaskListener mListener = new TaskListener() {
        @Override
        public void onEdit(TaskModel todo) {
            Intent intent = new Intent(getContext(), TaskFormActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(DatabaseConstants.TASK.ID, todo.getId());
            bundle.putString(DatabaseConstants.TASK.DESCRIPTION, todo.getDescription());
            bundle.putBoolean(DatabaseConstants.TASK.COMPLETED, todo.getCompleted());
            intent.putExtras(bundle);
            startActivity(intent);
        }

        @Override
        public void onDelete(TaskModel todo) {
            taskListViewModel.delete(todo);
            taskListViewModel.load();
        }

        @Override
        public void onComplete(TaskModel todo) {
            taskListViewModel.complete(todo);
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
            mTaskAdapter.updateTodo(list);
        });

        taskListViewModel.completedList.observe(getViewLifecycleOwner(), list -> {
            mCompletedAdapter.updateTodo(list);
        });
    }
}