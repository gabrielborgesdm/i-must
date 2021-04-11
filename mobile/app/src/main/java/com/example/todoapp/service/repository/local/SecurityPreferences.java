package com.example.todoapp.service.repository.local;

import android.content.Context;
import android.content.SharedPreferences;

public class SecurityPreferences {
    private SharedPreferences mPreferences;

    SecurityPreferences(Context context){
        mPreferences = context.getSharedPreferences("taskShared", Context.MODE_PRIVATE);
    }

    public void store(String key, String value) {
        mPreferences.edit().putString(key, value).apply();
    }

    public void remove(String key) {
        mPreferences.edit().remove(key).apply();
    }

    public String get(String key) {
        String value = mPreferences.getString(key, "");
        return value;
    }

}