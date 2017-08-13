package net.alhazmy13.mediapicker.Image;

import android.Manifest;
import android.annotation.TargetApi;
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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import net.alhazmy13.camerapicker.R;
import net.alhazmy13.mediapicker.FileProcessing;
import net.alhazmy13.mediapicker.Utility;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
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


    private File destination;
    private Uri mImageUri;
    private ImageConfig mImgConfig;
    private List<String> listOfImgs;

    public static Intent getCallingIntent(Context activity, ImageConfig imageConfig) {
        Intent intent = new Intent(activity, ImageActivity.class);
        intent.putExtra(ImageTags.Tags.IMG_CONFIG, imageConfig);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            mImgConfig = (ImageConfig) intent.getSerializableExtra(ImageTags.Tags.IMG_CONFIG);
        }

        if (savedInstanceState == null) {
            pickImageWrapper();
            listOfImgs = new ArrayList<>();
        }
        if (mImgConfig.debug)
            Log.d(ImageTags.Tags.TAG, mImgConfig.toString());
    }

    private void pickImage() {
        Utility.createFolder(mImgConfig.directory);
        destination = new File(mImgConfig.directory, Utility.getRandomString() + mImgConfig.extension.getValue());
        switch (mImgConfig.mode) {
            case CAMERA:
                startActivityFromCamera();
                break;
            case GALLERY:
                if (mImgConfig.allowMultiple)
                    startActivityFromGalleryMultiImg();
                else
                    startActivityFromGallery();
                break;
            case CAMERA_AND_GALLERY:
                showFromCameraOrGalleryAlert();
                break;
            default:
                break;
        }
    }

    private void showFromCameraOrGalleryAlert() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.media_picker_select_from))
                .setPositiveButton(getString(R.string.media_picker_camera), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mImgConfig.debug)
                            Log.d(ImageTags.Tags.TAG, "Alert Dialog - Start From Camera");
                        startActivityFromCamera();
                    }
                })
                .setNegativeButton(getString(R.string.media_picker_gallery), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mImgConfig.debug)
                            Log.d(ImageTags.Tags.TAG, "Alert Dialog - Start From Gallery");
                        if (mImgConfig.allowMultiple)
                            startActivityFromGalleryMultiImg();
                        else
                            startActivityFromGallery();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        if (mImgConfig.debug)
                            Log.d(ImageTags.Tags.TAG, "Alert Dialog - Canceled");
                        finish();
                    }
                })
                .show();
    }

    private void startActivityFromGallery() {
        mImgConfig.isImgFromCamera = false;
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ImageTags.IntentCode.REQUEST_CODE_SELECT_PHOTO);
        if (mImgConfig.debug)
            Log.d(ImageTags.Tags.TAG, "Gallery Start with Single Image mode");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void startActivityFromGalleryMultiImg() {
        mImgConfig.isImgFromCamera = false;
        Intent photoPickerIntent = new Intent();
        photoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Picture"), ImageTags.IntentCode.REQUEST_CODE_SELECT_MULTI_PHOTO);
        if (mImgConfig.debug)
            Log.d(ImageTags.Tags.TAG, "Gallery Start with Multiple Images mode");
    }

    private void startActivityFromCamera() {
        mImgConfig.isImgFromCamera = true;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mImageUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", destination);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), ImageTags.IntentCode.CAMERA_REQUEST);
        if (mImgConfig.debug)
            Log.d(ImageTags.Tags.TAG, "Camera Start");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mImageUri != null) {
            outState.putString(ImageTags.Tags.CAMERA_IMAGE_URI, mImageUri.toString());
            outState.putSerializable(ImageTags.Tags.IMG_CONFIG, mImgConfig);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(ImageTags.Tags.CAMERA_IMAGE_URI)) {
            mImageUri = Uri.parse(savedInstanceState.getString(ImageTags.Tags.CAMERA_IMAGE_URI));
            destination = new File(mImageUri.getPath());
            mImgConfig = (ImageConfig) savedInstanceState.getSerializable(ImageTags.Tags.IMG_CONFIG);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(ImageTags.Tags.TAG, "onActivityResult() called with: " + "requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ImageTags.IntentCode.CAMERA_REQUEST:
                    new CompressImageTask(destination.getAbsolutePath(), mImgConfig
                            , ImageActivity.this).execute();

                    break;
                case ImageTags.IntentCode.REQUEST_CODE_SELECT_PHOTO:
                    processOneImage(data);
                    break;
                case ImageTags.IntentCode.REQUEST_CODE_SELECT_MULTI_PHOTO:
                    //Check if the intent contain only one image
                    if (data.getClipData() == null) {
                        processOneImage(data);
                    } else {
                        //intent has multi images
                        listOfImgs = ImageProcessing.processMultiImage(this, data);
                        new CompressImageTask(listOfImgs,
                                mImgConfig, ImageActivity.this).execute();
                    }
                    break;
                default:
                    break;
            }
        } else {
            Intent intent = new Intent();
            intent.setAction("net.alhazmy13.mediapicker.rxjava.image.service");
            intent.putExtra(ImageTags.Tags.PICK_ERROR, "user did not select any image");
            sendBroadcast(intent);
            finish();
        }
    }

    private void processOneImage(Intent data) {
        try {
            Uri selectedImage = data.getData();
            String selectedImagePath = FileProcessing.getPath(this, selectedImage);
            new CompressImageTask(selectedImagePath,
                    mImgConfig, ImageActivity.this).execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void finishActivity(List<String> path) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(ImagePicker.EXTRA_IMAGE_PATH, (Serializable) path);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void pickImageWrapper() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();

            final List<String> permissionsList = new ArrayList<String>();
            if((mImgConfig.mode == ImagePicker.Mode.CAMERA || mImgConfig.mode == ImagePicker.Mode.CAMERA_AND_GALLERY)  && !addPermission(permissionsList, Manifest.permission.CAMERA))
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
                                            ImageTags.IntentCode.REQUEST_CODE_ASK_PERMISSIONS);
                                }
                            });
                    return;
                }
                ActivityCompat.requestPermissions(ImageActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
                        ImageTags.IntentCode.REQUEST_CODE_ASK_PERMISSIONS);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ImageTags.IntentCode.REQUEST_CODE_ASK_PERMISSIONS:
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

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private static class CompressImageTask extends AsyncTask<Void, Void, Void> {

        private final ImageConfig mImgConfig;
        private final List<String> listOfImgs;
        private List<String> destinationPaths;
        private WeakReference<ImageActivity> mContext;


        public CompressImageTask(List<String> listOfImgs, ImageConfig imageConfig, ImageActivity context) {
            this.listOfImgs = listOfImgs;
            this.mContext = new WeakReference<>(context);
            this.mImgConfig = imageConfig;
            this.destinationPaths = new ArrayList<>();
        }

        public CompressImageTask(String absolutePath, ImageConfig imageConfig, ImageActivity context) {
            List<String> list = new ArrayList<>();
            list.add(absolutePath);
            this.listOfImgs = list;
            this.mContext = new WeakReference<>(context);
            this.destinationPaths = new ArrayList<>();
            this.mImgConfig = imageConfig;
        }


        @Override
        protected Void doInBackground(Void... params) {
            for (String mPath : listOfImgs) {
                File file = new File(mPath);
                File destinationFile = null;
                if (mImgConfig.isImgFromCamera) {
                    destinationFile = file;
                } else {
                    destinationFile = new File(mImgConfig.directory, Utility.getRandomString() + mImgConfig.extension.getValue());
                }
                destinationPaths.add(destinationFile.getAbsolutePath());
                try {
                    Utility.compressAndRotateIfNeeded(file, destinationFile, mImgConfig.compressLevel.getValue(), mImgConfig.reqWidth, mImgConfig.reqHeight);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ImageActivity context = mContext.get();
            if (context != null) {
                context.finishActivity(destinationPaths);
                Intent intent = new Intent();
                intent.setAction("net.alhazmy13.mediapicker.rxjava.image.service");
                intent.putExtra(ImageTags.Tags.IMAGE_PATH, (Serializable) destinationPaths);
                context.sendBroadcast(intent);
            }
        }
    }

}