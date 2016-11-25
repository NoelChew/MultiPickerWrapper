package com.noelchew.multipickerwrapper.library.ui;

import android.content.Intent;
import android.os.Bundle;

import com.greysonparrelli.permiso.PermisoActivity;
import com.noelchew.multipickerwrapper.library.MultiPickerWrapper;

/**
 * Created by noelchew on 15/08/2016.
 */
public abstract class MultiPickerWrapperAppCompatActivity extends PermisoActivity {
    protected MultiPickerWrapper multiPickerWrapper;
    protected abstract MultiPickerWrapper.PickerUtilListener getMultiPickerWrapperListener();

    // override this optionally
    protected MultiPickerWrapper._CacheLocation getCacheLocation() {
        // TODO warning: setting this to internal app directory means only this app can access the file
        return MultiPickerWrapper._CacheLocation.EXTERNAL_CACHE_DIR;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        multiPickerWrapper = new MultiPickerWrapper(this, getCacheLocation());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (multiPickerWrapper.getPickerUtilListener() == null) {
            multiPickerWrapper.setPickerUtilListener(getMultiPickerWrapperListener());
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (multiPickerWrapper.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
