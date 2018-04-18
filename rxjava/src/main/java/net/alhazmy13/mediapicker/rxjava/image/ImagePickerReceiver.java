package net.alhazmy13.mediapicker.rxjava.image;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.alhazmy13.mediapicker.Image.ImageTags;

import java.util.List;

import io.reactivex.Emitter;


/**
 * Created by Alhazmy13 on 8/7/16.
 * MediaPicker
 */
public class ImagePickerReceiver extends BroadcastReceiver {

    private static final String TAG = "VideoPickerReceiver";
    private Emitter<? super List<String>> emitter;

    public ImagePickerReceiver(Emitter<? super List<String>> observer) {
        this.emitter = observer;
    }

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received message " + intent);
        List<String> imagePath = intent.getStringArrayListExtra(ImageTags.Tags.IMAGE_PATH);
        if (imagePath != null && imagePath.size() > 0) {
            emitter.onNext(imagePath);
            emitter.onComplete();
        } else {
            emitter.onError(new Throwable(intent.getStringExtra(ImageTags.Tags.PICK_ERROR)));
        }
    }
}
