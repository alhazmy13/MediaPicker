package net.alhazmy13.mediapicker.Image;

public class ImageTags {
    public static final class Tags {
        public static final String TAG = "ImagePicker";
        public static final String CAMERA_IMAGE_URI = "cameraImageUri";
        public static final String IMAGE_PATH = "IMAGE_PATH";
        public static final String IMAGE_PICKER_DIR = "/mediapicker/images/";
        public static final String IMG_CONFIG = "IMG_CONFIG";
        public static final String PICK_ERROR = "PICK_ERROR";
        public static final String IS_ALERT_SHOWING = "IS_ALERT_SHOWING";
    }

    public static final class Action {
        public static final String SERVICE_ACTION = "net.alhazmy13.mediapicker.rxjava.image.service";
    }

    public final class IntentCode {
        public static final int REQUEST_CODE_SELECT_MULTI_PHOTO = 5341;
        public static final int CAMERA_REQUEST = 1888;
        public static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
        public static final int REQUEST_CODE_SELECT_PHOTO = 43;


    }


}
