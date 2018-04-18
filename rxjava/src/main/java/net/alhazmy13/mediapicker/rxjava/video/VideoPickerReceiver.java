package net.alhazmy13.mediapicker.rxjava.video;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.alhazmy13.mediapicker.Video.VideoTags;

import java.util.List;

import io.reactivex.ObservableEmitter;

/**
 * Created by Alhazmy13 on 8/7/16.
 * MediaPicker
 */
public class VideoPickerReceiver extends BroadcastReceiver {

    private static final String TAG = "VideoPickerReceiver";
    private ObservableEmitter<List<String>> observer;

    public VideoPickerReceiver(ObservableEmitter<List<String>> observer) {
        this.observer = observer;
    }

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received message " + intent);
        List<String> imagePath = intent.getStringArrayListExtra(VideoTags.Tags.VIDEO_PATH);
        if (imagePath != null && imagePath.size() > 0)
            observer.onNext(imagePath);
        else
            observer.onError(new Throwable(intent.getStringExtra(VideoTags.Tags.PICK_ERROR)));
    }
}
