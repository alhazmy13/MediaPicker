package net.alhazmy13.mediapicker.adapter_rxjava;
import net.alhazmy13.mediapicker.Image.ImagePicker;
import net.alhazmy13.mediapicker.adapter_rxjava.observable.ImagePickerObservable;

import rx.Observable;

/**
 * Created by Alhazmy13 on 8/7/16.
 * MediaPicker
 */
public class ImagePickerHelper {
    private ImagePicker.Builder mBuilder;
    public ImagePickerHelper(ImagePicker.Builder builder){
        this.mBuilder = builder;
    }

    public  Observable<String> getObservable(){
        return Observable.create(new ImagePickerObservable(mBuilder));
    }

}
