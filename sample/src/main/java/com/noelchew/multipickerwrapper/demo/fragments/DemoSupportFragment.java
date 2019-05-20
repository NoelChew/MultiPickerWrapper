package com.noelchew.multipickerwrapper.demo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.noelchew.multipickerwrapper.library.ui.MultiPickerWrapperSupportFragment;

import java.util.List;

/**
 * Created by noelchew on 16/08/2016.
 */
public class DemoSupportFragment extends MultiPickerWrapperSupportFragment {

    Context context;

    RelativeLayout relativeLayoutImage;
    ImageView imageView;
    TextView tvData;
    Button btnCheckPermissions;

    String filePath;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_demo, null);
        context = getActivity();

        relativeLayoutImage = (RelativeLayout) view.findViewById(R.id.relative_layout_image);
        imageView = (ImageView) view.findViewById(R.id.image_view);
        tvData = (TextView) view.findViewById(R.id.text_view_selected_data);
        btnCheckPermissions = (Button) view.findViewById(R.id.button_check_permissions);

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

        return view;
    }

}
