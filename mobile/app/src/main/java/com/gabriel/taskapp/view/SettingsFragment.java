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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.taskapp.BuildConfig;
import com.gabriel.taskapp.R;
import com.gabriel.taskapp.service.constants.DatabaseConstants;
import com.gabriel.taskapp.service.listeners.TaskListener;
import com.gabriel.taskapp.service.models.local.LocalTaskModel;
import com.gabriel.taskapp.service.repositories.local.SecurityPreferences;
import com.gabriel.taskapp.service.services.SyncService;
import com.gabriel.taskapp.view.adapter.TaskAdapter;
import com.gabriel.taskapp.viewmodel.TaskListViewModel;
import com.gabriel.taskapp.viewmodel.TaskSettingsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.TimeUnit;

import static com.gabriel.taskapp.service.constants.SyncConstants.BUNDLED_LISTENER;
import static com.gabriel.taskapp.service.constants.SyncConstants.LAST_SYNC_SHARED_PREFERENCE;
import static com.gabriel.taskapp.service.constants.SyncConstants.SYNC_SERVICE_MESSAGE;
import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_TAG;
import static io.realm.Realm.getApplicationContext;

public class SettingsFragment extends Fragment implements View.OnClickListener{

    TaskSettingsViewModel taskSettingsViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        taskSettingsViewModel = new ViewModelProvider(this).get(TaskSettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        observer();
        setListeners(root);
        return root;
    }


    private void setListeners(View root) {
        root.findViewById(R.id.button_settings_log_out).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void observer() {
        taskSettingsViewModel.loggedOut.observe(getViewLifecycleOwner(), isLoggedOut -> {
           if(isLoggedOut){
               Intent intent = new Intent(this.getContext(), SignInActivity.class);
               startActivity(intent);
               getActivity().finish();
           }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button_settings_log_out) {
            taskSettingsViewModel.logout();
        }
    }
}