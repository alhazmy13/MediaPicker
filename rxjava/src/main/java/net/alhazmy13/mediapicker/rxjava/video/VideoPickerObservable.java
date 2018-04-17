package net.alhazmy13.mediapicker.rxjava.video;


import android.content.Context;
import android.content.IntentFilter;

import net.alhazmy13.mediapicker.Image.ImageTags;
import net.alhazmy13.mediapicker.Video.VideoPicker;
import net.alhazmy13.mediapicker.Video.VideoTags;
import net.alhazmy13.mediapicker.rxjava.image.ImagePickerReceiver;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

/**
 * Created by Alhazmy13 on 8/7/16.
 * MediaPicker
 */
public class VideoPickerObservable implements ObservableOnSubscribe<List<String>>, Disposable {

    private static final String TAG = "VideoPickerObservable";
    private VideoPicker.Builder mVideoPicker;
    private VideoPickerReceiver broadcastReceiver;
    private final WeakReference<Context> contextWeakReference;

    public VideoPickerObservable(VideoPicker.Builder videoPicker) {
        this.mVideoPicker = videoPicker;
        this.contextWeakReference = new WeakReference<Context>(mVideoPicker.getContext());


    }


    @Override
    public void subscribe(ObservableEmitter<List<String>> emitter) {
        broadcastReceiver = new VideoPickerReceiver(emitter);
        mVideoPicker.build();
        contextWeakReference.get().registerReceiver(broadcastReceiver, new IntentFilter(VideoTags.Action.SERVICE_ACTION));

    }

    @Override
    public void dispose() {
        if (contextWeakReference != null && contextWeakReference.get() != null && broadcastReceiver != null) {
            contextWeakReference.get().unregisterReceiver(broadcastReceiver);

        }
        broadcastReceiver = null;
    }

    @Override
    public boolean isDisposed() {
        return broadcastReceiver == null;
    }
}
