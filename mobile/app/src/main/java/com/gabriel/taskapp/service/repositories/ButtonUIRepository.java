package com.gabriel.taskapp.service.repositories;

import com.gabriel.taskapp.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ButtonUIRepository {
    Map<Integer, String> descriptionMap = new HashMap<>();

    public void startButtonLoading(MaterialButton button) {
        button.setEnabled(false);
        storeButtonDescription(button);
        button.setText(R.string.loading);
    }

    public void stopButtonLoading(MaterialButton button) {
        button.setEnabled(true);
        String buttonDescription = getButtonDescription(button);
        if(buttonDescription != null){
            button.setText(buttonDescription);
        }
    }

    private void storeButtonDescription(MaterialButton button) {
        int buttonId = button.getId();
        String buttonDescription = button.getText().toString();
        descriptionMap.put(buttonId, buttonDescription);
    }

    private String getButtonDescription(MaterialButton button) {
        int buttonId = button.getId();
        return descriptionMap.get(buttonId);
    }
}
