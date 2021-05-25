package com.gabriel.taskapp.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gabriel.taskapp.R;
import com.gabriel.taskapp.service.repositories.ImageRepository;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private final ArrayList<String> imagePaths;
    private ImageRepository mImageRepository;

    public ImageAdapter(Context context, ArrayList<String> imagePaths) {
        mContext = context;
        this.imagePaths = imagePaths;
        mImageRepository = ImageRepository.getRepository(mContext);
    }

    @Override
    public int getCount() {
        return imagePaths.size();
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
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Bitmap image = null;
            image = mImageRepository.getImage(imagePaths.get(position));
            imageView.setImageBitmap(image);
        } else {
            gridView = convertView;
        }
        return gridView;
    }

}