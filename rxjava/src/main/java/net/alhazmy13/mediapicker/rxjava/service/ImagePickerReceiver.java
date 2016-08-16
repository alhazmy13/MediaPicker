package net.alhazmy13.mediapicker.rxjava.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.alhazmy13.mediapicker.Image.ImageTags;

import java.util.List;

import rx.Observer;

/**
 * Created by Alhazmy13 on 8/7/16.
 * MediaPicker
 */
public class ImagePickerReceiver extends BroadcastReceiver{

    private static final String TAG = "ImagePickerReceiver";
    private Observer<List<String>> observer;

    public ImagePickerReceiver(Observer<List<String>> observer) {
        this.observer = observer;
    }

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received message "+intent);
        List<String> imagePath = (List<String>) intent.getSerializableExtra(ImageTags.Tags.IMAGE_PATH);
        observer.onNext(imagePath);
    }
}
