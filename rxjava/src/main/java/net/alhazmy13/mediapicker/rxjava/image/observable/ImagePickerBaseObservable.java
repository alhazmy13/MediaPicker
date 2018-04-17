package net.alhazmy13.mediapicker.rxjava.image.observable;

import android.content.Context;

import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by Alhazmy13 on 8/7/16.
 * MediaPicker
 */
abstract class ImagePickerBaseObservable implements ObservableOnSubscribe<List<String>> {

    private static final String TAG = "ImagePicker";
    public Context context;

    public ImagePickerBaseObservable(Context context) {
        this.context = context;
    }

    public abstract void registerImagePickerObservable();

    @Override
    public abstract void subscribe(ObservableEmitter<List<String>> emitter);


}