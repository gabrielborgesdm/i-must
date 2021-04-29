package com.gabriel.taskapp.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.annotation.RequiresApi;

import com.gabriel.taskapp.MainActivity;
import com.gabriel.taskapp.R;
import com.gabriel.taskapp.service.model.local.TaskModel;
import com.gabriel.taskapp.service.repository.local.TaskRepository;

import java.util.ArrayList;
import java.util.List;

import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_FILTER_OPEN;

public class TasksWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<TaskModel> mTodoList = new ArrayList();
    private TaskRepository  mRepository = TaskRepository.getRealmRepository();

    public TasksWidgetRemoteViewsFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        mTodoList = mRepository.getAllFiltered(TASK_FILTER_OPEN);
    }

    @Override
    public void onDataSetChanged() {
        mTodoList = mRepository.getAllFiltered(TASK_FILTER_OPEN);
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return mTodoList == null ? 0 : mTodoList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if(!checkPositionIsValid(position)) {
            return  null;
        }
        RemoteViews remoteView = new RemoteViews(mContext.getPackageName(), R.layout.row_widget_task);
        remoteView.setTextViewText(R.id.text_widget_description, mTodoList.get(position).getDescription());

        Intent intent = new Intent(mContext, MainActivity.class);
        remoteView.setOnClickFillInIntent(R.id.row_widget_task, intent);
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        if(!checkPositionIsValid(position)) {
            return  0;
        }
        return (mTodoList.get(position).getId()).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private boolean checkPositionIsValid(int position){
        if(position < 0 || position > mTodoList.size()) {
            return  false;
        }
        return  true;
    }
}
