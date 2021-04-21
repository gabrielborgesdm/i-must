package com.gabriel.taskapp.service.repository.local;

import android.content.Context;
import android.content.SharedPreferences;

public class SecurityPreferences {
    private final SharedPreferences mPreferences;

    public SecurityPreferences(Context context){
        mPreferences = context.getSharedPreferences("taskShared", Context.MODE_PRIVATE);
    }

    public void store(String key, String value) {
        mPreferences.edit().putString(key, value).apply();
    }

    public void remove(String key) {
        mPreferences.edit().remove(key).apply();
    }

    public String get(String key) {
        return mPreferences.getString(key, "");
    }

    public Long getLong(String key) {
        return mPreferences.getLong(key, 0L);
    }

    public void storeLong(String key, Long value) {
        mPreferences.edit().putLong(key, value).apply();
    }

}