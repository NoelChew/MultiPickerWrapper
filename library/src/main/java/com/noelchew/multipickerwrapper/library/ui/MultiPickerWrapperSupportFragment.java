package com.noelchew.multipickerwrapper.library.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.noelchew.multipickerwrapper.library.MultiPickerWrapper;

/**
 * Created by noelchew on 15/08/2016.
 */
public abstract class MultiPickerWrapperSupportFragment extends Fragment {
    protected MultiPickerWrapper multiPickerWrapper;
    protected abstract MultiPickerWrapper.PickerUtilListener getMultiPickerWrapperListener();

    // override this optionally
    protected MultiPickerWrapper._CacheLocation getCacheLocation() {
        return MultiPickerWrapper._CacheLocation.EXTERNAL_STORAGE_APP_DIR;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        multiPickerWrapper = new MultiPickerWrapper(this, getCacheLocation());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (multiPickerWrapper.getPickerUtilListener() == null) {
            multiPickerWrapper.setPickerUtilListener(getMultiPickerWrapperListener());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // You have to save path in case your activity is killed.
        // In such a scenario, you will need to re-initialize the CameraImagePicker
        outState.putString("picker_path", multiPickerWrapper.pickerPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                multiPickerWrapper.pickerPath = savedInstanceState.getString("picker_path");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (multiPickerWrapper.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
