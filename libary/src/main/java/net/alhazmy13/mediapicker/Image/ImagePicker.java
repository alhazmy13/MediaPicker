package net.alhazmy13.mediapicker.Image;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import java.io.Serializable;

/**
 * Created by Alhazmy13 on 10/26/15.
 * MediaPicker
 */
public class ImagePicker{

    private Activity context;
    private Extension extension;
    private ComperesLevel compressLevel;
    private Mode mode;
    private String directory;
    protected static OnImageSetListener onImagePicked;


    public interface OnImageSetListener {
        void OnImageSet(String path);

    }

    public enum Extension{
        PNG(".png"),JPG(".jpg");
        private final String value;
        Extension(String value){
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }

    public enum ComperesLevel{
        HARD(20),MEDIUM(50),SOFT(80),NONE(100);
        private final int value;
        ComperesLevel(int value){
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    public enum Mode{
        CAMERA(0),GALLERY(1),CAMERA_AND_GALLERY(2);
        private final int value;
        Mode(int value){
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    public enum Directory{
        DEFAULT(0);
        private final int value;
        Directory(int value){
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }
    public ImagePicker(Activity context){
        this.context = context;
        this.extension = Extension.PNG;
        this.directory = Environment.getExternalStorageDirectory()+"/mediapicker/image/";
        this.mode = Mode.CAMERA;
        this.compressLevel = ComperesLevel.NONE;
    }


    public void pick(){
        Intent intent=new Intent(context,ImageActivity.class);
        intent.putExtra(ImageTags.EXTENSION,extension);
        intent.putExtra(ImageTags.LEVEL,compressLevel);
        intent.putExtra(ImageTags.MODE,mode);
        intent.putExtra(ImageTags.DIRECTORY,directory);
        context.startActivity(intent);

    }

    public void setCompressLevel(ComperesLevel compressLevel) {
        this.compressLevel = compressLevel;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }
    public void setDirectory(Directory directory){
        switch (directory){
            case DEFAULT:
                this.directory = Environment.getExternalStorageDirectory()+"/mediapicker/image/";
                break;
        }
    }

    public void setOnImageSetListener(OnImageSetListener onImagePicked) {
        ImagePicker.onImagePicked = onImagePicked;
    }

    public void setExtension(Extension extension) {
        this.extension = extension;
    }
}
