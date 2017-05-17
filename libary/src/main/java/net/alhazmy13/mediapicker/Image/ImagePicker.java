package net.alhazmy13.mediapicker.Image;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;

import java.lang.ref.WeakReference;

/**
 * Created by Alhazmy13 on 10/26/15.
 * MediaPicker
 */
public class ImagePicker {

    public static final int IMAGE_PICKER_REQUEST_CODE = 42141;
    public static final String EXTRA_IMAGE_PATH = "EXTRA_IMAGE_PATH";

    private final ImageConfig imageConfig;

    public ImagePicker(Builder builder) {

        // Required
        WeakReference<Activity> context = builder.context;

        // Optional
        imageConfig = builder.imageConfig;
        Intent callingIntent = ImageActivity.getCallingIntent(context.get(), imageConfig);

        context.get().startActivityForResult(callingIntent, IMAGE_PICKER_REQUEST_CODE);
    }


    public static class Builder implements ImagePickerBuilderBase {

        // Required params
        private final WeakReference<Activity> context;

        private ImageConfig imageConfig;

        public Builder(Activity context) {
            this.context = new WeakReference<>(context);
            this.imageConfig = new ImageConfig();
        }

        @Override
        public ImagePicker.Builder compressLevel(ComperesLevel compressLevel) {
            this.imageConfig.compressLevel = compressLevel;
            return this;
        }

        @Override
        public ImagePicker.Builder mode(Mode mode) {
            this.imageConfig.mode = mode;
            return this;
        }

        @Override
        public ImagePicker.Builder directory(String directory) {
            this.imageConfig.directory = directory;
            return this;
        }

        @Override
        public ImagePicker.Builder directory(Directory directory) {
            switch (directory) {
                case DEFAULT:
                    this.imageConfig.directory = Environment.getExternalStorageDirectory() + ImageTags.Tags.IMAGE_PICKER_DIR;
                    break;
                default:
                    break;
            }
            return this;
        }

        @Override
        public ImagePicker.Builder extension(Extension extension) {
            this.imageConfig.extension = extension;
            return this;
        }

        @Override
        public ImagePicker.Builder scale(int minWidth, int minHeight) {
            this.imageConfig.reqHeight = minHeight;
            this.imageConfig.reqWidth = minWidth;
            return this;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public ImagePicker.Builder allowMultipleImages(boolean allowMultiple) {
            this.imageConfig.allowMultiple = allowMultiple;
            return this;
        }

        @Override
        public ImagePicker.Builder enableDebuggingMode(boolean debug) {
            this.imageConfig.debug = debug;
            return this;
        }


        @Override
        public ImagePicker build() {
            return new ImagePicker(this);
        }


        public Activity getContext() {
            return context.get();
        }

    }


    public enum Extension {
        PNG(".png"), JPG(".jpg");
        private final String value;

        Extension(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum ComperesLevel {
        HARD(20), MEDIUM(50), SOFT(80), NONE(100);
        private final int value;

        ComperesLevel(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static ComperesLevel getEnum(int value) {
            for (ComperesLevel v : values())
                if (v.getValue() == value) return v;
            throw new IllegalArgumentException();
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
