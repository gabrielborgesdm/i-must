package com.gabriel.taskapp.service.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.gabriel.taskapp.view.adapter.TasksWidgetRemoteViewsFactory;

public class TasksWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TasksWidgetRemoteViewsFactory(this.getApplicationContext());
    }
}