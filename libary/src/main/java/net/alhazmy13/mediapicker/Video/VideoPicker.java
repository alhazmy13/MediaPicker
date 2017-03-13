package net.alhazmy13.mediapicker.Video;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;

import java.lang.ref.WeakReference;


/**
 * Created by Alhazmy13 on 10/26/15.
 */
public class VideoPicker {


    static final int VIDEO_PICKER_REQUEST_CODE = 53213;
    static final String EXTRA_VIDEO_PATH = "EXTRA_VIDEO_PATH";

    private final VideoConfig imageConfig;

    VideoPicker(VideoPicker.Builder builder) {

        // Required
        WeakReference<Activity> context = builder.context;

        // Optional
        imageConfig = builder.imageConfig;
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

        //
//        @Override
//        public VideoPicker.Builder compressLevel(VideoPicker.ComperesLevel compressLevel) {
//            this.imageConfig.compressLevel = compressLevel;
//            return this;
//        }
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
            }
            return this;
        }

        @Override
        public VideoPicker.Builder extension(VideoPicker.Extension extension) {
            this.imageConfig.extension = extension;
            return this;
        }
//        @Override
//        public VideoPicker.Builder scale(int minWidth, int minHeight) {
//            this.imageConfig.reqHeight = minHeight;
//            this.imageConfig.reqWidth = minWidth;
//            return this;
//        }
//        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
//        @Override
//        public VideoPicker.Builder allowMultipleImages(boolean allowMultiple){
//            this.imageConfig.allowMultiple = allowMultiple;
//            return this;
//        }

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

//    public enum ComperesLevel {
//        HARD(20), MEDIUM(50), SOFT(80), NONE(100);
//        private final int value;
//
//        ComperesLevel(int value) {
//            this.value = value;
//        }
//
//        public int getValue() {
//            return value;
//        }
//
//        public static VideoPicker.ComperesLevel getEnum(int value) {
//            for (VideoPicker.ComperesLevel v : values())
//                if (v.getValue() == value) return v;
//            throw new IllegalArgumentException();
//        }
//    }

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
