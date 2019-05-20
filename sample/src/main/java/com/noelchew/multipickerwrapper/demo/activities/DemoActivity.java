package com.noelchew.multipickerwrapper.demo.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.greysonparrelli.permiso.Permiso;
import com.kbeanie.multipicker.api.entity.ChosenAudio;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.noelchew.multipickerwrapper.demo.R;
import com.noelchew.multipickerwrapper.demo.helper.ActionHelper;
import com.noelchew.multipickerwrapper.library.MultiPickerWrapper;

import java.util.List;

public class DemoActivity extends AppCompatActivity {

    Context context;

    RelativeLayout relativeLayoutImage;
    ImageView imageView;
    TextView tvData;
    Button btnCheckPermissions;

    // define multiPickerWrapper and it's callback
    private MultiPickerWrapper multiPickerWrapper;
    private MultiPickerWrapper._CacheLocation cacheLocation = MultiPickerWrapper._CacheLocation.EXTERNAL_CACHE_DIR;
    MultiPickerWrapper.PickerUtilListener multiPickerWrapperListener = new MultiPickerWrapper.PickerUtilListener() {
        @Override
        public void onPermissionDenied() {
            ActionHelper.onPermissionDenied(context);
        }

        @Override
        public void onImagesChosen(List<ChosenImage> list) {
            ActionHelper.onImagesChosen(list, context, tvData, imageView);
        }

        @Override
        public void onVideosChosen(List<ChosenVideo> list) {
            ActionHelper.onVideosChosen(list, context, tvData, imageView);
        }

        @Override
        public void onAudiosChosen(List<ChosenAudio> list) {
            ActionHelper.onAudiosChosen(list, context, tvData, imageView);
        }

        @Override
        public void onFilesChosen(List<ChosenFile> list) {
            ActionHelper.onFilesChosen(list, context, tvData, imageView);
        }

        @Override
        public void onError(String s) {
            ActionHelper.onError(s, context);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = DemoActivity.this;

        // * must add these lines for multipickerwrapper library --- begin ---- *
        Permiso.getInstance().setActivity(this);
        multiPickerWrapper = new MultiPickerWrapper(this, cacheLocation);
        // * must add these lines for multipickerwrapper library --- end ---- *

        setContentView(R.layout.activity_demo);

        relativeLayoutImage = (RelativeLayout) findViewById(R.id.relative_layout_image);
        imageView = (ImageView) findViewById(R.id.image_view);
        tvData = (TextView) findViewById(R.id.text_view_selected_data);
        btnCheckPermissions = (Button) findViewById(R.id.button_check_permissions);

        relativeLayoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionHelper.onImageClicked(context, multiPickerWrapper);
            }
        });
        tvData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionHelper.onTvDataClicked(context);
            }
        });

        btnCheckPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionHelper.onBtnCheckPermissionsOnClicked(context);
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
