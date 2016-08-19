# MultiPickerWrapper
[![Release](https://jitpack.io/v/noelchew/MultiPickerWrapper.svg)](https://jitpack.io/#noelchew/MultiPickerWrapper)

- pick single or multiple image/video
- capture single image/video
- crop image immediately after selection (optional)
- library handles Runtime Permission
- combination of the following libraries:
    -  [android-multipicker-library](https://github.com/coomar2841/android-multipicker-library) ([com.kbeanie:multipicker:1.1.1@aar](https://mvnrepository.com/artifact/com.kbeanie/multipicker/1.1.1))
    - [PermisoWrapper](https://github.com/NoelChew/PermisoWrapper) ([com.github.noelchew:PermisoWrapper:0.1.1](https://github.com/NoelChew/PermisoWrapper/releases/tag/0.1.1))
    - [UCrop](https://github.com/Yalantis/uCrop) ([com.yalantis:ucrop:2.1.2](https://mvnrepository.com/artifact/com.yalantis/ucrop/2.1.2))

# How to Use
## Activity
The Activity must extend MultiPickerWrapperAppCompatActivity:

```java
public class DemoActivity extends MultiPickerWrapperAppCompatActivity {
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
        options.setOvalDimmedLayer(true);
        
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

## Fragment or SupportFragment
For Fragment or SupportFragment, the Activity containing the fragment must extend PermisoActivity:

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

## Integration
This library is hosted by jitpack.io.

Root level gradle:
```
allprojects {
 repositories {
    jcenter()
    maven { url "https://jitpack.io" }
 }
}
```

Application level gradle:
```
dependencies {
    compile 'com.github.noelchew:MultiPickerWrapper:0.1.1'
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
