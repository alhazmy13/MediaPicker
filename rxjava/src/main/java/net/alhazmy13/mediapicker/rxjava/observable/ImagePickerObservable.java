package net.alhazmy13.mediapicker.rxjava.observable;


import android.content.IntentFilter;
import android.util.Log;

import net.alhazmy13.mediapicker.Image.ImagePicker;
import net.alhazmy13.mediapicker.Image.ImageTags;
import net.alhazmy13.mediapicker.rxjava.service.ImagePickerReceiver;

import java.util.List;

import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * Created by Alhazmy13 on 8/7/16.
 * MediaPicker
 */
public class ImagePickerObservable extends ImagePickerBaseObservable {

    private static final String TAG = "ImagePickerObservable";
    public Observer<List<String>> observer;
    private ImagePicker.Builder imagePicker;
    private ImagePickerReceiver receiver;

    public ImagePickerObservable(ImagePicker.Builder imagePicker) {
        super(imagePicker.getContext());
        this.imagePicker = imagePicker;

    }


    @Override
    public void call(final Subscriber subscriber) {
        super.call(subscriber);
        receiver = new ImagePickerReceiver(subscriber);
        imagePicker.build();
        registerImagePickerObservable();
        subscriber.add(Subscriptions.create(new Action0() {
            @Override
            public void call() {
                subscriber.unsubscribe();
                onUnsubscribed();
            }
        }));
    }

    @Override
    public void registerImagePickerObservable() {
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(RECEIVER_ACTION), 0);
        context.registerReceiver(receiver, new IntentFilter(ImageTags.Action.SERVICE_ACTION));

    }

    @Override
    public void onUnsubscribed() {
        Log.d(TAG, "onUnsubscribed() called with: " + "");
        try {
            context.unregisterReceiver(receiver);
        } catch (IllegalArgumentException ignored) {
        }

    }


}
