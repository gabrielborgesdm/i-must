package com.gabriel.taskapp.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.gabriel.taskapp.R;
import com.gabriel.taskapp.service.repositories.ImageRepository;
import com.github.chrisbanes.photoview.PhotoView;

import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_IMAGE;

public class FullscreenActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
        ImageRepository imageRepository = ImageRepository.getRepository(this);

        Bitmap bitmapImage = imageRepository.getImage(getIntent().getStringExtra(TASK_IMAGE));

        PhotoView image_view_fullscreen;
        Button button_fullscreen_close;

        image_view_fullscreen = findViewById(R.id.image_view_fullscreen);
        button_fullscreen_close = findViewById(R.id.button_fullscreen_close);

        button_fullscreen_close.setOnClickListener(v -> FullscreenActivity.this.finish());

        image_view_fullscreen.setImageBitmap(bitmapImage);
    }


}