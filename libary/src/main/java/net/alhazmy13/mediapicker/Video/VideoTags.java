package net.alhazmy13.mediapicker.Video;

/**
 * Created by Alhazmy13 on 2/8/16.
 * MediaPicker
 */
public class VideoTags {
    public static final class Tags {
        public static final String TAG = "VideoPicker";
        public static final String CAMERA_IMAGE_URI = "cameraVideoUri";
        public static final String VIDEO_PATH = "VIDEO_PATH";
        public static final String IMAGE_PICKER_DIR = "/mediapicker/videos/";
        public static final String IMG_CONFIG = "IMG_CONFIG";
        public static final String PICK_ERROR = "PICK_ERROR";
    }

    public static final class Action {
        public static final String SERVICE_ACTION = "net.alhazmy13.mediapicker.rxjava.video.service";
    }

    public final class IntentCode {
        public static final int REQUEST_CODE_SELECT_MULTI_PHOTO = 5341;
        public static final int CAMERA_REQUEST = 1888;
        public static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
        public static final int REQUEST_CODE_SELECT_PHOTO = 43;


    }

}
