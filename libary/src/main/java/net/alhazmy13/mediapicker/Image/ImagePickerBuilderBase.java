package net.alhazmy13.mediapicker.Image;

/**
 * Created by Alhazmy13 on 8/7/16.
 * MediaPicker
 */
public interface ImagePickerBuilderBase {
    ImagePicker.Builder compressLevel(ImagePicker.ComperesLevel compressLevel);

    ImagePicker.Builder mode(ImagePicker.Mode mode);

    ImagePicker.Builder directory(String directory);

    ImagePicker.Builder directory(ImagePicker.Directory directory);

    ImagePicker.Builder extension(ImagePicker.Extension extension);

    ImagePicker.Builder scale(int minWidth, int minHeight);

    ImagePicker.Builder allowMultipleImages(boolean allowMultiple);

    ImagePicker.Builder enableDebuggingMode(boolean debug);

    ImagePicker build();

}
