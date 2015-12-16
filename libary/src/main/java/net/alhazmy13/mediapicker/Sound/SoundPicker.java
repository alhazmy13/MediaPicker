package net.alhazmy13.mediapicker.Sound;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Alhazmy13 on 11/14/15.
 */
public class SoundPicker {
    private Activity context;
    public static OnVideoSetListener onVideoPicked;
    private String extension;
    private boolean isCompressed=false;
    public SoundPicker(Activity context){
        this.context=context;
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
       // Intent intent=new Intent(context,SoundActivity.class);
     //   context.startActivity(intent);

    }
    static {
        System.loadLibrary("hello-jni");
    }

}
