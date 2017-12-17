package net.alhazmy13.mediapicker.rxjava.video.observable;


import android.content.IntentFilter;
import android.util.Log;

import net.alhazmy13.mediapicker.Video.VideoPicker;
import net.alhazmy13.mediapicker.Video.VideoTags;
import net.alhazmy13.mediapicker.rxjava.video.service.VideoPickerReceiver;

import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * Created by Alhazmy13 on 8/7/16.
 * MediaPicker
 */
public class VideoPickerObservable extends VideoPickerBaseObservable {

    private VideoPicker.Builder mVideoPicker;
    private VideoPickerReceiver mReceiver;

    public VideoPickerObservable(VideoPicker.Builder videoPicker) {
        super(videoPicker.getContext());
        this.mVideoPicker = videoPicker;

    }


    @Override
    public void call(final Subscriber subscriber) {
        super.call(subscriber);
        mReceiver = new VideoPickerReceiver(subscriber);
        mVideoPicker.build();
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
        context.registerReceiver(mReceiver, new IntentFilter(VideoTags.Action.SERVICE_ACTION));

    }

    @Override
    public void onUnsubscribed() {
        try {
            context.unregisterReceiver(mReceiver);
        } catch (IllegalArgumentException ignored) {
        }

    }


}
