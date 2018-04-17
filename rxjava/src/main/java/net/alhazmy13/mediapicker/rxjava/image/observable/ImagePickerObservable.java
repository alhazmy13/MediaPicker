package net.alhazmy13.mediapicker.rxjava.image.observable;


import android.content.IntentFilter;

import net.alhazmy13.mediapicker.Image.ImagePicker;
import net.alhazmy13.mediapicker.Image.ImageTags;
import net.alhazmy13.mediapicker.rxjava.image.service.ImagePickerReceiver;

import java.util.List;

import io.reactivex.ObservableEmitter;

/**
 * Created by Alhazmy13 on 8/7/16.
 * MediaPicker
 */
public class ImagePickerObservable extends ImagePickerBaseObservable {

    private static final String TAG = "VideoPickerObservable";
    private ImagePicker.Builder imagePicker;
    private ImagePickerReceiver receiver;

    public ImagePickerObservable(ImagePicker.Builder imagePicker) {
        super(imagePicker.getContext());
        this.imagePicker = imagePicker;

    }


    @Override
    public void registerImagePickerObservable() {
        context.registerReceiver(receiver, new IntentFilter(ImageTags.Action.SERVICE_ACTION));

    }
//
//    @Override
//    public void onUnsubscribed() {
//        Log.d(TAG, "onUnsubscribed() called with: " + "");
//        try {
//            context.unregisterReceiver(receiver);
//        } catch (IllegalArgumentException ignored) {
//        }
//
//    }


    @Override
    public void subscribe(ObservableEmitter<List<String>> emitter) {
        receiver = new ImagePickerReceiver(emitter);
        imagePicker.build();
        registerImagePickerObservable();
    }
}
