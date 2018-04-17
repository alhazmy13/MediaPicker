package net.alhazmy13.mediapicker.rxjava.video.observable;


import android.content.IntentFilter;
import android.util.Log;

import net.alhazmy13.mediapicker.Video.VideoPicker;
import net.alhazmy13.mediapicker.Video.VideoTags;
import net.alhazmy13.mediapicker.rxjava.video.service.VideoPickerReceiver;

import org.reactivestreams.Subscriber;

import java.util.List;

import io.reactivex.ObservableEmitter;

/**
 * Created by Alhazmy13 on 8/7/16.
 * MediaPicker
 */
public class VideoPickerObservable extends VideoPickerBaseObservable {

    private static final String TAG = "VideoPickerObservable";
    private VideoPicker.Builder mVideoPicker;
    private VideoPickerReceiver mReceiver;

    public VideoPickerObservable(VideoPicker.Builder videoPicker) {
        super(videoPicker.getContext());
        this.mVideoPicker = videoPicker;

    }




    @Override
    public void subscribe(ObservableEmitter<List<String>> emitter) {
        context.registerReceiver(mReceiver, new IntentFilter(VideoTags.Action.SERVICE_ACTION));
        mReceiver = new VideoPickerReceiver(emitter);
        mVideoPicker.build();

    }
}
