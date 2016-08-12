package net.alhazmy13.mediapicker.adapter_rxjava.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import rx.Observer;

/**
 * Created by Alhazmy13 on 8/7/16.
 * MediaPicker
 */
public class ImagePickerReceiver extends BroadcastReceiver{

    private static final String TAG = "ImagePickerReceiver";
    private Observer<String> observer;
    public ImagePickerReceiver(){

    }
    public ImagePickerReceiver(Observer<String> observer) {
        this.observer = observer;
    }

    public void onReceive(Context context, Intent intent) {
       // Log.d(TAG, "Received message "+intent);
        String imagePath = intent.getStringExtra("IMAGE_PATH");
        observer.onNext(imagePath);
    }
}
