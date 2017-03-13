package net.alhazmy13.mediapicker.Video;

import android.os.Environment;

import java.io.Serializable;

/**
 * Created by Alhazmy13 on 8/16/16.
 * MediaPicker
 */
class VideoConfig implements Serializable {

    protected VideoPicker.Extension extension;
    //    protected VideoPicker.ComperesLevel compressLevel;
    protected VideoPicker.Mode mode;
    protected String directory;
    protected int reqHeight;
    protected int reqWidth;
    protected boolean allowMultiple;
    protected boolean isImgFromCamera;
    protected boolean debug;

    public VideoConfig() {
        this.extension = VideoPicker.Extension.MP4;
//        this.compressLevel = VideoPicker.ComperesLevel.NONE;
        this.mode = VideoPicker.Mode.CAMERA;
        this.directory = Environment.getExternalStorageDirectory() + VideoTags.Tags.IMAGE_PICKER_DIR;
        this.reqHeight = 0;
        this.reqWidth = 0;
        this.allowMultiple = false;
    }

    @Override
    public String toString() {
        return "ImageConfig{" +
                "extension=" + extension +
//                ", compressLevel=" + compressLevel +
                ", mode=" + mode +
                ", directory='" + directory + '\'' +
                ", reqHeight=" + reqHeight +
                ", reqWidth=" + reqWidth +
                ", allowMultiple=" + allowMultiple +
                ", isImgFromCamera=" + isImgFromCamera +
                ", debug=" + debug +
                '}';
    }
}
