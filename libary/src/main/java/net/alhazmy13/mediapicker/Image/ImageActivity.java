package net.alhazmy13.mediapicker.Image;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import net.alhazmy13.mediapicker.Image.Filters.BitmapFilter;
import net.alhazmy13.mediapicker.Image.Filters.Filter;
import net.alhazmy13.mediapicker.Utility;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Alhazmy13 on 10/26/15.
 */
public class ImageActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private File destination;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private int compressLevel,filterType;
    private String extension;
    private static final String TAG = "ImageActivity";
    private Uri mImageUri;
    //// TODO: 10/28/15  fix the bug on pick multi image
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called with: " + "savedInstanceState = [" + savedInstanceState + "]");
        compressLevel=getIntent().getIntExtra("level", 100);
        filterType=getIntent().getIntExtra("filter", Filter.DEFAULT);
        extension=getIntent().getStringExtra("extension");
        pickImageWrapper();
        Log.d(TAG, "onCreate() returned: " );

    }


    private void pickImage(){
        Log.d(TAG, "pickImage() called with: " + "");
        Utility.createFolder(ImagePicker.directory);
        destination = new  File(ImagePicker.directory,Utility.getRandomString()+extension);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mImageUri = Uri.fromFile(destination);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(intent, CAMERA_REQUEST);
        Log.d(TAG, "pickImage() returned: " );
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mImageUri != null) {
            outState.putString("cameraImageUri", mImageUri.toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("cameraImageUri")) {
            mImageUri = Uri.parse(savedInstanceState.getString("cameraImageUri"));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                Log.d(TAG, "onActivityResult() called with: " + "requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");

                Bitmap bitmap=BitmapFactory.decodeFile(destination.getAbsolutePath());
                bitmap=BitmapFilter.applyStyle(bitmap,filterType);
                OutputStream os = new BufferedOutputStream(new FileOutputStream(destination));
                Log.d(TAG, "Bitmap");
                bitmap.compress(Bitmap.CompressFormat.JPEG, compressLevel, os);
                ImagePicker.onImagePicked.OnImageSet(destination.getAbsolutePath());
                os.close();
                Log.d(TAG, "onActivityResult() returned: ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        finish();
    }



    private void pickImageWrapper() {
        Log.d(TAG, "pickImageWrapper() called with: " + "");
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();

            final List<String> permissionsList = new ArrayList<String>();
            if (!addPermission(permissionsList, Manifest.permission.CAMERA))
                permissionsNeeded.add("Camera");
            if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                permissionsNeeded.add("Read & Write External Storage");

            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    // Need Rationale
                    String message = "You need to grant access to " + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++)
                        message = message + ", " + permissionsNeeded.get(i);
                    showMessageOKCancel(message,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(ImageActivity.this,permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_PERMISSIONS);
                                }
                            });
                    return;
                }
                ActivityCompat.requestPermissions(ImageActivity.this,permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }

            pickImage();
        }else{
            pickImage();
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ImageActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ActivityCompat.checkSelfPermission(ImageActivity.this,permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(ImageActivity.this,permission))
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    // All Permissions Granted
                    pickImage();
                } else {
                    // Permission Denied
                    Toast.makeText(ImageActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}