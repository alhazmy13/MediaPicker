package net.alhazmy13.mediapickerexample;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import net.alhazmy13.mediapicker.Video.VideoPicker;
import net.alhazmy13.mediapicker.rxjava.video.VideoPickerHelper;

import java.util.List;

import rx.Subscriber;

import static android.app.Activity.RESULT_OK;

/**
 * Created by alhazmy13 on 3/13/17.
 */

public class VideoFragment extends Fragment {
    private VideoView videoView;
    private TextView path;

    private static final String TAG = "MainActivity";
    private List<String> mPath;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_layout, container, false);
        init(view);

        return view;
    }

    private void init(View view) {
        // Find our View instances
        videoView = (VideoView) view.findViewById(R.id.iv_video);
        MediaController mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.start();
        path = (TextView) view.findViewById(R.id.tv_path);
        view.findViewById(R.id.bt_pick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickVideo();
            }
        });
    }


    private void pickVideo() {
        new VideoPicker.Builder(getActivity())
                .mode(VideoPicker.Mode.CAMERA_AND_GALLERY)
                .directory(VideoPicker.Directory.DEFAULT)
                .extension(VideoPicker.Extension.MP4)
                .enableDebuggingMode(true)
                .build();
    }

    //
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            mPath = (List<String>) data.getSerializableExtra(VideoPicker.EXTRA_VIDEO_PATH);
            Log.d(TAG, "onActivityResult: ");
            loadVideo();
        }
    }

    private void loadVideo() {
        Log.d(TAG, "loadVideo: "+ (mPath == null));
        if (mPath != null && mPath.size() > 0) {
            Log.d(TAG, "loadVideo: ");
            Log.d(TAG, "loadImage: " + mPath.size());
            path.setText(mPath.get(0));
            videoView.setVideoURI(Uri.parse(mPath.get(0)));
            videoView.start();
        }
    }
}
