<p align="left">
  <img src="https://cloud.githubusercontent.com/assets/4659608/12700433/4276edc0-c7f3-11e5-9f2c-de6bcbb9416d.png" width="600">
</p>
# Media Picker
![](https://img.shields.io/badge/Platform-Android-brightgreen.svg)
![](https://img.shields.io/crates/l/rustc-serialize.svg)
![](https://img.shields.io/badge/version-1.2.0-blue.svg)

------ 
Media Picker is an Android Libary that lets you to select multiple images, video or voice for Android 4.1 (API 16) +.
You can report any issue on issues page. **Note: If you speak Arabic, you can submit issues with Arabic language and I will check them. :)**


## Installation
------ 
**Maven**
```xml
<dependency>
<groupId>net.alhazmy13.MediaPicker</groupId>
<artifactId>libary</artifactId>
<version>1.2.0</version>
</dependency>
```


**Gradle**
```gradle
dependencies {
	compile 'net.alhazmy13.MediaPicker:libary:1.2.0'
}
```

# Usage
------ 
## Images
After adding the library, you need to:

1. Implement an `OnImageSetListener`
2. Create an object from `ImagePicker` 


### Implement an `OnImageSetListener`
In order to receive the path of image, you will need to implement the `OnImageSetListener`  interfaces. Typically this will  call camera activity and return the path of image.
```java
  @Override
    public void onImageSet(String path) {
       //imageView.setImageBitmap(BitmapFactory.decodeFile(path));
    }
```
### Create an `ImagePicker`
You will need to create a new instance of `ImagePicker`. Once the instance are configured, you can call `pick()`.
```java
         ImagePicker imagePicker=new ImagePicker(this);
        imagePicker.setOnImageSetListener(this);
        imagePicker.pick();
```


### Additional Options
* `setMode` to select the mode, you can chose one ot these `CAMERA`,`GALLERY` or `CAMERA_AND_GALLERY`
```java
imagePicker.setMode(ImagePicker.GALLERY);
```
 
* `SetExtanion` You can change the extanion of image to `PNG` or `JPG`
```java
imagePicker.setExtension(ImagePicker.PNG);
```
* `setCompressLevel` You can change the quality of image with three different levels `HARD`,`MEDIUM` or `SOFT`
```java
imagePicker.setCompressLevel(ImagePicker.MEDIUM);
```

* `setDirectory` You can pass the storage path, or You can select `ImagePicker.DEFAULT_DIR` to keep the default path.

```java
imagePicker.setDirectory(ImagePicker.DEFAULT_DIR);

//OR

imagePicker.setDirectory(Environment.getExternalStorageDirectory()+"/myFolder");

```
------ 

## Video
------ 
After adding the library, you need to:

1. Implement an `OnVideoSetListener`
2. Create an object from `VideoPicker` 


### Implement an `OnVideoSetListener`
In order to receive the path of video, you will need to implement the `OnVideoSetListener`  interfaces. Typically this will  call camera activity and return the path of video.
```java
 @Override
    public void OnVideoSet(String path) {
        //
    }
```

### Create a `VideoPicker`
You will need to create a new instance of `VideoPicker`. Once the instance are configured, you can call `pick()`.
```java
        VideoPicker videoPicker=new VideoPicker(this);
        videoPicker.setOnVideoSetListener(this);
        videoPicker.pick();
```


### Additional Options
* `SetExtanion` You can change the extanion of video to `Mp4`,`3gp` or `mkv`
```java
    videoPicker.setExtension(VideoPicker._MP4);
```
* `setDirectory` You can pass the storage path, or You can select `VideoPicker.DEFAULT_DIR` to keep the default path.

```java
videoPicker.setDirectory(VideoPicker.DEFAULT_DIR);

//OR

videoPicker.setDirectory(Environment.getExternalStorageDirectory()+"/myFolder");

```



## Theme the pickers

You can change the strings be overwriting below resources in your project.

```xml

    <string name="media_picker_select_from">Select From:</string>
    <string name="media_picker_camera">Camera</string>
    <string name="media_picker_gallery">Gallery</string>
```



## License
------ 
    Copyright 2015 alhazmy

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    

