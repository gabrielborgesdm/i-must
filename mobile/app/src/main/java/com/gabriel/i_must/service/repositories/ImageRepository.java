package com.gabriel.i_must.service.repositories;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import com.gabriel.i_must.service.repositories.local.SecurityPreferences;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static com.gabriel.i_must.service.constants.PersonConstants.PERSON_EMAIL;
import static com.gabriel.i_must.service.constants.TaskConstants.TASK_TAG;

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

    public File getImageFile(String imageFilePath) {
        ContextWrapper cw = new ContextWrapper(mContext);
        File directory = cw.getDir(IMAGE_DIRECTORIES, Context.MODE_PRIVATE);
        File file = new File(directory, imageFilePath);
        if (!file.exists()) {
            return null;
        }
        return file;
    }

    private File createNewFile(String imageName){
        ContextWrapper cw = new ContextWrapper(mContext);
        File directory = cw.getDir(IMAGE_DIRECTORIES, Context.MODE_PRIVATE);

        return new File(directory, imageName + IMAGE_FORMAT);
    }

    public String writeImage(Uri imageUri){
        if(isBusy) return null;
        setBusy(true);
        Boolean error = false;
        String imageName = getImageName();
        File file = createNewFile(imageName);
        if (!file.exists()) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                Bitmap image = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imageUri);
                Integer orientation = getFileOrientation(imageUri);
                if(orientation != null) {
                    image = rotateBitmap(image, orientation);
                }
                image = this.resizeBitmap(image, IMAGE_MAX_LENGTH);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                image.recycle();
            } catch (java.io.IOException e) {
                e.printStackTrace();
                error = true;
            }
        }
        setBusy(false);
        return  error ? null : imageName + IMAGE_FORMAT;
    }

    public String writeBitmapImage(Bitmap bitmapImage){
        if(isBusy) return null;
        setBusy(true);
        Boolean error = false;
        String imageName = getImageName();
        File file = createNewFile(imageName);
        if (!file.exists()) {
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            }
        }
        setBusy(false);
        return error ? null : imageName + IMAGE_FORMAT;
    }

    private String getImageName(){
        SecurityPreferences sharedPreferences = new SecurityPreferences(mContext);
        String email = sharedPreferences.get(PERSON_EMAIL);
        return email.hashCode() + "" + System.currentTimeMillis();
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
    }

    public Bitmap getImage(String imageFilePath) {
        File file = getImageFile(imageFilePath);
        if(file == null) return  null;
        Bitmap image = BitmapFactory.decodeFile(file.getAbsolutePath());
        return image;
    }

    public Bitmap resizeBitmap(Bitmap source, int maxLength) {
        try {
            if (source.getHeight() >= source.getWidth()) {
                int targetHeight = maxLength;
                if (source.getHeight() <= targetHeight) {
                    return source;
                }

                double aspectRatio = (double) source.getWidth() / (double) source.getHeight();
                int targetWidth = (int) (targetHeight * aspectRatio);

                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                return result;
            } else {
                int targetWidth = maxLength;

                if (source.getWidth() <= targetWidth) {
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

    public Integer getFileOrientation(Uri uri){
        int orientation = 0;
        try {
            InputStream input = mContext.getContentResolver().openInputStream(uri);
            if (input != null){
                ExifInterface exif = new ExifInterface(input);
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                input.close();
            }
        } catch (Exception e) {
            Log.d(TASK_TAG, "getFileOrientation: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return orientation;
    }

    public Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Log.d(TASK_TAG, "rotateBitmap: " + orientation);
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

    public Bitmap convertBase64ToBitmap(String base64String) {
        final String pureBase64Encoded = base64String.substring(base64String.indexOf(",")  + 1);
        final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public ArrayList<String> decodeImagesAndGetPaths(ArrayList<String> images){
        ArrayList<String> imagePaths = new ArrayList<>();
        for(int i = 0; i < images.size(); i++){
            String imageBase64 = images.get(i);
            Bitmap bitmapImage = convertBase64ToBitmap(imageBase64);
            String imagePath = writeBitmapImage(bitmapImage);
            if(imagePath != null){
                imagePaths.add(imagePath);
            }
        }
        return imagePaths;
    }

    public void deleteImages(ArrayList<String> imagePaths) {
        if(imagePaths == null || imagePaths.size() == 0) return;
        Log.d(TASK_TAG, "deleteImages: " + Arrays.toString(imagePaths.toArray()));
        imagePaths.forEach(path -> {
            File imageFile = getImageFile(path);
            if(imageFile != null){
                imageFile.delete();
            }
        });
    }

    public void deleteAllImages() {
        ContextWrapper cw = new ContextWrapper(mContext);
        File directory = cw.getDir(IMAGE_DIRECTORIES, Context.MODE_PRIVATE);
        if (directory.isDirectory())
            for (File child : Objects.requireNonNull(directory.listFiles())) {
                Log.d(TASK_TAG, "deleteAllImages: " + child.getAbsolutePath());
                child.delete();
            }

    }
}
