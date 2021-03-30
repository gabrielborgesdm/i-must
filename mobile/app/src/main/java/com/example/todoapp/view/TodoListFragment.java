package com.example.todoapp.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.service.model.TodoModel;
import com.example.todoapp.view.adapter.TodoAdapter;
import com.example.todoapp.view.listener.TodoListener;
import com.example.todoapp.viewmodel.TodoListViewModel;

import java.util.List;

import static com.example.todoapp.service.constants.TodoConstants.TODO_TAG;

public class TodoListFragment extends Fragment {

    TodoListViewModel todoListViewModel;
    TodoAdapter mTodoAdapter = new TodoAdapter();
    TodoAdapter mCompletedAdapter = new TodoAdapter();
    TodoListener mListener = new TodoListener() {
        @Override
        public void onEdit(TodoModel todo) {

        }

        @Override
        public void onDelete(TodoModel todo) {
            todoListViewModel.delete(todo);
            todoListViewModel.load();
        }

        @Override
        public void onComplete(TodoModel todo) {
            todoListViewModel.complete(todo);
            todoListViewModel.load();
        }
    };

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        todoListViewModel = new ViewModelProvider(this).get(TodoListViewModel.class);
        Log.d(TODO_TAG, "onCreateView: teste");
        View root = inflater.inflate(R.layout.fragment_todo_list, container, false);

        RecyclerView todoRecycler = root.findViewById(R.id.todo_open_view);
        todoRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        todoRecycler.setAdapter(mTodoAdapter);
        mTodoAdapter.attachListener(mListener);

        RecyclerView completedRecycler = root.findViewById(R.id.todo_completed_view);
        completedRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        completedRecycler.setAdapter(mCompletedAdapter);
        mCompletedAdapter.attachListener(mListener);

        observer();

        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TODO_TAG, "getAll: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        todoListViewModel.load();
    }

    private void observer() {
        todoListViewModel.todoList.observe(getViewLifecycleOwner(), list -> {
            mTodoAdapter.updateTodo(list);
        });

        todoListViewModel.completedList.observe(getViewLifecycleOwner(), list -> {
            mCompletedAdapter.updateTodo(list);
        });
    }
}