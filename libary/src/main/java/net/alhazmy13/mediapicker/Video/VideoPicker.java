package net.alhazmy13.mediapicker.Video;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;

import java.lang.ref.WeakReference;


/**
 * Created by Alhazmy13 on 10/26/15.
 */
public class VideoPicker {


    public static final int VIDEO_PICKER_REQUEST_CODE = 12645;
    public static final String EXTRA_VIDEO_PATH = "EXTRA_VIDEO_PATH";

    private static final String IMAGE_PICKER_DIR = "/mediapicker/videos/";

    private final Extension extension;
    private final Mode mode;
    private final String directory;

    private VideoPicker(Builder builder) {

        // Required
        WeakReference<Activity> context = builder.context;

        // Optional
        extension = builder.extension;
        mode = builder.mode;
        directory = builder.directory;

        Intent callingIntent = VideoActivity.getCallingIntent(context.get(), extension,
                mode, directory);

        context.get().startActivityForResult(callingIntent, VIDEO_PICKER_REQUEST_CODE);
    }

    public Mode getMode() {
        return mode;
    }

    public String getDirectory() {
        return directory;
    }


    public Extension getExtension() {
        return extension;
    }

    public static class Builder {

        // Required params
        private final WeakReference<Activity> context;

        // Optional params
        private Extension extension = Extension._MP4;
        private Mode mode = Mode.CAMERA;
        private String directory = Environment.getExternalStorageDirectory() + IMAGE_PICKER_DIR;

        public Builder(Activity context) {
            this.context = new WeakReference<>(context);
        }


        public VideoPicker.Builder mode(Mode mode) {
            this.mode = mode;
            return this;
        }

        public VideoPicker.Builder directory(String directory) {
            this.directory = directory;
            return this;
        }

        public VideoPicker.Builder directory(Directory directory) {
            switch (directory) {
                case DEFAULT:
                    this.directory = Environment.getExternalStorageDirectory() + IMAGE_PICKER_DIR;
            }
            return this;
        }

        public VideoPicker.Builder extension(Extension extension) {
            this.extension = extension;
            return this;
        }

        public VideoPicker build() {
            return new VideoPicker(this);
        }
    }


    public enum Extension {
        _MP4(".mp4"), _3GP(".3gp"), _MKV(".mkv");
        private final String value;

        Extension(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }



    public enum Mode {
        CAMERA(0), GALLERY(1), CAMERA_AND_GALLERY(2);
        private final int value;

        Mode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum Directory {
        DEFAULT(0);
        private final int value;

        Directory(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
