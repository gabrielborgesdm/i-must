package com.gabriel.i_must.view.viewholder;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.i_must.R;
import com.gabriel.i_must.service.listeners.TaskListener;
import com.gabriel.i_must.service.models.local.LocalTaskModel;
import com.gabriel.i_must.view.FullscreenActivity;
import com.gabriel.i_must.view.adapter.ImageAdapter;

import java.util.ArrayList;

import static com.gabriel.i_must.service.constants.TaskConstants.TASK_IMAGE;

public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TaskListener mListener;
    private LocalTaskModel mTask;
    private ImageView mButtonEdit;
    private ImageView mButtonMenu;
    private ImageView mButtonComplete;
    private ImageView mButtonRemove;
    private ImageView mCollapse;
    private TextView mDescription;
    private TextView mDatetime;
    private GridView mImageGrid;
    Boolean mIsCollapsed = true;

    public TaskViewHolder(@NonNull View itemView, TaskListener listener) {
        super(itemView);
        mListener = listener;
    }


    public void bind(LocalTaskModel task) {
        setVariables(task);
        setListeners();
        setViewImages(task);
        setDatetime(task);
        setCompleted(task);
        mDescription.setText(task.getDescription());
    }

    private void setVariables(LocalTaskModel task) {
        mTask = task;
        mButtonEdit = itemView.findViewById(R.id.image_view_row_task_edit);
        mButtonComplete = itemView.findViewById(R.id.button_complete_task);
        mButtonRemove = itemView.findViewById(R.id.image_view_row_task_remove);
        mButtonMenu = itemView.findViewById(R.id.image_view_row_task_options);
        mCollapse = itemView.findViewById(R.id.image_view_row_task_collapse);
        mDescription = itemView.findViewById(R.id.text_view_row_task_description);
        mDatetime = itemView.findViewById(R.id.text_view_row_task_date);
        mImageGrid = itemView.findViewById(R.id.grid_view_row_task_images);
    }

    private void setListeners() {
        mButtonMenu.setOnClickListener(this);
        mButtonEdit.setOnClickListener(this);
        mButtonComplete.setOnClickListener(this);
        mButtonRemove.setOnClickListener(this);
        mCollapse.setOnClickListener(this);
    }

    private void setViewImages(LocalTaskModel task) {
        GridView grid = itemView.findViewById(R.id.grid_view_row_task_images);
        if (task.getImagePaths() != null && task.getImagePaths().size() > 0) {
            mCollapse.setVisibility(View.VISIBLE);
            if(!mIsCollapsed){
                toggleCollapse();
            }
            ArrayList<String> imagePaths = task.getImagePaths();
            ImageAdapter adapter = new ImageAdapter(itemView.getContext(), imagePaths);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener((parent, view, position, id) -> {
                Intent intent = new Intent(itemView.getContext(), FullscreenActivity.class);
                intent.putExtra(TASK_IMAGE, imagePaths.get(position));
                itemView.getContext().startActivity(intent);
            });
        } else {
            mCollapse.setVisibility(View.INVISIBLE);
            grid.setVisibility(View.GONE);
        }
    }

    private void setDatetime(LocalTaskModel task) {
        if (task.getDatetime().length() > 0) {
            mDatetime.setText(task.getDatetime());
        }
    }

    private void setCompleted(LocalTaskModel task) {
        if (!task.getCompleted()) {
            mButtonComplete.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_baseline_crop_square_24));
        } else {
            mDescription.setPaintFlags(mDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        handleVisibilityClicks(id);
        if(id == R.id.image_view_row_task_collapse){
            toggleCollapse();
        }
    }

    private void handleVisibilityClicks(int id) {
        if (id == R.id.image_view_row_task_edit) {
            mListener.onEdit(mTask);
            changeTaskOptionsVisibility(false);
        } else if (id == R.id.button_complete_task) {
            mListener.onComplete(mTask);
        } else if (id == R.id.image_view_row_task_remove) {
            showRemoveDialog();
        } else if (id == R.id.image_view_row_task_options) {
            if (mButtonEdit.getVisibility() == View.VISIBLE) {
                changeTaskOptionsVisibility(false);
            } else {
                changeTaskOptionsVisibility(true);
            }

        }
    }

    private void changeTaskOptionsVisibility(Boolean isVisible) {
        if (isVisible) {
            mButtonEdit.setVisibility(View.VISIBLE);
            mButtonRemove.setVisibility(View.VISIBLE);
        } else {
            mButtonEdit.setVisibility(View.INVISIBLE);
            mButtonRemove.setVisibility(View.INVISIBLE);
        }
    }

    private void showRemoveDialog() {
        new AlertDialog.Builder(itemView.getContext())
                .setTitle(R.string.todo_removal)
                .setMessage(R.string.want_to_remove)
                .setPositiveButton(R.string.remove, (dialog, which) -> {
                    mListener.onDelete(mTask);
                    changeTaskOptionsVisibility(false);
                })
                .setNeutralButton(R.string.cancel, (dialog, which) -> {
                    changeTaskOptionsVisibility(false);
                })
                .show();
    }

    private void toggleCollapse() {
        mIsCollapsed = !mIsCollapsed;
        if(mIsCollapsed){
            mImageGrid.setVisibility(View.GONE);
            mCollapse.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_baseline_arrow_right_24));
        } else {
            mCollapse.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_baseline_arrow_drop_down_24));
            mImageGrid.setVisibility(View.VISIBLE);

        }
    }
}
