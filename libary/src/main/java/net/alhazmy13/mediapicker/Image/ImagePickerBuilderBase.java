package net.alhazmy13.mediapicker.Image;

/**
 * Created by Alhazmy13 on 8/7/16.
 * MediaPicker
 */
public interface ImagePickerBuilderBase {
    /**
     * Compress level image picker . builder.
     *
     * @param compressLevel the compress level
     * @return the image picker . builder
     */
    ImagePicker.Builder compressLevel(ImagePicker.ComperesLevel compressLevel);

    /**
     * Mode image picker . builder.
     *
     * @param mode the mode
     * @return the image picker . builder
     */
    ImagePicker.Builder mode(ImagePicker.Mode mode);

    /**
     * Directory image picker . builder.
     *
     * @param directory the directory
     * @return the image picker . builder
     */
    ImagePicker.Builder directory(String directory);

    /**
     * Directory image picker . builder.
     *
     * @param directory the directory
     * @return the image picker . builder
     */
    ImagePicker.Builder directory(ImagePicker.Directory directory);

    /**
     * Extension image picker . builder.
     *
     * @param extension the extension
     * @return the image picker . builder
     */
    ImagePicker.Builder extension(ImagePicker.Extension extension);

    /**
     * Scale image picker . builder.
     *
     * @param minWidth  the min width
     * @param minHeight the min height
     * @return the image picker . builder
     */
    ImagePicker.Builder scale(int minWidth, int minHeight);

    /**
     * Allow multiple images
     *
     * @param allowMultiple the allow multiple
     * @return the image picker . builder
     */
    ImagePicker.Builder allowMultipleImages(boolean allowMultiple);

    /**
     * Enable debugging mode image picker . builder.
     *
     * @param debug the debug
     * @return the image picker . builder
     */
    ImagePicker.Builder enableDebuggingMode(boolean debug);

    /**
     * Allow online images (ex: Google Drive, Google Photo ...)
     *
     * @param allowOnlineImages the allow online images
     * @return the image picker . builder
     */
    ImagePicker.Builder allowOnlineImages(boolean allowOnlineImages);

    /**
     * Build image picker.
     *
     * @return the image picker
     */
    ImagePicker build();

}
