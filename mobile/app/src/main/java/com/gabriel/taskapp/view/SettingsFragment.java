package com.gabriel.taskapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.gabriel.taskapp.R;
import com.gabriel.taskapp.service.repositories.ButtonUIRepository;
import com.gabriel.taskapp.viewmodel.SettingsViewModel;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    SettingsViewModel settingsViewModel;
    ButtonUIRepository mButtonRepository = new ButtonUIRepository();
    View mRoot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        mRoot = root;
        observer();
        setListeners();
        return root;
    }


    private void setListeners() {
        mRoot.findViewById(R.id.button_settings_log_out).setOnClickListener(this);
        mRoot.findViewById(R.id.button_settings_sync).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button_settings_log_out) {
            mButtonRepository.startButtonLoading(mRoot.findViewById(R.id.button_settings_log_out));
            settingsViewModel.logout();
        } else if (id == R.id.button_settings_sync) {
            mButtonRepository.startButtonLoading(mRoot.findViewById(R.id.button_settings_sync));
            settingsViewModel.syncTasks();
        }
    }

    private void observer() {
        settingsViewModel.isLoggedOut.observe(getViewLifecycleOwner(), isLoggedOut -> {
            if (isLoggedOut) {
                Intent intent = new Intent(this.getContext(), SignInActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
            mButtonRepository.stopButtonLoading(mRoot.findViewById(R.id.button_settings_log_out));
        });

        settingsViewModel.isSynced.observe(getViewLifecycleOwner(), isSynced -> {
            if (isSynced) {
                Intent intent = new Intent(this.getContext(), SignInActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
            mButtonRepository.stopButtonLoading(mRoot.findViewById(R.id.button_settings_sync));
        });
    }

}