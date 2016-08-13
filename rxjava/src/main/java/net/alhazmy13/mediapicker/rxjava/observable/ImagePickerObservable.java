package net.alhazmy13.mediapicker.rxjava.observable;


import android.util.Log;
import net.alhazmy13.mediapicker.Image.ImagePicker;
import net.alhazmy13.mediapicker.rxjava.service.ImagePickerReceiver;

import rx.Observer;
import rx.Subscriber;

/**
 * Created by Alhazmy13 on 8/7/16.
 * MediaPicker
 */
public class ImagePickerObservable extends ImagePickerBaseObservable{

    private static final String TAG = "ImagePickerObservable";
    private ImagePicker.Builder imagePicker;
    public Observer<String> observer;
    private ImagePickerReceiver receiver;
    public ImagePickerObservable(ImagePicker.Builder imagePicker) {
        super(imagePicker.getContext());
        this.imagePicker = imagePicker;

    }


    @Override
    public void call(Subscriber subscriber) {
        super.call(subscriber);
        receiver = new ImagePickerReceiver(subscriber);
        imagePicker.build();
    }

    @Override
    public void onUnsubscribed() {
        //Log.d(TAG, "Unsubscribed");
    }




}
