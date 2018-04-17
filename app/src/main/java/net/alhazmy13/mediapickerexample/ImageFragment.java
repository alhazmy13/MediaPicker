package net.alhazmy13.mediapickerexample;

import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.alhazmy13.mediapicker.Image.ImagePicker;
import net.alhazmy13.mediapicker.rxjava.image.ImagePickerHelper;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by alhazmy13 on 3/13/17.
 */

public class ImageFragment extends Fragment {
    private ImageView imageView;
    private TextView path;

    private static final String TAG = "MainActivity";
    private List<String> mPath;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_layout, container, false);

        // Find our View instances
        imageView = (ImageView) view.findViewById(R.id.iv_image);
        path = (TextView) view.findViewById(R.id.tv_path);
        view.findViewById(R.id.bt_pick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        return view;
    }


    private void pickImage() {
        Observable<List<String>> x = new ImagePickerHelper(
                new ImagePicker.Builder(getActivity())
                        .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                        .allowMultipleImages(true)
                        .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                        .directory(ImagePicker.Directory.DEFAULT)
                        .extension(ImagePicker.Extension.PNG)
                        .allowOnlineImages(false)
                        .scale(600, 600)
                        .allowMultipleImages(true)
                        .enableDebuggingMode(true))
                .getObservable();

        x.subscribe(new Observer<List<String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<String> imagePath) {
                Log.d(TAG, "onNext() called with: " + "imagePath = [" + imagePath + "]");
                mPath = imagePath;
                loadImage();
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError() called with: " + "e = [" + e + "]");
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });

    }

    private void loadImage() {
        Log.d(TAG, "loadImage: " + mPath.size());
        path.setText(mPath.get(0));
        imageView.setImageBitmap(BitmapFactory.decodeFile(mPath.get(0)));
    }

}
