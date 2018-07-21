package net.alhazmy13.mediapicker.rxjava.image;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by Alhazmy13 on 8/7/16.
 * MediaPicker
 */
public class ImagePickerHelper {
    private ImagePicker.Builder mBuilder;

    public ImagePickerHelper(ImagePicker.Builder builder) {
        this.mBuilder = builder;
    }

    public Observable<List<String>> getObservable() {
        return Observable.create(new ImagePickerObservable(mBuilder));
    }

}
