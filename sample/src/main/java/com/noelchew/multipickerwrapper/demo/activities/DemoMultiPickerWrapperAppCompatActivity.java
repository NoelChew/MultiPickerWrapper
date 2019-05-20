package com.noelchew.multipickerwrapper.demo.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kbeanie.multipicker.api.entity.ChosenAudio;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.noelchew.multipickerwrapper.demo.R;
import com.noelchew.multipickerwrapper.demo.helper.ActionHelper;
import com.noelchew.multipickerwrapper.library.MultiPickerWrapper;
import com.noelchew.multipickerwrapper.library.ui.MultiPickerWrapperAppCompatActivity;

import java.util.List;

public class DemoMultiPickerWrapperAppCompatActivity extends MultiPickerWrapperAppCompatActivity {

    Context context;

    RelativeLayout relativeLayoutImage;
    ImageView imageView;
    TextView tvData;
    Button btnCheckPermissions;

    @Override
    protected MultiPickerWrapper.PickerUtilListener getMultiPickerWrapperListener() {
        return multiPickerWrapperListener;
    }

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
        context = DemoMultiPickerWrapperAppCompatActivity.this;
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

}
