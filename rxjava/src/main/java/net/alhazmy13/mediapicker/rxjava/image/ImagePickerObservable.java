package net.alhazmy13.mediapicker.rxjava.image;


import android.content.Context;
import android.content.IntentFilter;

import net.alhazmy13.mediapicker.Image.ImagePicker;
import net.alhazmy13.mediapicker.Image.ImageTags;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;

/**
 * Created by Alhazmy13 on 8/7/16.
 * MediaPicker
 */
public class ImagePickerObservable implements ObservableOnSubscribe<List<String>>, Disposable {

    private static final String TAG = "ImagePickerObservable";
    private ImagePicker.Builder imagePicker;
    private ImagePickerReceiver broadcastReceiver;
    private final WeakReference<Context> contextWeakReference;

    public ImagePickerObservable(ImagePicker.Builder imagePicker) {
        this.imagePicker = imagePicker;
        this.contextWeakReference = new WeakReference<Context>(imagePicker.getContext());

    }


    @Override
    public void subscribe(ObservableEmitter<List<String>> emitter) {
        emitter.setDisposable(Disposables.fromAction(new Action() {
            @Override
            public void run() {
                dispose();
            }
        }));
        broadcastReceiver = new ImagePickerReceiver(emitter);
        imagePicker.build();
        contextWeakReference.get().registerReceiver(broadcastReceiver, new IntentFilter(ImageTags.Action.SERVICE_ACTION));
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
