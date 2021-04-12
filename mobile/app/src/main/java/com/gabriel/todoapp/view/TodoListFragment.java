package com.gabriel.todoapp.view;

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

import com.gabriel.todoapp.R;
import com.gabriel.todoapp.service.constants.DatabaseConstants;
import com.gabriel.todoapp.service.model.local.TodoModel;
import com.gabriel.todoapp.view.adapter.TodoAdapter;
import com.gabriel.todoapp.service.listener.TodoListener;
import com.gabriel.todoapp.viewmodel.TodoListViewModel;

import static com.gabriel.todoapp.service.constants.TodoConstants.TODO_TAG;

public class TodoListFragment extends Fragment {

    TodoListViewModel todoListViewModel;
    TodoAdapter mTodoAdapter = new TodoAdapter();
    TodoAdapter mCompletedAdapter = new TodoAdapter();
    TodoListener mListener = new TodoListener() {
        @Override
        public void onEdit(TodoModel todo) {
            Intent intent = new Intent(getContext(), TodoFormActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(DatabaseConstants.TODO.ID, todo.getId());
            bundle.putString(DatabaseConstants.TODO.DESCRIPTION, todo.getDescription());
            bundle.putBoolean(DatabaseConstants.TODO.COMPLETED, todo.getCompleted());
            intent.putExtras(bundle);
            startActivity(intent);
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