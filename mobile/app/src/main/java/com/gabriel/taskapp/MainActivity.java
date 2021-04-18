package com.gabriel.taskapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gabriel.taskapp.service.repository.SyncRepository;
import com.gabriel.taskapp.service.services.SyncService;
import com.gabriel.taskapp.view.TaskFormActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.gabriel.taskapp.service.repository.local.RealmHelpers.startRealmContext;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startRealmContext(getApplicationContext());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), TaskFormActivity.class)));


    }
}