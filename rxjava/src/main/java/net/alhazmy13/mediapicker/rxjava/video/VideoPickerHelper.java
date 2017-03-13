package net.alhazmy13.mediapicker.rxjava.video;
import net.alhazmy13.mediapicker.Image.ImagePicker;
import net.alhazmy13.mediapicker.Video.VideoPicker;
import net.alhazmy13.mediapicker.rxjava.video.observable.VideoPickerObservable;

import java.util.List;

import rx.Observable;

/**
 * Created by Alhazmy13 on 8/7/16.
 * MediaPicker
 */
public class VideoPickerHelper {
    private VideoPicker.Builder mBuilder;
    public VideoPickerHelper(VideoPicker.Builder builder){
        this.mBuilder = builder;
    }

    public Observable<List<String>> getObservable(){
        return Observable.create(new VideoPickerObservable(mBuilder));
    }

}
