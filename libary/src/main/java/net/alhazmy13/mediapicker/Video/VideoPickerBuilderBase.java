package net.alhazmy13.mediapicker.Video;

/**
 * Created by Alhazmy13 on 8/7/16.
 * MediaPicker
 */
public interface VideoPickerBuilderBase {
//    VideoPicker.Builder compressLevel(VideoPicker.ComperesLevel compressLevel);

    VideoPicker.Builder mode(VideoPicker.Mode mode);

    VideoPicker.Builder directory(String directory);

    VideoPicker.Builder directory(VideoPicker.Directory directory);

    VideoPicker.Builder extension(VideoPicker.Extension extension);

//    VideoPicker.Builder scale(int minWidth, int minHeight);
//
//    VideoPicker.Builder allowMultipleImages(boolean allowMultiple);

    VideoPicker.Builder enableDebuggingMode(boolean debug);

    VideoPicker build();

}
