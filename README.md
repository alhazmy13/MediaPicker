# Media Picker
This library for Android allowing the selection of multiple images, video or voice  for Android 4.1 (API 16) +.

You can report any issue on issues page. **Note: If you speak Arabic, you can submit issues with Arabic language and I will check them. :)**

## Installation
**Maven**
```xml
<dependency>
<groupId>net.alhazmy13.mediapicker</groupId>
<artifactId>libary</artifactId>
<version>1.0.2-beta</version>
</dependency>
```
**Gradle**
```gradle

dependencies {
	compile 'net.alhazmy13.mediapicker:libary:1.0.2-neta'
}
```

# Usage
After adding the library, you need to:

1. Implement an `OnImageSetListener`
2. Create an object from `CameraPicker` 


### Implement an `OnImageSetListener`
In order to receive the path of image, you will need to implement the `OnImageSetListener`  interfaces. Typically this will  call camera activity and return the path of image.
```java
  @Override
    public void onImageSet(String path) {
       //imageView.setImageBitmap(BitmapFactory.decodeFile(path));

    }
```

### Create a `CameraPicker`
You will need to create a new instance of `CameraPicker`. Once the instance are configured, you can call `pick()`.
```java
        CameraPicker cameraPicker=new CameraPicker(this);
        cameraPicker.setOnImagePicked(this);
        cameraPicker.pick();
```


### Additional Options
* `SetExtanion` You can change the extanion of iamge to `PNG` or `JPG`
```java
cameraPicker.setExtension(CameraPicker.PNG);
```

### Comming Fetaures
* Resize, compress, filter and optimize image.
* Pick/Select Image from Gallery.
* Record video and voice.
* etc...

## License

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
    

