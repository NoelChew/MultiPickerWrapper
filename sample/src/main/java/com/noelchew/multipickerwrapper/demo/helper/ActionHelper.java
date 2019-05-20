package com.noelchew.multipickerwrapper.demo.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kbeanie.multipicker.api.entity.ChosenAudio;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.noelchew.multipickerwrapper.demo.R;
import com.noelchew.multipickerwrapper.demo.utils.FileIntentUtil;
import com.noelchew.multipickerwrapper.demo.utils.PixelUtil;
import com.noelchew.multipickerwrapper.library.MultiPickerWrapper;
import com.noelchew.ncutils.alert.AlertDialogUtil;
import com.noelchew.permisowrapper.PermisoWrapper;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActionHelper {
    private static String filePath = "";

    public static void onPermissionDenied(Context context) {
        Toast.makeText(context, "Permission denied.", Toast.LENGTH_SHORT).show();
    }

    public static void onImagesChosen(List<ChosenImage> list, final Context context, TextView tvData, ImageView imageView) {
        filePath = list.get(0).getOriginalPath();
        tvData.setText(context.getString(R.string.show_data) + filePath);
        AlertDialogUtil.showAlertDialogWithSelections(context,
                "Image Selected:",
                new ArrayList<>(Arrays.asList(new String[]{"Show", "Cancel"})),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            FileIntentUtil.openFile(context, filePath);
                        }
                    }
                });
        Glide.with(context).load(list.get(0).getOriginalPath()).into(imageView);
    }


    public static void onVideosChosen(List<ChosenVideo> list, final Context context, TextView tvData, ImageView imageView) {
        filePath = list.get(0).getOriginalPath();
        tvData.setText(context.getString(R.string.show_data) + filePath);
        AlertDialogUtil.showAlertDialogWithSelections(context,
                "Video Selected:",
                new ArrayList<>(Arrays.asList(new String[]{"Show", "Cancel"})),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            FileIntentUtil.openFile(context, filePath);
                        }
                    }
                });
        Glide.with(context).load(list.get(0).getPreviewThumbnail()).into(imageView);
    }

    public static void onAudiosChosen(List<ChosenAudio> list, final Context context, TextView tvData, ImageView imageView) {
        // multiple audio is supported here
        String text = "";
        ArrayList<Uri> uriArrayList = new ArrayList<>();
        ArrayList<String> pathArrayList = new ArrayList<>();
        for (ChosenAudio chosenAudio : list) {
            uriArrayList.add(Uri.fromFile(new File(chosenAudio.getOriginalPath())));
            filePath = chosenAudio.getOriginalPath();
            pathArrayList.add(filePath);
            text += filePath + "\n";
        }
        final ArrayList<String> _pathArrayList = pathArrayList;
        tvData.setText(context.getString(R.string.show_data_multiple) + text + "\n" + context.getString(R.string.click_to_show_last_item));
        AlertDialogUtil.showAlertDialogWithSelections(context,
                "Audio Selected:",
                pathArrayList,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FileIntentUtil.openFile(context, _pathArrayList.get(which));

                    }
                });
        imageView.setImageDrawable(null);
    }

    public static void onFilesChosen(List<ChosenFile> list, final Context context, TextView tvData, ImageView imageView) {
        filePath = list.get(0).getOriginalPath();
        tvData.setText(context.getString(R.string.show_data) + filePath);
        AlertDialogUtil.showAlertDialogWithSelections(context,
                "File Selected:",
                new ArrayList<>(Arrays.asList(new String[]{"Show", "Cancel"})),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            FileIntentUtil.openFile(context, filePath);
                        }
                    }
                });

        imageView.setImageDrawable(null);

    }


    public static void onError(String s, Context context) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    public static void onImageClicked(final Context context, final MultiPickerWrapper multiPickerWrapper) {
        AlertDialogUtil.showAlertDialogWithSelections(context,
                "What do you want to pick?",
                new ArrayList<>(Arrays.asList(new String[]{"Image", "Image + Crop", "Video", "Audio", "File"})),
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
                        } else if (which == 3) {
                            AlertDialogUtil.showAlertDialogWithSelections(context,
                                    "Audio",
                                    new ArrayList<>(Arrays.asList(new String[]{"Single Audio", "Multiple Audio"})),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case 0:
                                                    // pick single audio file
                                                    multiPickerWrapper.getPermissionAndPickAudio();
                                                    break;

                                                case 1:
                                                    // pick multiple audio file
                                                    multiPickerWrapper.getPermissionAndPickMultipleAudio();
                                                    break;
                                            }
                                        }
                                    });
                        } else if (which == 4) {
                            AlertDialogUtil.showAlertDialogWithSelections(context,
                                    "File",
                                    new ArrayList<>(Arrays.asList(new String[]{"PDF", "Any File"})),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case 0:
                                                    // pick PDF file
                                                    // get list of mime type here: http://androidxref.com/4.4.4_r1/xref/frameworks/base/media/java/android/media/MediaFile.java#174
                                                    multiPickerWrapper.getPermissionAndPickSingleFile("application/pdf");
                                                    break;

                                                case 1:
                                                    // pick any file
                                                    multiPickerWrapper.getPermissionAndPickSingleFile();
                                                    break;
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public static void onTvDataClicked(Context context) {
        if (!TextUtils.isEmpty(filePath)) {
            FileIntentUtil.openFile(context, filePath);
        }
    }

    public static void onBtnCheckPermissionsOnClicked(Context context) {
        PermisoWrapper.startInstalledAppDetailsActivity(context);
    }
}
