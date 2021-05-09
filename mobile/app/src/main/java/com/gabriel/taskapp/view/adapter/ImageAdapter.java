package com.gabriel.taskapp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gabriel.taskapp.R;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private final List<Integer> imageIds;

    public ImageAdapter(Context context, List<Integer> imageIds) {
        mContext = context;
        this.imageIds = imageIds;
    }

    @Override
    public int getCount() {
        return imageIds.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (convertView == null) {
            gridView = inflater.inflate(R.layout.grid_image_layout, null);
            ImageView imageView = gridView.findViewById(R.id.grid_item_image);
            imageView.setImageResource(imageIds.get(position));
        } else {
            gridView = convertView;
        }
        return gridView;
    }

}