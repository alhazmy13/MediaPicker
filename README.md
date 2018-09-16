<p align="left">
  <img src="https://cloud.githubusercontent.com/assets/4659608/12700433/4276edc0-c7f3-11e5-9f2c-de6bcbb9416d.png" width="600">
</p>

# Media Picker
![](https://img.shields.io/badge/Platform-Android-brightgreen.svg)
![](https://img.shields.io/hexpm/l/plug.svg)
![](https://img.shields.io/badge/version-2.4.1_beta-blue.svg)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/ea51407d60d04c938c263de206095abf)](https://www.codacy.com/app/me_101/MediaPicker?utm_source=github.com&utm_medium=referral&utm_content=alhazmy13/MediaPicker&utm_campaign=badger)


**[Please let me know if your application go to production via this link](https://docs.google.com/forms/d/e/1FAIpQLSe4Y5Fwn1mlEoD4RxjXQzTvL4mofhESuBlTkAPQhI7J_WqMDQ/viewform?c=0&w=1)**
------
Media Picker is an Android Libary that lets you to select multiple images, video or voice for Android 4.1 (API 16) +.
You can report any issue on issues page. **Note: If you speak Arabic, you can submit issues with Arabic language and I will check them. :)**

# NOTE
----
This build `2.x.x` will break backward compatibility and there are a lot of changes to improve the performance and fix a lot of Leak memory issues, So please read below document carefully.
## Installation
------
**Maven**

```xml
<dependency>
<groupId>net.alhazmy13.MediaPicker</groupId>
<artifactId>libary</artifactId>
<version>2.4.1-beta</version>
</dependency>
```


**Gradle**

```gradle
dependencies {
	implementation 'net.alhazmy13.MediaPicker:libary:2.4.1-beta'
}
```

# Usage
------
## Images
After adding the library, you need to:

1. Create an object from `ImagePicker` or `VideoPicker`
2. Override `onActivityResult` to receive the path of image or videos.



### Create an `ImagePicker`
You will need to create a new instance of `ImagePicker`. Once the instance are configured, you can call `build()`.

```java
        new ImagePicker.Builder(MainActivity.this)
                        .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                        .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                        .directory(ImagePicker.Directory.DEFAULT)
                        .extension(ImagePicker.Extension.PNG)
                        .scale(600, 600)
                        .allowMultipleImages(false)
                        .enableDebuggingMode(true)
                        .build();
```
### Override `onActivityResult `
In order to receive the path of image, you will need to override `onActivityResult ` .

```java
   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
            //Your Code
        }
    }
```

### Additional Options
* `mode` to select the mode, you can choose one of these `CAMERA`,`GALLERY` or `CAMERA_AND_GALLERY`

```java
.mode(ImagePicker.Mode.CAMERA)
```

* `extension` You can change the extension of image to `PNG` or `JPG`

```java
.extension(ImagePicker.Extension.PNG)
```
* `compressLevel` You can change the quality of image with three different levels `HARD`,`MEDIUM`, `SOFT` or `NONE`

```java
.compressLevel(ImagePicker.ComperesLevel.MEDIUM)
```

* `directory` You can pass the storage path, or select `Directory.DEFAULT_DIR` to keep the default path.

```java
.directory(ImagePicker.Directory.DEFAULT)

//OR

.directory(Environment.getExternalStorageDirectory()+"/myFolder")

```

* `scale` You can scale the image to a a minimum width and height. This will only be used if compressLevel is set. To avoid OutOfMemory issues, ensure this is used.

```java
.scale(500, 500)
```
* `allowMultipleImages` Extra used to select and return multiple images from gallery **CANNOT select single image from gallery if this feature was enabled**

```java
	.allowMultipleImages(true)
```

* `enableDebuggingMode` used to print Image Picker Log

```java
	.enableDebuggingMode(true)
```

* `allowOnlineImages` an option to allow the user to select any image from online resource ex: Google Drive **(KNOWN ISSUE) if you enable this option then you cannot select multiple images**

```java
	.allowOnlineImages(true)
```


### Create an `VideoPicker`
You will need to create a new instance of `VideoPicker`. Once the instance are configured, you can call `build()`.

```java
        new VideoPicker.Builder(MainActivity.this)
                        .mode(VideoPicker.Mode.CAMERA_AND_GALLERY)
                        .directory(VideoPicker.Directory.DEFAULT)
                        .extension(VideoPicker.Extension.MP4)
                        .enableDebuggingMode(true)
                        .build();
```
### Override `onActivityResult `
In order to receive the path of videos, you will need to override `onActivityResult ` .

```java
   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths =  data.getStringArrayListExtra(VideoPicker.EXTRA_VIDEO_PATH);
            //Your Code
        }
    }
```

### Additional Options
* `mode` to select the mode, you can choose one of these `CAMERA`,`GALLERY` or `CAMERA_AND_GALLERY`

```java
.mode(VideoPicker.Mode.CAMERA)
```

* `extension` You can change the extension of video to `MP4` 

```java
.extension(VideoPicker.Extension.MP4)
```

* `directory` You can pass the storage path, or select `Directory.DEFAULT_DIR` to keep the default path.

```java
.directory(VideoPicker.Directory.DEFAULT)

//OR

.directory(Environment.getExternalStorageDirectory()+"/myFolder")

```

* `enableDebuggingMode` used to print Video Picker Log

```java
	.enableDebuggingMode(true)
```

### RxJava 2 for MediaPicker

It's an extenstion that allow you to return an observable from `ImagePickerBuilder` or `VideoPickerBuilder`, all you need is to add below dependency and then return the observable from `ImagePickerHelper` || `VideoPickerHelper` class.


**Gradle**

```gradle
dependencies {
  implementation 'io.reactivex.rxjava2:rxandroid:(Last_version)'
  implementation 'io.reactivex.rxjava2:rxjava:(Last_version)'
	implementation 'net.alhazmy13.MediaPicker:rxjava:(Last_version)'
}
```

```Java
  new ImagePickerHelper(
        new ImagePicker.Builder(Context)
                ...)
                .getObservable()
                .subscribe(....);
```

------


## Theme the pickers

You can change the strings be overwriting below resources in your project.

```xml

    <string name="media_picker_select_from">Select From:</string>
    <string name="media_picker_camera">Camera</string>
    <string name="media_picker_gallery">Gallery</string>
    <string name="media_picker_ok">Ok</string>
    <string name="media_picker_cancel">Cancel</string>
    <string name="media_picker_some_permission_is_denied">Some Permission is Denied</string>
    <string name="media_picker_you_need_to_grant_access_to">You need to grant access to</string>
    <string name="media_picker_read_Write_external_storage"><![CDATA[Read & Write External Storage]]></string>
```



