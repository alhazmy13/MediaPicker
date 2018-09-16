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

    /**
     * The constant IMAGE_PICKER_REQUEST_CODE.
     */
    public static final int IMAGE_PICKER_REQUEST_CODE = 42141;
    /**
     * The constant EXTRA_IMAGE_PATH.
     */
    public static final String EXTRA_IMAGE_PATH = "EXTRA_IMAGE_PATH";

    /**
     * Instantiates a new Image picker.
     *
     * @param builder the builder
     */
    ImagePicker(Builder builder) {

        // Required
        WeakReference<Activity> context = builder.context;

        // Optional
        ImageConfig imageConfig = builder.imageConfig;
        Intent callingIntent = ImageActivity.getCallingIntent(context.get(), imageConfig);

        context.get().startActivityForResult(callingIntent, IMAGE_PICKER_REQUEST_CODE);
    }


    /**
     * The type Builder.
     */
    public static class Builder implements ImagePickerBuilderBase {

        // Required params
        private final WeakReference<Activity> context;

        private ImageConfig imageConfig;

        /**
         * Instantiates a new Builder.
         *
         * @param context the context
         */
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
        public Builder allowOnlineImages(boolean allowOnlineImages) {
            this.imageConfig.allowOnlineImages = allowOnlineImages;
            return this;
        }


        @Override
        public ImagePicker build() {
            return new ImagePicker(this);
        }


        /**
         * Gets context.
         *
         * @return the context
         */
        public Activity getContext() {
            return context.get();
        }

    }


    /**
     * The enum Extension.
     */
    public enum Extension {
        /**
         * Png extension.
         */
        PNG(".png"), /**
         * Jpg extension.
         */
        JPG(".jpg");
        private final String value;

        Extension(String value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * The enum Comperes level.
     */
    public enum ComperesLevel {
        /**
         * Hard comperes level.
         */
        HARD(20), /**
         * Medium comperes level.
         */
        MEDIUM(50), /**
         * Soft comperes level.
         */
        SOFT(80), /**
         * None comperes level.
         */
        NONE(100);
        private final int value;

        ComperesLevel(int value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public int getValue() {
            return value;
        }

    }

    /**
     * The enum Mode.
     */
    public enum Mode {
        /**
         * Camera mode.
         */
        CAMERA(0), /**
         * Gallery mode.
         */
        GALLERY(1), /**
         * Camera and gallery mode.
         */
        CAMERA_AND_GALLERY(2);
        private final int value;

        Mode(int value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public int getValue() {
            return value;
        }
    }

    /**
     * The enum Directory.
     */
    public enum Directory {
        /**
         * Default directory.
         */
        DEFAULT(0);
        private final int value;

        Directory(int value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public int getValue() {
            return value;
        }
    }
}
