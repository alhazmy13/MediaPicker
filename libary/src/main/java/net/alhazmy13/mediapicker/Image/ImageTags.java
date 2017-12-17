package net.alhazmy13.mediapicker.Image;

public class ImageTags {
    public static final class Tags{
        public static final String TAG = "GifPicker";
        public static final String LEVEL = "level";
        public static final String EXTENSION = "extension";
        public static final String MODE = "mode";
        public static final String DIRECTORY = "DIRECTORY";
        public static final String CAMERA_IMAGE_URI = "cameraImageUri";
        public static final String COMPRESS_LEVEL = "COMPRESS_LEVEL";
        public static final String REQUESTED_WIDTH = "REQUESTED_WIDTH";
        public static final String REQUESTED_HEIGHT = "REQUESTED_HEIGHT";
        public static final String IMAGE_PATH = "GIF_PATH";
        public static final String ALLOW_MULTIPLE = "ALLOW_MULTIPLE";
        public static final String DEBUG = "DEBUG";
        public static final String IMAGE_PICKER_DIR = "/mediapicker/images/";
        public static final String IMG_CONFIG = "GIF_CONFIG";
        public static final String PICK_ERROR = "PICK_ERROR";
    }

    public static final class Action{
        public static final String SERVICE_ACTION = "net.alhazmy13.mediapicker.rxjava.image.service";
    }

    public final class IntentCode{
        public static final int REQUEST_CODE_SELECT_MULTI_PHOTO = 5341;
        public static final int CAMERA_REQUEST = 1888;
        public static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
        public static final int REQUEST_CODE_SELECT_PHOTO = 43;


    }
   

}
