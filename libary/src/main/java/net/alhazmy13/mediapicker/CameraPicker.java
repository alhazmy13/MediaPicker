package net.alhazmy13.mediapicker;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Alhazmy13 on 10/26/15.
 */
public class CameraPicker {
    //private ImageView imageView;
    private String path;
    private Activity context;
    public static OnImagePicked onImagePicked;
    private String extension;
    private int sizeType;
    private int size=1024;
    private boolean isSizeChanged=false;


    public CameraPicker(Activity context){
        this.context=context;
        extension=Img.PNG;
        sizeType=Img.KB;
        size=1024;
    }

    public interface OnImagePicked{
        void OnImagePicked(String path);
    }
    public void setOnImagePicked(OnImagePicked listen) {
        onImagePicked = listen;
    }


    public void setExtension(String extension){
        this.extension=extension;
    }

    public void setSize(int type,int size){
        if(type==Img.ORIGINAL) {
            isSizeChanged = false;
        }else if(type==Img.KB || type==Img.MB){
            isSizeChanged = true;
            this.size = size * (this.sizeType == Img.MB ? 1024 : 1);
        }
    }


    public void pick(){
        Intent intent=new Intent(context,ForwarderActivity.class);
        intent.putExtra("extension",extension);
        if(isSizeChanged)intent.putExtra("size",size);
        context.startActivity(intent);

    }




}
