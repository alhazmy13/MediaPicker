package net.alhazmy13.mediapicker.rxjava.image.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.alhazmy13.mediapicker.Image.ImageTags;

import java.util.List;

import io.reactivex.ObservableEmitter;


/**
 * Created by Alhazmy13 on 8/7/16.
 * MediaPicker
 */
public class ImagePickerReceiver extends BroadcastReceiver {

    private static final String TAG = "VideoPickerReceiver";
    private ObservableEmitter<List<String>> observer;

    public ImagePickerReceiver(ObservableEmitter<List<String>> observer) {
        this.observer = observer;
    }

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received message " + intent);
        List<String> imagePath = (List<String>) intent.getSerializableExtra(ImageTags.Tags.IMAGE_PATH);
        if (imagePath != null && imagePath.size() > 0)
            observer.onNext(imagePath);
        else
            observer.onError(new Throwable(intent.getStringExtra(ImageTags.Tags.PICK_ERROR)));
    }
}
