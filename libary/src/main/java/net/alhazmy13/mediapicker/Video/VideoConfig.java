package net.alhazmy13.mediapicker.Video;

import android.os.Environment;

import java.io.Serializable;

/**
 * Created by Alhazmy13 on 8/16/16.
 * MediaPicker
 */
class VideoConfig implements Serializable {

    protected VideoPicker.Extension extension;
    protected VideoPicker.Mode mode;
    protected String directory;
    protected boolean allowMultiple;
    protected boolean isImgFromCamera;
    protected boolean debug;

    VideoConfig() {
        this.extension = VideoPicker.Extension.MP4;
        this.mode = VideoPicker.Mode.CAMERA;
        this.directory = Environment.getExternalStorageDirectory() + VideoTags.Tags.IMAGE_PICKER_DIR;
        this.allowMultiple = false;
    }

    @Override
    public String toString() {
        return "ImageConfig{" +
                "extension=" + extension +
                ", mode=" + mode +
                ", directory='" + directory + '\'' +
                ", allowMultiple=" + allowMultiple +
                ", isImgFromCamera=" + isImgFromCamera +
                ", debug=" + debug +
                '}';
    }
}
