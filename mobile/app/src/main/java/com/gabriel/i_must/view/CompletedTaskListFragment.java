package com.gabriel.i_must.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.i_must.R;
import com.gabriel.i_must.service.listeners.CompletedTaskListener;
import com.gabriel.i_must.service.models.local.LocalTaskModel;
import com.gabriel.i_must.view.adapter.CompletedTaskAdapter;
import com.gabriel.i_must.viewmodel.TaskListViewModel;

public class CompletedTaskListFragment extends Fragment {

    TaskListViewModel taskListViewModel;
    CompletedTaskAdapter mCompletedAdapter = new CompletedTaskAdapter();
    CompletedTaskListener mListener = new CompletedTaskListener() {

        @Override
        public void onDelete(LocalTaskModel task) {
            taskListViewModel.delete(task);
            taskListViewModel.load();
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        taskListViewModel = new ViewModelProvider(this).get(TaskListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_completed_task_list, container, false);
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

        taskListViewModel.completedList.observe(getViewLifecycleOwner(), list -> {

            if (list == null || list.size() == 0) {
                TextView textNoCompleted = getActivity().findViewById(R.id.text_no_completed);
                textNoCompleted.setVisibility(View.VISIBLE);
            } else {
                TextView textNoCompleted = getActivity().findViewById(R.id.text_no_completed);
                textNoCompleted.setVisibility(View.GONE);
            }
            mCompletedAdapter.updateTodo(list);
        });
    }
}