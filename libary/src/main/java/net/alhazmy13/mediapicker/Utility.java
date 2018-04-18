package net.alhazmy13.mediapicker;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
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
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        os.close();
        return path;
    }

    public static String getRandomString() {
        return UUID.randomUUID().toString();
    }

    public static void createFolder(String path) {
        try {
            File dir = new File(path.substring(0, path.lastIndexOf("/")));
            Log.d(TAG, "createFolder: " + dir.exists());
            if (!dir.exists()) {
                dir.mkdirs();
            }
        } catch (Exception ex) {
            Log.w(TAG, "creating file error: ", ex);
        }

    }

    @WorkerThread
    public static void compressAndRotateIfNeeded(File sourceFile, File destinationFile, int value, int reqWidth, int reqHeight) throws IOException {

        String path = sourceFile.getAbsolutePath();

        BitmapFactory.Options bounds = new BitmapFactory.Options();
        Bitmap bm;
        if (reqHeight != 0 && reqWidth != 0) {
            bounds.inJustDecodeBounds = true;
            bm = BitmapFactory.decodeFile(path, bounds);
            bounds.inSampleSize = calculateInSampleSize(bounds, reqWidth, reqHeight);
            bounds.inJustDecodeBounds = false;
        }
        bm = BitmapFactory.decodeFile(path, bounds);

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

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @WorkerThread
    private static int getCameraPhotoOrientation(File file) throws IOException {
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
            default:
                break;
        }

        return rotate;
    }

}
