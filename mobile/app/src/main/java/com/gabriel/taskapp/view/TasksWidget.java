package com.gabriel.taskapp.view;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.gabriel.taskapp.R;
import com.gabriel.taskapp.service.repositories.local.SecurityPreferences;
import com.gabriel.taskapp.service.services.TasksWidgetRemoteViewsService;

import static com.gabriel.taskapp.service.constants.PersonConstants.PERSON_TOKEN;
import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_TAG;

public class TasksWidget extends AppWidgetProvider {
    private static Integer tasksSize = null;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SecurityPreferences sharedPreferences = new SecurityPreferences(context);
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(
                    context.getPackageName(),
                    R.layout.tasks_widget
            );

            if (!sharedPreferences.get(PERSON_TOKEN).equals("")) {
                if(tasksSize == 0){
                    views.setViewVisibility(R.id.text_view_widget_no_tasks, View.VISIBLE);
                } else {
                    views.setViewVisibility(R.id.text_view_widget_no_tasks, View.GONE);
                }
                Intent intent = new Intent(context, TasksWidgetRemoteViewsService.class);
                views.setRemoteAdapter(R.id.widgetListView, intent);
                views.setViewVisibility(R.id.not_authenticated, View.GONE);
            } else {
                views.setViewVisibility(R.id.not_authenticated, View.VISIBLE);
            }

            Intent listIntent = new Intent(context, SignInActivity.class);
            PendingIntent listPendingIntent = PendingIntent.getActivity(context, 0, listIntent, 0);
            views.setOnClickPendingIntent(R.id.button_widget_list, listPendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetListView);
        }
    }

    public static void sendRefreshBroadcast(Context context, Integer newTasksSize) {
        tasksSize = newTasksSize;
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, TasksWidget.class));
        context.sendBroadcast(intent);
    }

    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);

        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            Log.d(TASK_TAG, "onReceive: " + "updateActiononReceive");
            updateWidgets(context);
        }

    }

    public void updateWidgets(Context context) {
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        int[] ids = widgetManager.getAppWidgetIds(new ComponentName(context, TasksWidget.class));
        onUpdate(context, widgetManager, ids);
    }
}