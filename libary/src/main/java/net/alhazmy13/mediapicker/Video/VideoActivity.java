package net.alhazmy13.mediapicker.Video;

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
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import net.alhazmy13.mediapicker.FileProcessing;
import net.alhazmy13.mediapicker.R;
import net.alhazmy13.mediapicker.Utility;

import java.io.File;
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
public class VideoActivity extends AppCompatActivity {

    private File destination;
    private Uri mVideoUri;
    private VideoConfig mVideoConfig;
    private List<String> mListOfVideos;
    private AlertDialog alertDialog;

    public static Intent getCallingIntent(Context activity, VideoConfig videoConfig) {
        Intent intent = new Intent(activity, VideoActivity.class);
        intent.putExtra(VideoTags.Tags.IMG_CONFIG, videoConfig);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            mVideoConfig = (VideoConfig) intent.getSerializableExtra(VideoTags.Tags.IMG_CONFIG);
        }

        if (savedInstanceState == null) {
            pickVideoWrapper();
            mListOfVideos = new ArrayList<>();
        }
        if (mVideoConfig.debug)
            Log.d(VideoTags.Tags.TAG, mVideoConfig.toString());
    }

    @Override
    protected void onPause() {
        if (alertDialog != null)
            alertDialog.dismiss();
        super.onPause();
    }

    private void pickVideo() {
        Utility.createFolder(mVideoConfig.directory);
        destination = new File(mVideoConfig.directory, Utility.getRandomString() + mVideoConfig.extension.getValue());
        switch (mVideoConfig.mode) {
            case CAMERA:
                startActivityFromCamera();
                break;
            case GALLERY:
                if (mVideoConfig.allowMultiple)
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
                        if (mVideoConfig.debug)
                            Log.d(VideoTags.Tags.TAG, "Alert Dialog - Start From Camera");
                        startActivityFromCamera();
                        alertDialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.media_picker_gallery), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mVideoConfig.debug)
                            Log.d(VideoTags.Tags.TAG, "Alert Dialog - Start From Gallery");
                        if (mVideoConfig.allowMultiple)
                            startActivityFromGalleryMultiImg();
                        else
                            startActivityFromGallery();
                        alertDialog.dismiss();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        if (mVideoConfig.debug)
                            Log.d(VideoTags.Tags.TAG, "Alert Dialog - Canceled");
                        alertDialog.dismiss();
                        finish();
                    }
                }).create();
        alertDialog.show();

    }

    private void startActivityFromGallery() {
        mVideoConfig.isImgFromCamera = false;
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        photoPickerIntent.setType("video/*");
        startActivityForResult(photoPickerIntent, VideoTags.IntentCode.REQUEST_CODE_SELECT_PHOTO);
        if (mVideoConfig.debug)
            Log.d(VideoTags.Tags.TAG, "Gallery Start with Single video mode");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void startActivityFromGalleryMultiImg() {
        mVideoConfig.isImgFromCamera = false;
        Intent photoPickerIntent = new Intent();
        photoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("video/*");
        startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Picture"), VideoTags.IntentCode.REQUEST_CODE_SELECT_MULTI_PHOTO);
        if (mVideoConfig.debug)
            Log.d(VideoTags.Tags.TAG, "Gallery Start with Multiple videos mode");
    }

    private void startActivityFromCamera() {
        mVideoConfig.isImgFromCamera = true;
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        mVideoUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", destination);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mVideoUri);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), VideoTags.IntentCode.CAMERA_REQUEST);
        if (mVideoConfig.debug)
            Log.d(VideoTags.Tags.TAG, "Camera Start");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mVideoUri != null) {
            outState.putString(VideoTags.Tags.CAMERA_IMAGE_URI, mVideoUri.toString());
            outState.putSerializable(VideoTags.Tags.IMG_CONFIG, mVideoConfig);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(VideoTags.Tags.CAMERA_IMAGE_URI)) {
            mVideoUri = Uri.parse(savedInstanceState.getString(VideoTags.Tags.CAMERA_IMAGE_URI));
            destination = new File(mVideoUri.getPath());
            mVideoConfig = (VideoConfig) savedInstanceState.getSerializable(VideoTags.Tags.IMG_CONFIG);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mVideoConfig.debug)
            Log.d(VideoTags.Tags.TAG, "onActivityResult() called with: " + "requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case VideoTags.IntentCode.CAMERA_REQUEST:
                    new VideoActivity.CompresVideoTask(destination.getAbsolutePath(), mVideoConfig
                            , VideoActivity.this).execute();
                    break;
                case VideoTags.IntentCode.REQUEST_CODE_SELECT_PHOTO:
                    processOneVideo(data);
                    break;
                case VideoTags.IntentCode.REQUEST_CODE_SELECT_MULTI_PHOTO:
                    //Check if the intent contain only one image
                    if (data.getClipData() == null) {
                        processOneVideo(data);
                    } else {
                        //intent has multi images
                        mListOfVideos = VideoProcessing.processMultiVideos(this, data);
                        new VideoActivity.CompresVideoTask(mListOfVideos,
                                mVideoConfig, VideoActivity.this).execute();
                    }
                    break;
                default:
                    break;
            }
        } else {
            Intent intent = new Intent();
            intent.setAction(VideoTags.Action.SERVICE_ACTION);
            intent.putExtra(VideoTags.Tags.PICK_ERROR, "user did not select any videos");
            sendBroadcast(intent);
            finish();
        }
    }

    private void processOneVideo(Intent data) {
        try {
            Uri selectedVideo = data.getData();
            String path = FileProcessing.getVideoPath(selectedVideo, VideoActivity.this);
            new VideoActivity.CompresVideoTask(path,
                    mVideoConfig, VideoActivity.this).execute();

        } catch (Exception ex) {
            Intent intent = new Intent();
            intent.setAction(VideoTags.Action.SERVICE_ACTION);
            intent.putExtra(VideoTags.Tags.PICK_ERROR, "Issue with video path: " + ex.getMessage());
            sendBroadcast(intent);
            setResult(RESULT_CANCELED, intent);
            finish();
        }

    }

    private void finishActivity(List<String> path) {
        Intent intent = new Intent();
        intent.setAction(VideoTags.Action.SERVICE_ACTION);
        intent.putExtra(VideoTags.Tags.VIDEO_PATH, (Serializable) path);
        sendBroadcast(intent);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(VideoPicker.EXTRA_VIDEO_PATH, (Serializable) path);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void pickVideoWrapper() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<>();

            final List<String> permissionsList = new ArrayList<>();
            if ((mVideoConfig.mode == VideoPicker.Mode.CAMERA || mVideoConfig.mode == VideoPicker.Mode.CAMERA_AND_GALLERY) && !addPermission(permissionsList, Manifest.permission.CAMERA))
                permissionsNeeded.add(getString(R.string.media_picker_camera));
            if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                permissionsNeeded.add(getString(R.string.media_picker_read_Write_external_storage));

            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    // Need Rationale
                    StringBuilder message = new StringBuilder(getString(R.string.media_picker_you_need_to_grant_access_to) + permissionsNeeded.get(0));
                    for (int i = 1; i < permissionsNeeded.size(); i++)
                        message.append(", ").append(permissionsNeeded.get(i));
                    showMessageOKCancel(message.toString(),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(VideoActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
                                            VideoTags.IntentCode.REQUEST_CODE_ASK_PERMISSIONS);
                                }
                            });
                    return;
                }
                ActivityCompat.requestPermissions(VideoActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
                        VideoTags.IntentCode.REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }

            pickVideo();
        } else {
            pickVideo();
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(VideoActivity.this)
                .setMessage(message)
                .setPositiveButton(getString(R.string.media_picker_ok), okListener)
                .setNegativeButton(getString(R.string.media_picker_cancel), null)
                .create()
                .show();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ActivityCompat.checkSelfPermission(VideoActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            return ActivityCompat.shouldShowRequestPermissionRationale(VideoActivity.this, permission);
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case VideoTags.IntentCode.REQUEST_CODE_ASK_PERMISSIONS:
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
                    pickVideo();
                } else {
                    // Permission Denied
                    Toast.makeText(VideoActivity.this, getString(R.string.media_picker_some_permission_is_denied), Toast.LENGTH_SHORT)
                            .show();
                    onBackPressed();
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    private static class CompresVideoTask extends AsyncTask<Void, Void, Void> {

        private final VideoConfig mVideoConfig;
        private final List<String> listOfImgs;
        private List<String> destinationPaths;
        private WeakReference<VideoActivity> mContext;


        CompresVideoTask(List<String> listOfImgs, VideoConfig videoConfig, VideoActivity context) {
            this.listOfImgs = listOfImgs;
            this.mContext = new WeakReference<>(context);
            this.mVideoConfig = videoConfig;
            this.destinationPaths = new ArrayList<>();
        }

        CompresVideoTask(String absolutePath, VideoConfig videoConfig, VideoActivity context) {
            List<String> list = new ArrayList<>();
            list.add(absolutePath);
            this.listOfImgs = list;
            this.mContext = new WeakReference<>(context);
            this.destinationPaths = new ArrayList<>();
            this.mVideoConfig = videoConfig;
        }


        @Override
        protected Void doInBackground(Void... params) {
            for (String mPath : listOfImgs) {
                File file = new File(mPath);
                File destinationFile;
                if (mVideoConfig.isImgFromCamera) {
                    destinationFile = file;
                } else {
                    destinationFile = new File(mVideoConfig.directory, Utility.getRandomString() + mVideoConfig.extension.getValue());
                    FileProcessing.copyDirectory(file, destinationFile);
                }
                destinationPaths.add(destinationFile.getAbsolutePath());

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            VideoActivity context = mContext.get();
            if (context != null) {
                context.finishActivity(destinationPaths);

            }
        }
    }


}