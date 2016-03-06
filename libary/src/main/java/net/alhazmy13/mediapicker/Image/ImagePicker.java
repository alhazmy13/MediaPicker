package net.alhazmy13.mediapicker.Image;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;

/**
 * Created by Alhazmy13 on 10/26/15.
 * MediaPicker
 */
public class ImagePicker {

    private Activity context;
    private Extension extension;
    private ComperesLevel compressLevel;
    private Mode mode;
    private String directory;
    protected static OnImageSetListener onImagePicked;


    public interface OnImageSetListener {
        void OnImageSet(String path);

    }



    public static class Builder{
        private ImagePicker imagePicker;
        public  Builder(Activity context){
            imagePicker = new ImagePicker(context);
        }
        public ImagePicker.Builder setCompressLevel(ComperesLevel compressLevel) {
            imagePicker.compressLevel = compressLevel;
            return this;
        }
        public ImagePicker.Builder setMode(Mode mode) {
            imagePicker.mode = mode;
            return this;
        }
        public ImagePicker.Builder setDirectory(String directory) {
            imagePicker.directory = directory;
            return this;
        }
        public ImagePicker.Builder setDirectory(Directory directory){
            switch (directory){
                case DEFAULT:
                    imagePicker.directory = Environment.getExternalStorageDirectory()+"/mediapicker/image/";
                    return this;
            }
            return this;
        }
        public ImagePicker.Builder setOnImageSetListener(OnImageSetListener onImagePicked) {
            imagePicker.onImagePicked = onImagePicked;
            return this;
        }
        public ImagePicker.Builder setExtension(Extension extension) {
            imagePicker.extension = extension;
            return this;
        }
        public void pick(){
            Intent intent=new Intent(imagePicker.context,ImageActivity.class);
            intent.putExtra(ImageTags.EXTENSION,imagePicker.extension);
            intent.putExtra(ImageTags.LEVEL,imagePicker.compressLevel);
            intent.putExtra(ImageTags.MODE,imagePicker.mode);
            intent.putExtra(ImageTags.DIRECTORY,imagePicker.directory);
            imagePicker.context.startActivity(intent);

        }
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
        public static ComperesLevel getEnum(int value) {
            for(ComperesLevel v : values())
                if(v.getValue() == value) return v;
            throw new IllegalArgumentException();
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
    @Deprecated
    public ImagePicker(Activity context){
        this.context = context;
        this.extension = Extension.PNG;
        this.directory = Environment.getExternalStorageDirectory()+"/mediapicker/image/";
        this.mode = Mode.CAMERA;
        this.compressLevel = ComperesLevel.NONE;
    }


    @Deprecated
    public void pick(){
        Intent intent=new Intent(context,ImageActivity.class);
        intent.putExtra(ImageTags.EXTENSION,extension);
        intent.putExtra(ImageTags.LEVEL,compressLevel);
        intent.putExtra(ImageTags.MODE,mode);
        intent.putExtra(ImageTags.DIRECTORY,directory);
        context.startActivity(intent);

    }
    @Deprecated
    public void setCompressLevel(ComperesLevel compressLevel) {
        this.compressLevel = compressLevel;
    }
    @Deprecated
    public void setMode(Mode mode) {
        this.mode = mode;
    }
    @Deprecated
    public void setDirectory(String directory) {
        this.directory = directory;
    }
    @Deprecated
    public void setDirectory(Directory directory){
        switch (directory){
            case DEFAULT:
                this.directory = Environment.getExternalStorageDirectory()+"/mediapicker/image/";
                break;
        }
    }
    @Deprecated
    public void setOnImageSetListener(OnImageSetListener onImagePicked) {
        this.onImagePicked = onImagePicked;
    }
    @Deprecated
    public void setExtension(Extension extension) {
        this.extension = extension;
    }
}
