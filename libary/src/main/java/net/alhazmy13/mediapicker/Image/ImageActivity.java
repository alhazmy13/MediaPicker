package net.alhazmy13.mediapicker.Image;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import net.alhazmy13.camerapicker.R;
import net.alhazmy13.mediapicker.Image.ImagePicker.ComperesLevel;
import net.alhazmy13.mediapicker.Image.ImagePicker.Extension;
import net.alhazmy13.mediapicker.Image.ImagePicker.Mode;
import net.alhazmy13.mediapicker.Utility;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alhazmy13 on 10/26/15.
 * MediaPicker
 */
public class ImageActivity extends AppCompatActivity {
    private static final String TAG = "ImageActivity";

    private final int CAMERA_REQUEST = 1888;
    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private final int REQUEST_CODE_SELECT_PHOTO = 43;

    private File destination;
    private ComperesLevel compressLevel;
    private Extension extension;
    private Uri mImageUri;
    private ImagePicker.Mode mode;
    private String directory;
    private int reqWidth;
    private int reqHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            compressLevel = (ComperesLevel) intent.getSerializableExtra(ImageTags.LEVEL);
            extension = (Extension) intent.getSerializableExtra(ImageTags.EXTENSION);
            mode = (Mode) intent.getSerializableExtra(ImageTags.MODE);
            directory = intent.getStringExtra(ImageTags.DIRECTORY);
            reqWidth = intent.getIntExtra(ImageTags.REQUESTED_WIDTH, 0);
            reqHeight = intent.getIntExtra(ImageTags.REQUESTED_HEIGHT, 0);
        }

        if (savedInstanceState == null) {
            pickImageWrapper();
        }
    }

    private void pickImage() {
        Utility.createFolder(directory);
        destination = new File(directory, Utility.getRandomString() + extension.getValue());
        switch (mode) {
            case CAMERA:
                startActivityFromCamera();
                break;
            case GALLERY:
                startActivityFromGallery();
                break;
            case CAMERA_AND_GALLERY:
                showFromCameraOrGalleryAlert();
                break;
        }
    }

    private void showFromCameraOrGalleryAlert() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.media_picker_select_from))
                .setPositiveButton(getString(R.string.media_picker_camera), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivityFromCamera();
                    }
                })
                .setNegativeButton(getString(R.string.media_picker_gallery), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivityFromGallery();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d(TAG, "onCancel: ");
                        finish();
                    }
                })
                .show();
    }

    private void startActivityFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        photoPickerIntent.setType("image/*");
        mImageUri = Uri.fromFile(destination);
        startActivityForResult(photoPickerIntent, REQUEST_CODE_SELECT_PHOTO);
    }

    private void startActivityFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mImageUri = Uri.fromFile(destination);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mImageUri != null) {
            outState.putString(ImageTags.CAMERA_IMAGE_URI, mImageUri.toString());
            outState.putInt(ImageTags.COMPRESS_LEVEL, compressLevel.getValue());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey(ImageTags.CAMERA_IMAGE_URI)) {
            mImageUri = Uri.parse(savedInstanceState.getString(ImageTags.CAMERA_IMAGE_URI));
            destination = new File(mImageUri.getPath());
            compressLevel = ComperesLevel.getEnum(savedInstanceState.getInt(ImageTags.COMPRESS_LEVEL));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    new CompressImageTask(destination.getAbsolutePath(), destination.getAbsolutePath(),
                            compressLevel.getValue(), reqWidth, reqHeight, ImageActivity.this).execute();
                } else {
                    finish();
                }

                break;
            case REQUEST_CODE_SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    String selectedImagePath = "";
                    try {
                        Uri selectedImage = data.getData();
                        selectedImagePath = Utility.getRealPathFromURI(this, selectedImage);
                        new CompressImageTask(selectedImagePath, destination.getAbsolutePath(),
                                compressLevel.getValue(), reqWidth, reqHeight, ImageActivity.this).execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    finish();
                }
        }
    }

    private void finishActivity(String path) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(ImagePicker.EXTRA_IMAGE_PATH, path);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void pickImageWrapper() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();

            final List<String> permissionsList = new ArrayList<String>();
            if (!addPermission(permissionsList, Manifest.permission.CAMERA))
                permissionsNeeded.add(getString(R.string.media_picker_camera));
            if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                permissionsNeeded.add(getString(R.string.media_picker_read_Write_external_storage));

            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    // Need Rationale
                    String message = getString(R.string.media_picker_you_need_to_grant_access_to) + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++)
                        message = message + ", " + permissionsNeeded.get(i);
                    showMessageOKCancel(message,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(ImageActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_PERMISSIONS);
                                }
                            });
                    return;
                }
                ActivityCompat.requestPermissions(ImageActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }

            pickImage();
        } else {
            pickImage();
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ImageActivity.this)
                .setMessage(message)
                .setPositiveButton(getString(R.string.media_picker_ok), okListener)
                .setNegativeButton(getString(R.string.media_picker_cancel), null)
                .create()
                .show();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ActivityCompat.checkSelfPermission(ImageActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(ImageActivity.this, permission))
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    pickImage();
                } else {
                    // Permission Denied
                    Toast.makeText(ImageActivity.this, getString(R.string.media_picker_some_permission_is_denied), Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private static class CompressImageTask extends AsyncTask<Void, Void, Void> {

        private final String mPath;
        private final String mDestinationPath;
        private final int mCompressLevel;
        private WeakReference<ImageActivity> mContext;
        private final int mReqWidth;
        private final int mReqHeight;

        public CompressImageTask(String sourcePath, String destinationPath, int compressLevel, int reqWidth, int reqHeight, ImageActivity context) {
            mPath = sourcePath;
            mDestinationPath = destinationPath;
            mCompressLevel = compressLevel;
            mContext = new WeakReference<>(context);
            mReqWidth = reqWidth;
            mReqHeight = reqHeight;

            Log.d(TAG, "CompressImageTask(): " + "path = [" + sourcePath + "], destinationPath = [" + destinationPath + "], compressLevel = ["
                    + compressLevel + "]");
        }

        @Override
        protected Void doInBackground(Void... params) {

            File file = new File(mPath);
            File destinationFile = new File(mDestinationPath);
            try {
                Utility.compressAndRotateIfNeeded(file, destinationFile, mCompressLevel, mReqWidth, mReqHeight);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ImageActivity context = mContext.get();
            if (context != null) {
                context.finishActivity(mDestinationPath);
            }
        }
    }

    public static Intent getCallingIntent(Context activity, Extension extension,
                                          ComperesLevel compressLevel, Mode mode,
                                          String directory, int reqWidth, int reqHeight) {
        Intent intent = new Intent(activity, ImageActivity.class);
        intent.putExtra(ImageTags.EXTENSION, extension);
        intent.putExtra(ImageTags.LEVEL, compressLevel);
        intent.putExtra(ImageTags.MODE, mode);
        intent.putExtra(ImageTags.DIRECTORY, directory);
        intent.putExtra(ImageTags.REQUESTED_WIDTH, reqWidth);
        intent.putExtra(ImageTags.REQUESTED_HEIGHT, reqHeight);
        return intent;
    }

}