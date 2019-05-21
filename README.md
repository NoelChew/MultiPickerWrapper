# MultiPickerWrapper
[![Release](https://jitpack.io/v/noelchew/MultiPickerWrapper.svg)](https://jitpack.io/#noelchew/MultiPickerWrapper)

- pick single or multiple image/video/audio
- capture single image/video
- crop image immediately after selection (optional)
- pick single/multiple file with specified mime type
- library handles Runtime Permission
- combination of the following libraries:
    -  [android-multipicker-library](https://github.com/coomar2841/android-multipicker-library) ([com.kbeanie:multipicker:1.1.31@aar](https://github.com/coomar2841/android-multipicker-library/releases/tag/v1.1.31))
    - [PermisoWrapper](https://github.com/NoelChew/PermisoWrapper) ([com.github.noelchew:PermisoWrapper:0.2.0](https://github.com/NoelChew/PermisoWrapper/releases/tag/0.2.0))
    - [UCrop](https://github.com/Yalantis/uCrop) ([com.yalantis:ucrop:2.2.0](https://mvnrepository.com/artifact/com.yalantis/ucrop/2.2.0))

#### Optionally allows user to crop image taken or selected.

![screenshot1](https://github.com/NoelChew/MultiPickerWrapper/blob/master/screenshots/screenshot_1.png)

# How to Use
## Activity
The Activity can either extend MultiPickerWrapperAppCompatActivity:

```java
public class DemoMultiPickerWrapperAppCompatActivity extends MultiPickerWrapperAppCompatActivity {
    // example method to pick multiple image
    private void pickCroppedImage() {
    
        // configure cropping activity UI to match current theme colour
        UCrop.Options options = new UCrop.Options();
        options.setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        options.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        options.setCropFrameColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        options.setCropFrameStrokeWidth(PixelUtil.dpToPx(4));
        options.setCropGridColor(ContextCompat.getColor(context, R.color.colorPrimary));
        options.setCropGridStrokeWidth(PixelUtil.dpToPx(2));
        options.setActiveWidgetColor(ContextCompat.getColor(context, R.color.colorPrimary));
        options.setToolbarTitle("MultiPickerWrapper - Crop");
        
        // set rounded cropping guide
        options.setCircleDimmedLayer(true);
        
        // set aspectRatioWidth and Height of 1 -> gives square rounded image cropping
        multiPickerWrapper.getPermissionAndPickSingleImageAndCrop(options, 1, 1);
    }

    @Override
    protected MultiPickerWrapper.PickerUtilListener getMultiPickerWrapperListener() {
        return multiPickerWrapperListener;
    }
    
    MultiPickerWrapper.PickerUtilListener multiPickerWrapperListener = new MultiPickerWrapper.PickerUtilListener() {
        @Override
        public void onPermissionDenied() {
            // do something here
        }
        
        @Override
        public void onImagesChosen(List<ChosenImage> list) {
            String filePath = list.get(0).getOriginalPath();
            Uri uri = Uri.fromFile(new File(filePath));
            // do something here with filePath or uri
        }

        @Override
        public void onVideosChosen(List<ChosenVideo> list) {
            String filePath = list.get(0).getOriginalPath();
            String thumbnailPath = list.get(0).getPreviewThumbnail();
            Uri uri = Uri.fromFile(new File(filePath));
            // do something with filePath or uri
        }

        @Override
        public void onError(String s) {
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
        }
    };
    
    // rest of the activity code...
    
}

```

or override the following methods in Activity:
```java
public class DemoActivity extends AppCompatActivity {

    // define multiPickerWrapper and it's callback
    private MultiPickerWrapper multiPickerWrapper;
    private MultiPickerWrapper._CacheLocation cacheLocation = MultiPickerWrapper._CacheLocation.EXTERNAL_CACHE_DIR;
    MultiPickerWrapper.PickerUtilListener multiPickerWrapperListener = new MultiPickerWrapper.PickerUtilListener() {
        @Override
        public void onPermissionDenied() {
            // do something here
        }

        @Override
        public void onImagesChosen(List<ChosenImage> list) {
            // do something here
        }

        @Override
        public void onVideosChosen(List<ChosenVideo> list) {
            // do something here
        }

        @Override
        public void onAudiosChosen(List<ChosenAudio> list) {
            // do something here
        }

        @Override
        public void onFilesChosen(List<ChosenFile> list) {
            // do something here
        }

        @Override
        public void onError(String s) {
            // do something here
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // * must add these lines for multipickerwrapper library --- begin ---- *
        Permiso.getInstance().setActivity(this);
        multiPickerWrapper = new MultiPickerWrapper(this, cacheLocation);
        // * must add these lines for multipickerwrapper library --- end ---- *

    }
    
    // * must add these lines for multipickerwrapper library --- begin ---- *
    @Override
    public void onResume() {
        super.onResume();
        Permiso.getInstance().setActivity(this);
        if (multiPickerWrapper.getPickerUtilListener() == null) {
            multiPickerWrapper.setPickerUtilListener(multiPickerWrapperListener);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // You have to save path in case your activity is killed.
        // In such a scenario, you will need to re-initialize the CameraImagePicker
        outState.putString("picker_path", multiPickerWrapper.pickerPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // After Activity recreate, you need to re-initialize these
        // two values to be able to re-initialize CameraImagePicker
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                multiPickerWrapper.pickerPath = savedInstanceState.getString("picker_path");
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (multiPickerWrapper.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    // * must add these lines for multipickerwrapper library --- end ---- *

}

```

## Fragment or SupportFragment
For Fragment or SupportFragment, the Activity containing the fragment can either extend PermisoActivity:

```java
public class DemoSupportFragmentActivity extends PermisoActivity {
    // activity code here
}
```

or override the following methods in Activity:

```java
// This example is not extending PermisoActivity on purpose. Look out for comments surrounded by *
public class DemoSupportFragmentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // * must add this line *
        Permiso.getInstance().setActivity(this);

        setContentView(R.layout.activity_fragment_demo);
        DemoSupportFragment fragment = new DemoSupportFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        // * must add this line *
        Permiso.getInstance().setActivity(this);
    }

    // * must add this *
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);
    }
}
```

Similarly, the Fragment or SupportFragment must extend MultiPickerWrapperFragment or MultiPickerWrapperSupportFragment:

```java
public class DemoSupportFragment extends MultiPickerWrapperSupportFragment {
    // example method to record video
    private void recordVideo() {
        multiPickerWrapper.getPermissionAndTakeVideo();
    }

    @Override
    protected MultiPickerWrapper.PickerUtilListener getMultiPickerWrapperListener() {
        return multiPickerWrapperListener;
    }

    MultiPickerWrapper.PickerUtilListener multiPickerWrapperListener = new MultiPickerWrapper.PickerUtilListener() {
        @Override
        public void onPermissionDenied() {
            // do something here
        }
        
        @Override
        public void onImagesChosen(List<ChosenImage> list) {
            String filePath = list.get(0).getOriginalPath();
            Uri uri = Uri.fromFile(new File(filePath));
            // do something here with filePath or uri
        }

        @Override
        public void onVideosChosen(List<ChosenVideo> list) {
            String filePath = list.get(0).getOriginalPath();
            String thumbnailPath = list.get(0).getPreviewThumbnail();
            Uri uri = Uri.fromFile(new File(filePath));
            // do something with filePath or uri
        }

        @Override
        public void onError(String s) {
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
        }
    };
    
    // rest of the fragment code
    
}
```
## List of Predefined Methods
- getPermissionAndPickSingleImage()
- getPermissionAndPickSingleImageAndCrop()
- getPermissionAndPickMultipleImage()
- getPermissionAndTakePicture()
- getPermissionAndTakePictureAndCrop()
- getPermissionAndPickSingleVideo()
- getPermissionAndTakeVideo()
- getPermissionAndTakeVideoWithDurationLimit()
- getPermissionAndPickSingleFile()
- getPermissionAndPickMultipleFile()
- getPermissionAndPickAudio()
- getPermissionAndPickMultipleAudio()

## Integration
This library is hosted by jitpack.io.

Root level gradle:
```
allprojects {
 repositories {
    jcenter()
    google()
    maven { url "https://jitpack.io" }
 }
}
```

Application level gradle:[![Release](https://jitpack.io/v/noelchew/MultiPickerWrapper.svg)](https://jitpack.io/#noelchew/MultiPickerWrapper)
```
dependencies {
    implementation 'com.github.noelchew:MultiPickerWrapper:x.y.z'
}
```
Note: do not add the jitpack.io repository under buildscript


## Proguard
```
# UCrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

# OkHttp (included in UCrop library)
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**
```
