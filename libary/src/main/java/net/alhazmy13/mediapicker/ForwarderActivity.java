package net.alhazmy13.mediapicker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

/**
 * Created by Alhazmy13 on 10/26/15.
 */
public class ForwarderActivity extends Activity {
    private static final int CAMERA_REQUEST = 1888;
    String path;
    File destination;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        destination = new   File(Environment.getExternalStorageDirectory(),getRandonString()+".jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    private String getRandonString(){
        return UUID.randomUUID().toString();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                FileInputStream in = new FileInputStream(destination);
                BitmapFactory.Options options = new BitmapFactory.Options();
                //  options.inSampleSize = 10; //Downsample 10x
                Log.d("PP", " bitmap factory==========" + destination.getAbsolutePath());
                CameraPicker.onImagePicked.OnImagePicked(destination.getAbsolutePath().toString());
                Bitmap user_picture_bmp = BitmapFactory.decodeStream(in, null, options);
                //  userImage.setImageBitmap(user_picture_bmp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}