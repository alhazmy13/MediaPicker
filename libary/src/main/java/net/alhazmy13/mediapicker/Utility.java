package net.alhazmy13.mediapicker;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.WorkerThread;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Alhazmy13 on 11/10/15.
 */
public class Utility {
    private static final String TAG = "Utility";

    public static String compressImage(String path) throws IOException {
        File file = new File(path);
        Bitmap bitmap=BitmapFactory.decodeFile(path);
        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        os.close();
        return path;
    }

    public static String getRandomString(){
       // return SystemClock.currentThreadTimeMillis()+"";
        return UUID.randomUUID().toString();
    }

    public static void createFolder(String path){
        try {
            File dir = new File(path.substring(0,path.lastIndexOf("/")));
            Log.d(TAG, "createFolder: "+dir.exists());
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }catch (Exception ex){
            Log.w(TAG, "creating file error: ", ex);
        }

    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @WorkerThread
    public static void compressAndRotateIfNeeded(File sourceFile, File destinationFile, int value) throws IOException {

        String path = sourceFile.getAbsolutePath();

        Log.d("compress", "file path: " + path);

        BitmapFactory.Options bounds = new BitmapFactory.Options();

        // TODO: Support sample decoding with user width and height preferences
//        bounds.inSampleSize = 4;

        Bitmap bm = BitmapFactory.decodeFile(path, bounds);

        if (bm == null) {
            Log.d("compress", "bitmap is null");
            return;
        }

        int rotationAngle = getCameraPhotoOrientation(sourceFile);

        Matrix matrix = new Matrix();
        matrix.postRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight,
                matrix, true);

        FileOutputStream fos = new FileOutputStream(destinationFile.getAbsoluteFile());
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, value, fos);
        fos.flush();
        fos.close();
    }

    @WorkerThread
    public static int getCameraPhotoOrientation(File file) throws IOException {
        ExifInterface exif = new ExifInterface(
                file.getAbsolutePath());
        int orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);

        int rotate = 0;

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
        }

        return rotate;
    }

}
