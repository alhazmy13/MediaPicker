package net.alhazmy13.mediapicker.Video;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;


/**
 * Created by Alhazmy13 on 10/26/15.
 */
public class VideoPicker {

    public static final String _MP4=".mp4";
    public static final String _3GP=".3gp";
    public static final String _MKV=".mkv";

    private Activity context;
    public static OnVideoSetListener onVideoPicked;
    private String extension;
    public static final String DEFAULT_DIR= Environment.getExternalStorageDirectory()+"/mediapicker/video/";
    protected static String directory;




    public VideoPicker(Activity context){
        this.context=context;
        extension= _MP4;
    }


    public interface OnVideoSetListener{
        void OnVideoSet(String path);
    }
    public void setOnVideoSetListener(OnVideoSetListener listen) {
        onVideoPicked = listen;
    }


    public void setExtension(String extension){
        this.extension=extension;
    }


    public void pick(){
        Intent intent=new Intent(context,VideoActivity.class);
        intent.putExtra("extension",extension);
        context.startActivity(intent);

    }
    public void setDirectory(String directory){
        this.directory=directory;
    }


}
