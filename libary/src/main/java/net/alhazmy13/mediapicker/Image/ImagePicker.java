package net.alhazmy13.mediapicker.Image;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;

import java.lang.ref.WeakReference;

/**
 * Created by Alhazmy13 on 10/26/15.
 * MediaPicker
 */
public class ImagePicker {

    public static final int IMAGE_PICKER_REQUEST_CODE = 12345;
    public static final String EXTRA_IMAGE_PATH = "EXTRA_IMAGE_PATH";

    private static final String IMAGE_PICKER_DIR = "/mediapicker/images/";

    private final Extension extension;
    private final ComperesLevel compressLevel;
    private final Mode mode;
    private final String directory;

    private ImagePicker(Builder builder) {

        // Required
        WeakReference<Activity> context = builder.context;

        // Optional
        extension = builder.extension;
        compressLevel = builder.compressLevel;
        mode = builder.mode;
        directory = builder.directory;

        Intent callingIntent = ImageActivity.getCallingIntent(context.get(), extension, compressLevel,
                mode, directory);

        context.get().startActivityForResult(callingIntent, IMAGE_PICKER_REQUEST_CODE);
    }

    public Mode getMode() {
        return mode;
    }

    public String getDirectory() {
        return directory;
    }

    public ComperesLevel getCompressLevel() {
        return compressLevel;
    }

    public Extension getExtension() {
        return extension;
    }

    public static class Builder {

        // Required params
        private final WeakReference<Activity> context;

        // Optional params
        private Extension extension = Extension.PNG;
        private ComperesLevel compressLevel =  ComperesLevel.NONE;
        private Mode mode = Mode.CAMERA;
        private String directory = Environment.getExternalStorageDirectory() + IMAGE_PICKER_DIR;

        public Builder(Activity context) {
            this.context = new WeakReference<>(context);
        }

        public ImagePicker.Builder compressLevel(ComperesLevel compressLevel) {
            this.compressLevel = compressLevel;
            return this;
        }

        public ImagePicker.Builder mode(Mode mode) {
            this.mode = mode;
            return this;
        }

        public ImagePicker.Builder directory(String directory) {
            this.directory = directory;
            return this;
        }

        public ImagePicker.Builder directory(Directory directory) {
            switch (directory) {
                case DEFAULT:
                    this.directory = Environment.getExternalStorageDirectory() + IMAGE_PICKER_DIR;
            }
            return this;
        }

        public ImagePicker.Builder extension(Extension extension) {
            this.extension = extension;
            return this;
        }

        public ImagePicker build() {
            return new ImagePicker(this);
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
