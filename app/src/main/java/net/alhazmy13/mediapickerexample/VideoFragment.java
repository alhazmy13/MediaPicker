package net.alhazmy13.mediapickerexample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import net.alhazmy13.mediapicker.Video.VideoPicker;

import java.util.List;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
            mPath = data.getStringArrayListExtra(VideoPicker.EXTRA_VIDEO_PATH);
            loadVideo();
        }
    }

    private void loadVideo() {
        if (mPath != null && mPath.size() > 0) {
            path.setText(mPath.get(0));
            videoView.setVideoURI(Uri.parse(mPath.get(0)));
            videoView.start();
        }
    }
}
