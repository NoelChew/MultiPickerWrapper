package com.noelchew.multipickerwrapper.demo.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.noelchew.multipickerwrapper.demo.R;
import com.noelchew.multipickerwrapper.demo.utils.PixelUtil;
import com.noelchew.multipickerwrapper.library.MultiPickerWrapper;
import com.noelchew.multipickerwrapper.library.ui.MultiPickerWrapperAppCompatActivity;
import com.noelchew.permisowrapper.PermisoWrapper;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.List;

public class DemoActivity extends MultiPickerWrapperAppCompatActivity {

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
            MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
            builder.title("Image Selected:")
                    .content(list.get(0).getOriginalPath())
                    .items("Show", "Cancel")
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                            if (which == 0) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setDataAndType(uri, "image/*");
                                startActivity(intent);
                            }
                        }
                    }).show();
            Glide.with(context).load(uri).into(imageView);
        }

        @Override
        public void onVideosChosen(List<ChosenVideo> list) {
            final Uri uri = Uri.fromFile(new File(list.get(0).getOriginalPath()));
            filePath = list.get(0).getOriginalPath();
            tvData.setText(getString(R.string.show_data) + filePath);
            isVideo = true;
            MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
            builder.title("Video Selected:")
                    .content(list.get(0).getOriginalPath())
                    .items("Show", "Cancel")
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                            if (which == 0) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setDataAndType(uri, "video/*");
                                startActivity(intent);
                            }
                        }
                    }).show();

            Glide.with(context).load(list.get(0).getPreviewThumbnail()).into(imageView);
        }

        @Override
        public void onError(String s) {
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = DemoActivity.this;
        setContentView(R.layout.activity_demo);

        relativeLayoutImage = (RelativeLayout) findViewById(R.id.relative_layout_image);
        imageView = (ImageView) findViewById(R.id.image_view);
        tvData = (TextView) findViewById(R.id.text_view_selected_data);

        relativeLayoutImage.setOnClickListener(relativeLayoutImageOnClickListener);
        tvData.setOnClickListener(tvDataOnClickListener);

        btnCheckPermissions = (Button) findViewById(R.id.button_check_permissions);
        btnCheckPermissions.setOnClickListener(btnCheckPermissionsOnClickListener);

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

    private View.OnClickListener relativeLayoutImageOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
            builder.title("Image or Video")
                    .content("Do you want to select image or video?")
                    .items("Image", "Image + Crop", "Video")
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                            if (which == 0) {
                                MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
                                builder.title("Image")
                                        .content("Do you want to pick from Gallery or take with Camera?")
                                        .items("Gallery", "Camera")
                                        .itemsCallback(new MaterialDialog.ListCallback() {
                                            @Override
                                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
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
                                        }).show();
                            } if (which == 1) {
                                MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
                                builder.title("Crop Image")
                                        .content("Do you want to pick from Gallery or take with Camera?")
                                        .items("Gallery", "Camera")
                                        .itemsCallback(new MaterialDialog.ListCallback() {
                                            @Override
                                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                                UCrop.Options options = new UCrop.Options();
                                                options.setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                                                options.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
                                                options.setToolbarTitle("MultiPickerWrapper - Crop");

                                                options.setCropFrameColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                                                options.setCropFrameStrokeWidth(PixelUtil.dpToPx(4));

                                                options.setCropGridColor(ContextCompat.getColor(context, R.color.colorPrimary));
                                                options.setCropGridStrokeWidth(PixelUtil.dpToPx(2));

                                                options.setOvalDimmedLayer(true);

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
                                        }).show();
                            } else if (which == 2) {
                                MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
                                builder.title("Video")
                                        .content("Do you want to pick from Gallery or record with Camera?")
                                        .items("Gallery", "Camera")
                                        .itemsCallback(new MaterialDialog.ListCallback() {
                                            @Override
                                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
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
                                        }).show();
                            }
                        }
                    }).show();
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

    private View.OnClickListener btnCheckPermissionsOnClickListener  = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PermisoWrapper.startInstalledAppDetailsActivity(context);
        }
    };
}
