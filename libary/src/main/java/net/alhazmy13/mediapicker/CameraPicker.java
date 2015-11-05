package net.alhazmy13.mediapicker;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alhazmy13 on 10/26/15.
 */
public class CameraPicker extends AbstractClass{
    private Activity context;
    public static OnImageSetListener onImagePicked;
    private String extension;
    private int sizeType;
    private int size=1024;
    private boolean isSizeChanged=false;

    public CameraPicker(Activity context){
        this.context=context;
        extension= StaticAttributes.PNG;
        sizeType= StaticAttributes.KB;
        size=1024;
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

    public void setSize(int type,int size){
        if(type== ORIGINAL) {
            isSizeChanged = false;
        }else if(type== KB || type== MB){
            isSizeChanged = true;
            this.size = size * (this.sizeType == MB ? 1024 : 1);
        }
    }



    public void pick(){
        Intent intent=new Intent(context,CameraActivity.class);
        intent.putExtra("extension",extension);
        if(isSizeChanged)intent.putExtra("size",size);
        context.startActivity(intent);

    }




}
