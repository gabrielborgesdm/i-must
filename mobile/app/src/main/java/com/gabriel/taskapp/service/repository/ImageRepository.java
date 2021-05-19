package com.gabriel.taskapp.service.repository;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import com.gabriel.taskapp.service.repository.local.SecurityPreferences;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static com.gabriel.taskapp.service.constants.PersonConstants.PERSON_EMAIL;
import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_TAG;

public class ImageRepository {

    private static ImageRepository mRepository = null;
    private static Context mContext;
    private boolean isBusy = false;
    private final String IMAGE_DIRECTORIES = "images";
    private final String IMAGE_FORMAT = ".jpg";
    private final int IMAGE_MAX_LENGTH = 720;


    private ImageRepository(Context context) {
        mContext = context;
    }

    public static ImageRepository getRepository(Context context){
        if(mRepository == null){
            mRepository = new ImageRepository(context);
        }
        return  mRepository;
    }

    private File getImageFile(String imageFilePath) {
        ContextWrapper cw = new ContextWrapper(mContext);
        File directory = cw.getDir(IMAGE_DIRECTORIES, Context.MODE_PRIVATE);
        File file = new File(directory, imageFilePath);
        if (!file.exists()) {
            return null;
        }
        return file;
    }

    public String writeImage(Uri imageUri){
        if(isBusy) return null;
        setBusy(true);
        ContextWrapper cw = new ContextWrapper(mContext);
        File directory = cw.getDir(IMAGE_DIRECTORIES, Context.MODE_PRIVATE);
        String imageName = getImageName();
        File file = new File(directory, imageName + IMAGE_FORMAT);
        if (!file.exists()) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                Bitmap image = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imageUri);
                image = this.resizeBitmap(image, IMAGE_MAX_LENGTH);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                image.recycle();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        setBusy(false);
        return imageName + IMAGE_FORMAT;
    }

    private String getImageName(){
        SecurityPreferences sharedPreferences = new SecurityPreferences(mContext);
        String email = sharedPreferences.get(PERSON_EMAIL);
        return email.hashCode() + "" + System.currentTimeMillis();
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
    }

    public Bitmap getImage(String imageFilePath) {
        File file = getImageFile(imageFilePath);
        if(file == null) return  null;
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    public Bitmap resizeBitmap(Bitmap source, int maxLength) {
        try {
            if (source.getHeight() >= source.getWidth()) {
                int targetHeight = maxLength;
                if (source.getHeight() <= targetHeight) { // if image already smaller than the required height
                    return source;
                }

                double aspectRatio = (double) source.getWidth() / (double) source.getHeight();
                int targetWidth = (int) (targetHeight * aspectRatio);

                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                return result;
            } else {
                int targetWidth = maxLength;

                if (source.getWidth() <= targetWidth) { // if image already smaller than the required height
                    return source;
                }

                double aspectRatio = ((double) source.getHeight()) / ((double) source.getWidth());
                int targetHeight = (int) (targetWidth * aspectRatio);

                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                return result;

            }
        }
        catch (Exception e)
        {
            return source;
        }
    }

    public int getFileOrientation(String path){
        ExifInterface exif = null;
        int orientation = 0;
        try {
            exif = new ExifInterface(path);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

        } catch (Exception e) {
            Log.d(TASK_TAG, "getFileOrientation: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return orientation;
    }

    public Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] convertBitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public Bitmap convertByteArrayToBitmap(byte[] byteArray){
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public String convertFileToBase64(String imageFilePath) throws IOException {
        String base64Image = null;
        File file = getImageFile(imageFilePath);
        if(file != null) {
            try {
                byte[] fileContent = Files.readAllBytes(file.toPath());
                base64Image = Base64.encodeToString(fileContent, Base64.DEFAULT);
            } catch (IOException e) {
                throw new IllegalStateException("Could not read file " + file, e);
            }
        }

        return base64Image;
    }
}
