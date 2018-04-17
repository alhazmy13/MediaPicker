package net.alhazmy13.mediapicker.rxjava.video.observable;

import android.content.Context;

import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;


/**
 * Created by Alhazmy13 on 8/7/16.
 * MediaPicker
 */
abstract class VideoPickerBaseObservable implements ObservableOnSubscribe<List<String>> {

    private static final String TAG = "ImagePicker";
    public Context context;

    public VideoPickerBaseObservable(Context context) {
        this.context = context;
    }

    @Override
    public abstract void subscribe(ObservableEmitter<List<String>> emitter);


}