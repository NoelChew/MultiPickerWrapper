[![Release](https://jitpack.io/v/noelchew/MultiPickerWrapper.svg)](https://jitpack.io/#noelchew/MultiPickerWrapper)
# MultiPickerWrapper
Wrapper around [android-multipicker-library](https://github.com/coomar2841/android-multipicker-library) ([com.kbeanie:multipicker:1.1.1@aar](https://mvnrepository.com/artifact/com.kbeanie/multipicker/1.1.1))

Integrated with Runtime Permission library [PermisoWrapper](https://github.com/NoelChew/PermisoWrapper)

PermisoWrapper provides convenient methods to handle permission request. It is a wrapper around the fantastic [Permiso](https://github.com/greysonp/permiso) library.

Currently, this wrapper only supports image and video picking (and capturing).

# How to Use
## Activity
The Activity must extend MultiPickerWrapperAppCompatActivity:

```java
public class DemoActivity extends MultiPickerWrapperAppCompatActivity {
    // example method to pick multiple image
    private void pickMultipleImage() {
        multiPickerWrapper.getPermissionAndPickMultipleImage();
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
- getPermissionAndPickMultipleImage()
- getPermissionAndTakePicture()
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
    compile 'com.github.noelchew:MultiPickerWrapper:0.1.0'
}
```
Note: do not add the jitpack.io repository under buildscript
