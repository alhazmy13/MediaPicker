package net.alhazmy13.mediapicker.Video;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;

import java.lang.ref.WeakReference;


/**
 * Created by Alhazmy13 on 10/26/15.
 */
public class VideoPicker {


    public static final int VIDEO_PICKER_REQUEST_CODE = 53213;
    public static final String EXTRA_VIDEO_PATH = "EXTRA_VIDEO_PATH";

    VideoPicker(VideoPicker.Builder builder) {

        // Required
        WeakReference<Activity> context = builder.context;

        // Optional
        VideoConfig imageConfig = builder.imageConfig;
        Intent callingIntent = VideoActivity.getCallingIntent(context.get(), imageConfig);

        context.get().startActivityForResult(callingIntent, VIDEO_PICKER_REQUEST_CODE);
    }


    public static class Builder implements VideoPickerBuilderBase {

        // Required params
        private final WeakReference<Activity> context;

        private VideoConfig imageConfig;

        public Builder(Activity context) {
            this.context = new WeakReference<>(context);
            this.imageConfig = new VideoConfig();
        }


        @Override
        public VideoPicker.Builder mode(VideoPicker.Mode mode) {
            this.imageConfig.mode = mode;
            return this;
        }

        @Override
        public VideoPicker.Builder directory(String directory) {
            this.imageConfig.directory = directory;
            return this;
        }

        @Override
        public VideoPicker.Builder directory(VideoPicker.Directory directory) {
            switch (directory) {
                case DEFAULT:
                    this.imageConfig.directory = Environment.getExternalStorageDirectory() + VideoTags.Tags.IMAGE_PICKER_DIR;
                    break;
                default:
                    break;
            }
            return this;
        }

        @Override
        public VideoPicker.Builder extension(VideoPicker.Extension extension) {
            this.imageConfig.extension = extension;
            return this;
        }

        @Override
        public VideoPicker.Builder enableDebuggingMode(boolean debug) {
            this.imageConfig.debug = debug;
            return this;
        }


        @Override
        public VideoPicker build() {
            return new VideoPicker(this);
        }


        public Activity getContext() {
            return context.get();
        }

    }


    public enum Extension {
        MP4(".mp4");
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
