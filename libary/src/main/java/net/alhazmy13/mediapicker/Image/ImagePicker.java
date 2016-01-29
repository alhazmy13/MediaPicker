package net.alhazmy13.mediapicker.Image;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;

/**
 * Created by Alhazmy13 on 10/26/15.
 */
public class ImagePicker {


    //Extension Types
    public static final String PNG = ".png";
    public static final String JPG = ".jpg";

    //Comperes Levels
    public static final int HARD = 20;
    public static final int MEDIUM = 50;
    public static final int SOFT = 100;

    //Mode
    public static final int CAMERA = 0;
    public static final int GALERY = 1;
    public static final int CAMERA_AND_GALERY = 2;

    private Activity context;
    public static OnImageSetListener onImagePicked;
    private String extension=PNG;
    private int compressLevel=0;
    private boolean isCompressed=false;
    private int mode = CAMERA;
    public static final String DEFAULT_DIR=Environment.getExternalStorageDirectory()+"/mediapicker/image/";
    protected static String directory;

    //private boolean isFiltered =false;


    public ImagePicker(Activity context){
        this.context=context;
        extension= PNG;
        directory=DEFAULT_DIR;
    }



    public interface OnImageSetListener{
        void OnImageSet(String path);
    }
    public void setOnImageSetListener(OnImageSetListener listen) {
        onImagePicked = listen;
    }


    public void setExtension(String extension){
        this.extension=extension;
    }
    public void setCompressLevel(int level){
        isCompressed=true;
        compressLevel=level;
    }


    public void pick(){
        Intent intent=new Intent(context,ImageActivity.class);
        intent.putExtra("extension",extension);
        if(isCompressed)intent.putExtra("level",compressLevel);
        intent.putExtra("mode",mode);
        context.startActivity(intent);

    }

    public void setDirectory(String directory){
        this.directory=directory;
    }
    //TODO setFilter
    /*
    public void setFilter(int filterType){
        this.isFiltered=true;
        this.filterType=filterType;
    }
*/

    public void setMode(int mode){
        this.mode = mode;
    }


}
