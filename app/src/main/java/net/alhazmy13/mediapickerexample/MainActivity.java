package net.alhazmy13.mediapickerexample;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.alhazmy13.mediapicker.Image.ImagePicker;
import net.alhazmy13.mediapicker.Video.VideoPicker;

public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "MainActivity";
    private static final String IMAGE_PATH = "IMAGE_PATH";

    private TextView textView;
    private ImageView imageView;

    private String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.tv_path);
        Button pickButton = (Button) findViewById(R.id.bt_pick);
        imageView = (ImageView) findViewById(R.id.iv_image);

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

        if (!TextUtils.isEmpty(mPath)) {
            outState.putString(IMAGE_PATH, mPath);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey(IMAGE_PATH)) {
            mPath = savedInstanceState.getString(IMAGE_PATH);
            loadImage();
        }
    }

    private void pickImage() {
        new ImagePicker.Builder(MainActivity.this)
                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                .compressLevel(ImagePicker.ComperesLevel.HARD)
                .directory(ImagePicker.Directory.DEFAULT)
                .scale(300, 300)
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            mPath = data.getStringExtra(VideoPicker.EXTRA_VIDEO_PATH);
            loadImage();
        }
        else if(requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            mPath = data.getStringExtra(ImagePicker.EXTRA_IMAGE_PATH);
            loadImage();
        }
    }

    private void loadImage() {
        textView.setText(mPath);
        imageView.setImageBitmap(BitmapFactory.decodeFile(mPath));
    }

}