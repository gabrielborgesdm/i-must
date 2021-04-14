package com.gabriel.todoapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gabriel.todoapp.service.repository.SyncRepository;
import com.gabriel.todoapp.view.TodoFormActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.gabriel.todoapp.service.repository.local.RealmHelpers.startRealmContext;

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
                startActivity(new Intent(getApplicationContext(), TodoFormActivity.class)));

        SyncRepository sync = new SyncRepository(getApplicationContext());
        sync.syncTasks();
    }
}