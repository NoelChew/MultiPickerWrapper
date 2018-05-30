package com.noelchew.multipickerwrapper.demo.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.noelchew.multipickerwrapper.demo.R;
import com.noelchew.multipickerwrapper.demo.utils.PixelUtil;
import com.noelchew.multipickerwrapper.library.MultiPickerWrapper;
import com.noelchew.multipickerwrapper.library.ui.MultiPickerWrapperSupportFragment;
import com.noelchew.ncutils.alert.AlertDialogUtil;
import com.noelchew.permisowrapper.PermisoWrapper;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
    boolean isVideo = false;

    @Override
    protected MultiPickerWrapper.PickerUtilListener getMultiPickerWrapperListener() {
        return multiPickerWrapperListener;
    }

    MultiPickerWrapper.PickerUtilListener multiPickerWrapperListener = new MultiPickerWrapper.PickerUtilListener() {
        @Override
        public void onPermissionDenied() {
            Toast.makeText(context, "Permission denied.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onImagesChosen(List<ChosenImage> list) {
            final Uri uri = Uri.fromFile(new File(list.get(0).getOriginalPath()));
            filePath = list.get(0).getOriginalPath();
            tvData.setText(getString(R.string.show_data) + filePath);
            isVideo = false;
            AlertDialogUtil.showAlertDialogWithSelections(context,
                    "Image Selected:",
                    new ArrayList<>(Arrays.asList(new String[]{"Show", "Cancel"})),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setDataAndType(uri, "image/*");
                                startActivity(intent);
                            }
                        }
                    });

            Glide.with(context).load(uri).into(imageView);
        }

        @Override
        public void onVideosChosen(List<ChosenVideo> list) {
            final Uri uri = Uri.fromFile(new File(list.get(0).getOriginalPath()));
            filePath = list.get(0).getOriginalPath();
            tvData.setText(getString(R.string.show_data) + filePath);
            isVideo = true;
            AlertDialogUtil.showAlertDialogWithSelections(context,
                    "Video Selected:",
                    new ArrayList<>(Arrays.asList(new String[]{"Show", "Cancel"})),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setDataAndType(uri, "video/*");
                                startActivity(intent);
                            }
                        }
                    });

            Glide.with(context).load(list.get(0).getPreviewThumbnail()).into(imageView);
        }

        @Override
        public void onError(String s) {
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
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

        relativeLayoutImage.setOnClickListener(relativeLayoutImageOnClickListener);
        tvData.setOnClickListener(tvDataOnClickListener);

        btnCheckPermissions = (Button) view.findViewById(R.id.button_check_permissions);
        btnCheckPermissions.setOnClickListener(btnCheckPermissionsOnClickListener);

        return view;
    }

    private View.OnClickListener relativeLayoutImageOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialogUtil.showAlertDialogWithSelections(context,
                    "Do you want to select image or video?",
                    new ArrayList<>(Arrays.asList(new String[]{"Image", "Image + Crop", "Video"})),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                AlertDialogUtil.showAlertDialogWithSelections(context,
                                        "Image",
                                        new ArrayList<>(Arrays.asList(new String[]{"Gallery", "Camera"})),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which) {
                                                    case 0:
                                                        // choose image from gallery
                                                        multiPickerWrapper.getPermissionAndPickSingleImage();
                                                        break;

                                                    case 1:
                                                        // take image with camera
                                                        multiPickerWrapper.getPermissionAndTakePicture();
                                                        break;
                                                }
                                            }
                                        });
                            }
                            if (which == 1) {
                                AlertDialogUtil.showAlertDialogWithSelections(context,
                                        "Crop Image",
                                        new ArrayList<>(Arrays.asList(new String[]{"Gallery", "Camera"})),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                UCrop.Options options = new UCrop.Options();
                                                options.setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                                                options.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
                                                options.setToolbarTitle("MultiPickerWrapper - Crop");

                                                options.setCropFrameColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                                                options.setCropFrameStrokeWidth(PixelUtil.dpToPx(4));

                                                options.setCropGridColor(ContextCompat.getColor(context, R.color.colorPrimary));
                                                options.setCropGridStrokeWidth(PixelUtil.dpToPx(2));

                                                options.setCircleDimmedLayer(true);

                                                options.setActiveWidgetColor(ContextCompat.getColor(context, R.color.colorPrimary));
//                                                options.setAspectRatioOptions(0, new AspectRatio[]{new AspectRatio("Profile Picture", 1, 1), new AspectRatio("Cover Picture", 16, 9)});

                                                switch (which) {
                                                    case 0:
                                                        // choose image from gallery
                                                        multiPickerWrapper.getPermissionAndPickSingleImageAndCrop(options, 1, 1);
                                                        break;

                                                    case 1:
                                                        // take image with camera
                                                        multiPickerWrapper.getPermissionAndTakePictureAndCrop(options, 1, 1);
                                                        break;
                                                }
                                            }
                                        });
                            } else if (which == 2) {
                                AlertDialogUtil.showAlertDialogWithSelections(context,
                                        "Video",
                                        new ArrayList<>(Arrays.asList(new String[]{"Gallery", "Camera"})),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which) {
                                                    case 0:
                                                        // choose video from gallery
                                                        multiPickerWrapper.getPermissionAndPickSingleVideo();
                                                        break;

                                                    case 1:
                                                        // record video with camera
                                                        multiPickerWrapper.getPermissionAndTakeVideo();
                                                        break;
                                                }
                                            }
                                        });
                            }
                        }
                    });
        }
    };

    private View.OnClickListener tvDataOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!TextUtils.isEmpty(filePath)) {
                Uri uri = Uri.fromFile(new File(filePath));
                if (!isVideo) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "image/*");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "video/*");
                    startActivity(intent);
                }
            }
        }
    };

    private View.OnClickListener btnCheckPermissionsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PermisoWrapper.startInstalledAppDetailsActivity(context);
        }
    };
}
