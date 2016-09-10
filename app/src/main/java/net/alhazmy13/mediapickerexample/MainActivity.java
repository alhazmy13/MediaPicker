package net.alhazmy13.mediapickerexample;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.alhazmy13.mediapicker.Image.ImagePicker;
import net.alhazmy13.mediapicker.rxjava.image.ImagePickerHelper;

import java.io.Serializable;
import java.util.List;

import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String IMAGE_PATH = "IMAGE_TAGS_IMAGE_PATH";
    private TextView textView;
    private ImageView imageView;
    private String videoPath;
    private  List<String> mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.tv_path);
        Button pickButton = (Button) findViewById(R.id.bt_pick);
        imageView = (ImageView) findViewById(R.id.iv_image);

        assert pickButton != null;
        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(IMAGE_PATH, (Serializable) mPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey(IMAGE_PATH)) {
            mPath = (List<String>) savedInstanceState.getSerializable(IMAGE_PATH);
            loadImage();
        }
    }

    private void pickImage() {
        new ImagePickerHelper(
                new ImagePicker.Builder(MainActivity.this)
                        .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                        .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                        .directory(ImagePicker.Directory.DEFAULT)
                        .extension(ImagePicker.Extension.PNG)
                        .scale(600, 600)
                        .enableDebuggingMode(true))
                .getObservable()
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted() called with: " + "");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError() called with: " + "e = [" + e + "]");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<String> imagePath) {
                        Log.d(TAG, "onNext() called with: " + "imagePath = [" + imagePath + "]");
                        mPath = imagePath;
                        loadImage();
                    }
                });
    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
//            videoPath = data.getStringExtra(VideoPicker.EXTRA_VIDEO_PATH);
//            loadImage();
//        }
//        else if(requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
//            mPath = (List<String>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_PATH);
//            loadImage();
//        }
//    }

    private void loadImage() {
        Log.d(TAG, "loadImage: "+mPath.size());
        textView.setText(mPath.get(0));
        imageView.setImageBitmap(BitmapFactory.decodeFile(mPath.get(0)));
    }

}