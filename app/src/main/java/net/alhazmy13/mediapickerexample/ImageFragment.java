package net.alhazmy13.mediapickerexample;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * Created by alhazmy13 on 3/13/17.
 */

public class ImageFragment extends Fragment {
    private ImageView imageView;
    private TextView path;

    private static final String TAG = "MainActivity";
    private List<String> mPath;

    public static int CUSTOM_REQUEST_CODE = 9876;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_layout, container, false);

        // Find our View instances
        imageView = view.findViewById(R.id.iv_image);
        path = view.findViewById(R.id.tv_path);
        view.findViewById(R.id.bt_pick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        return view;
    }


    private void pickImage() {
        new ImagePicker.Builder(getActivity())
                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                .allowMultipleImages(true)
                .requestCode(CUSTOM_REQUEST_CODE)
                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(ImagePicker.Directory.DEFAULT)
                .extension(ImagePicker.Extension.PNG)
                .allowOnlineImages(false)
                .scale(600, 600)
                .allowMultipleImages(true)
                .enableDebuggingMode(true)
                .build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        if (requestCode == CUSTOM_REQUEST_CODE && resultCode == RESULT_OK) {
            mPath = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
            Log.d(TAG, "onActivityResult: ");
            loadImage();
        }
    }

    private void loadImage() {
        Log.d(TAG, "loadImage: " + mPath.size());
        if (mPath != null && mPath.size() > 0) {
            path.setText(mPath.get(0));
            imageView.setImageBitmap(BitmapFactory.decodeFile(mPath.get(0)));
        }
    }

}
