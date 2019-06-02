package net.alhazmy13.mediapicker.Image;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ClipData;
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

import net.alhazmy13.mediapicker.FileProcessing;
import net.alhazmy13.mediapicker.R;
import net.alhazmy13.mediapicker.Utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.DialogInterface.BUTTON_POSITIVE;

/**
 * Created by Alhazmy13 on 10/26/15.
 * MediaPicker
 */
public class ImageActivity extends AppCompatActivity {

    private File destination;
    private Uri mImageUri;
    private ImageConfig mImgConfig;
    private List<String> listOfImgs;
    private AlertDialog alertDialog;

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

    @Override
    protected void onStop() {
        if (alertDialog != null)
            alertDialog.dismiss();
        super.onStop();
    }

    private void pickImage() {
        Utility.createFolder(mImgConfig.directory);
        destination = new File(mImgConfig.directory, Utility.getRandomString() + mImgConfig.extension.getValue());
        switch (mImgConfig.mode) {
            case CAMERA:
                startActivityFromCamera();
                break;
            case GALLERY:
                if (mImgConfig.allowMultiple && mImgConfig.allowOnlineImages)
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
        alertDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.media_picker_select_from))
                .setPositiveButton(getString(R.string.media_picker_camera), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mImgConfig.debug)
                            Log.d(ImageTags.Tags.TAG, "Alert Dialog - Start From Camera");
                        startActivityFromCamera();
                        alertDialog.dismiss();
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
                        alertDialog.dismiss();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        if (mImgConfig.debug)
                            Log.d(ImageTags.Tags.TAG, "Alert Dialog - Canceled");
                        alertDialog.dismiss();
                        finish();
                    }
                })
                .create();
        if (alertDialog != null)
            alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    private void startActivityFromGallery() {
        mImgConfig.isImgFromCamera = false;
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, !mImgConfig.allowOnlineImages);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ImageTags.IntentCode.REQUEST_CODE_SELECT_PHOTO);
        if (mImgConfig.debug)
            Log.d(ImageTags.Tags.TAG, "Gallery Start with Single Image mode");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void startActivityFromGalleryMultiImg() {
        mImgConfig.isImgFromCamera = false;
        Intent photoPickerIntent = new Intent();
        photoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, !mImgConfig.allowOnlineImages);
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
        outState.putBoolean(ImageTags.Tags.IS_ALERT_SHOWING, (alertDialog == null ? false : alertDialog.isShowing()));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(ImageTags.Tags.CAMERA_IMAGE_URI)) {
            mImageUri = Uri.parse(savedInstanceState.getString(ImageTags.Tags.CAMERA_IMAGE_URI));
            destination = new File(mImageUri.getPath());
            mImgConfig = (ImageConfig) savedInstanceState.getSerializable(ImageTags.Tags.IMG_CONFIG);
        }
        if (savedInstanceState.getBoolean(ImageTags.Tags.IS_ALERT_SHOWING, false)) {
            if (alertDialog == null)
                pickImage();
            else
                alertDialog.show();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mImgConfig.debug)
            Log.d(ImageTags.Tags.TAG, "onActivityResult() called with: " + "requestCode = [" + requestCode + "]," +
                    " resultCode = [" + resultCode + "], data = [" + data + "]");
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
                    processMutliPhoto(data);
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void processMutliPhoto(Intent data) {
        //Check if the intent contain only one image
        if (data.getClipData() == null) {
            processOneImage(data);
        } else {
            //intent has multi images
            listOfImgs = ImageProcessing.processMultiImage(this, data);
            if (listOfImgs != null && listOfImgs.size() > 0) {
                new CompressImageTask(listOfImgs, mImgConfig, ImageActivity.this).execute();
            } else {
                //For 'Select pic from Google Photos - app Crash' fix
                String check = data.getClipData().toString();
                if (check != null && check.contains("com.google.android.apps.photos")) {
                    ClipData clipdata = data.getClipData();
                    for (int i = 0; i < clipdata.getItemCount(); i++) {
                        Uri selectedImage = clipdata.getItemAt(i).getUri();
                        String selectedImagePath = FileProcessing.getPath(ImageActivity.this, selectedImage);
                        listOfImgs.add(selectedImagePath);
                    }
                    new CompressImageTask(listOfImgs, mImgConfig, ImageActivity.this).execute();
                }
            }
        }
    }

    public void processOneImage(Intent data) {
        try {
            Uri selectedImage = data.getData();
            if (selectedImage != null) {
                String rawPath = selectedImage.toString();
                //For 'Select pic from Google Drive - app Crash' fix
                if (rawPath.contains("com.google.android.apps.docs.storage")) {
                    String fileTempPath = getCacheDir().getPath();
                    new ImageActivity.SaveImageFromGoogleDriveTask(fileTempPath, mImgConfig, selectedImage, ImageActivity.this).execute();
                } else {
                    String selectedImagePath = FileProcessing.getPath(this, selectedImage);
                    new ImageActivity.CompressImageTask(selectedImagePath,
                            mImgConfig, ImageActivity.this).execute();
                }
            }
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
            List<String> permissionsNeeded = new ArrayList<>();

            final List<String> permissionsList = new ArrayList<>();
            if ((mImgConfig.mode == ImagePicker.Mode.CAMERA || mImgConfig.mode == ImagePicker.Mode.CAMERA_AND_GALLERY) && !addPermission(permissionsList, Manifest.permission.CAMERA))
                permissionsNeeded.add(getString(R.string.media_picker_camera));
            if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                permissionsNeeded.add(getString(R.string.media_picker_read_Write_external_storage));

            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    // Need Rationale
                    StringBuilder message = new StringBuilder(getString(R.string.media_picker_you_need_to_grant_access_to) + " " + permissionsNeeded.get(0));
                    for (int i = 1; i < permissionsNeeded.size(); i++)
                        message.append(", ").append(permissionsNeeded.get(i));
                    showMessageOKCancel(message.toString(),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case BUTTON_POSITIVE:
                                            ActivityCompat.requestPermissions(ImageActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
                                                    ImageTags.IntentCode.REQUEST_CODE_ASK_PERMISSIONS);
                                            break;

                                        default:
                                            onBackPressed();
                                            break;

                                    }


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
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(getString(R.string.media_picker_ok), okListener)
                .setNegativeButton(getString(R.string.media_picker_cancel), okListener)
                .create()
                .show();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ActivityCompat.checkSelfPermission(ImageActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            return ActivityCompat.shouldShowRequestPermissionRationale(ImageActivity.this, permission);
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ImageTags.IntentCode.REQUEST_CODE_ASK_PERMISSIONS:
                Map<String, Integer> perms = new HashMap<>();
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
                    onBackPressed();
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


        CompressImageTask(List<String> listOfImgs, ImageConfig imageConfig, ImageActivity context) {
            this.listOfImgs = listOfImgs;
            this.mContext = new WeakReference<>(context);
            this.mImgConfig = imageConfig;
            this.destinationPaths = new ArrayList<>();
        }

        CompressImageTask(String absolutePath, ImageConfig imageConfig, ImageActivity context) {
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
                File destinationFile;
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


    private static class SaveImageFromGoogleDriveTask extends AsyncTask<Void, Void, Void> {

        private final ImageConfig mImgConfig;
        private final List<String> listOfImgs;
        private List<String> destinationPaths;
        private List<Uri> destinationUris;
        private WeakReference<ImageActivity> mContext;


        SaveImageFromGoogleDriveTask(String absolutePath, ImageConfig imageConfig, Uri uri, ImageActivity context) {
            List<String> list = new ArrayList<>();
            list.add(absolutePath);
            this.listOfImgs = list;

            List<Uri> uris = new ArrayList<>();
            uris.add(uri);
            destinationUris = uris;

            this.mContext = new WeakReference<>(context);
            this.destinationPaths = new ArrayList<>();
            this.mImgConfig = imageConfig;
        }


        @Override
        protected Void doInBackground(Void... params) {

            for (int i = 0; i < listOfImgs.size(); i++) {
                String path = listOfImgs.get(i);
                Uri uriPath = destinationUris.get(i);
                try {
                    String fileName = "drive_img_" + System.currentTimeMillis() + ".jpg";
                    String fullImagePath = path + "/" + fileName;
                    boolean isFileSaved = saveFile(uriPath, fullImagePath);
                    if (isFileSaved) {
                        destinationPaths.add(fullImagePath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new CompressImageTask(destinationPaths, mImgConfig, mContext.get()).execute();
        }

        boolean filenotfoundexecption;

        //For Google Drive
        boolean saveFile(Uri sourceuri, String destination) throws IOException {
            filenotfoundexecption = false;
            int originalsize;
            InputStream input = null;
            try {
                input = mContext.get().getContentResolver().openInputStream(sourceuri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                filenotfoundexecption = true;
            }

            try {
                originalsize = input.available();
                BufferedInputStream bis;
                BufferedOutputStream bos;
                try {
                    bis = new BufferedInputStream(input);
                    bos = new BufferedOutputStream(new FileOutputStream(destination, false));
                    byte[] buf = new byte[originalsize];
                    bis.read(buf);
                    do {
                        bos.write(buf);
                    } while (bis.read(buf) != -1);
                } catch (IOException e) {
                    filenotfoundexecption = true;
                    return false;
                }
            } catch (NullPointerException e) {
                filenotfoundexecption = true;
            }
            return true;
        }

    }
}
