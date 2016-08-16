package com.noelchew.multipickerwrapper.library;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;

import com.kbeanie.multipicker.api.CacheLocation;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.CameraVideoPicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.VideoPicker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.callbacks.VideoPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.noelchew.permisowrapper.PermisoWrapper;

import java.util.List;

/**
 * Created by noelchew on 15/08/2016.
 */
public class MultiPickerWrapper {
    private ImagePicker imagePicker;
    private VideoPicker videoPicker;
    private CameraImagePicker cameraImagePicker;
    private CameraVideoPicker cameraVideoPicker;
    private PickerUtilListener pickerUtilListener;
    public String pickerPath;
    private Context context;
    private Activity activity;
    private android.support.v4.app.Fragment supportFragment;
    private Fragment fragment;
    private _CacheLocation cacheLocation;

    private int videoDurationLimit = 15;

    public MultiPickerWrapper(Activity activity, _CacheLocation cacheLocation) {
        imagePicker = new ImagePicker(activity);
        videoPicker = new VideoPicker(activity);
        cameraImagePicker = new CameraImagePicker(activity);
        cameraVideoPicker = new CameraVideoPicker(activity);
        this.context = activity;
        this.activity = activity;
        this.cacheLocation = cacheLocation;
    }

    public MultiPickerWrapper(Activity activity) {
        this(activity, _CacheLocation.EXTERNAL_STORAGE_APP_DIR);
    }

    public MultiPickerWrapper(android.support.v4.app.Fragment supportFragment, _CacheLocation cacheLocation) {
        imagePicker = new ImagePicker(supportFragment);
        videoPicker = new VideoPicker(supportFragment);
        cameraImagePicker = new CameraImagePicker(supportFragment);
        cameraVideoPicker = new CameraVideoPicker(supportFragment);
        this.context = supportFragment.getActivity();
        this.supportFragment = supportFragment;
        this.cacheLocation = cacheLocation;
    }

    public MultiPickerWrapper(android.support.v4.app.Fragment supportFragment) {
        this(supportFragment, _CacheLocation.EXTERNAL_STORAGE_APP_DIR);
    }

    public MultiPickerWrapper(Fragment fragment, _CacheLocation cacheLocation) {
        imagePicker = new ImagePicker(fragment);
        videoPicker = new VideoPicker(fragment);
        cameraImagePicker = new CameraImagePicker(fragment);
        cameraVideoPicker = new CameraVideoPicker(fragment);
        this.context = fragment.getActivity();
        this.fragment = fragment;
        this.cacheLocation = cacheLocation;
    }

    public MultiPickerWrapper(Fragment fragment) {
        this(fragment, _CacheLocation.EXTERNAL_STORAGE_APP_DIR);
    }

    public PickerUtilListener getPickerUtilListener() {
        return this.pickerUtilListener;
    }

    public void setPickerUtilListener(PickerUtilListener pickerUtilListener) {
        this.pickerUtilListener = pickerUtilListener;
    }

    public void getPermissionAndPickSingleImage() {
        PermisoWrapper.getPermissionPickPictureVideo(context, pickSinglePicturePermissionListener);
    }

    public void getPermissionAndPickMultipleImage() {
        PermisoWrapper.getPermissionPickPictureVideo(context, pickMultiplePicturePermissionListener);
    }

    public void getPermissionAndTakePicture() {
        PermisoWrapper.getPermissionTakePicture(context, takePicturePermissionListener);
    }

    public void getPermissionAndPickSingleVideo() {
        PermisoWrapper.getPermissionPickPictureVideo(context, pickVideoPermissionListener);
    }

    public void getPermissionAndTakeVideo() {
        PermisoWrapper.getPermissionTakeVideo(context, takeVideoPermissionListener);
    }

    public void getPermissionAndTakeVideoWithDurationLimit(int videoDurationLimit) {
        this.videoDurationLimit = videoDurationLimit;
        PermisoWrapper.getPermissionTakeVideo(context, takeVideoWithDurationLimitPermissionListener);
    }

    private void pickSingleImage() {
        imagePicker.shouldGenerateMetadata(false);
        imagePicker.shouldGenerateThumbnails(false);
        imagePicker.setCacheLocation(cacheLocation.getValue());
        imagePicker.setImagePickerCallback(imagePickerCallback);
        imagePicker.pickImage();
    }

    private void pickMultipleImage() {
        imagePicker.allowMultiple();
        imagePicker.shouldGenerateMetadata(false);
        imagePicker.shouldGenerateThumbnails(false);
        imagePicker.setCacheLocation(cacheLocation.getValue());
        imagePicker.setImagePickerCallback(imagePickerCallback);
        imagePicker.pickImage();
    }

    private void takePicture() {
        cameraImagePicker.shouldGenerateMetadata(false);
        cameraImagePicker.shouldGenerateThumbnails(false);
        cameraImagePicker.setCacheLocation(cacheLocation.getValue());
        cameraImagePicker.setImagePickerCallback(imagePickerCallback);
        pickerPath = cameraImagePicker.pickImage();
    }

    private void pickSingleVideo() {
        videoPicker.shouldGenerateMetadata(true);
        videoPicker.shouldGeneratePreviewImages(true);
        videoPicker.setCacheLocation(cacheLocation.getValue());
        videoPicker.setVideoPickerCallback(videoPickerCallback);
        videoPicker.pickVideo();
    }

    private void takeVideo() {
        Bundle extras = new Bundle();
        // For capturing Low quality videos; Default is 1: HIGH
        extras.putInt(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        cameraVideoPicker.setExtras(extras);
        cameraVideoPicker.shouldGenerateMetadata(true);
        cameraVideoPicker.shouldGeneratePreviewImages(true);
        cameraVideoPicker.setCacheLocation(cacheLocation.getValue());
        cameraVideoPicker.setVideoPickerCallback(videoPickerCallback);
        pickerPath = cameraVideoPicker.pickVideo();
    }

    public void takeVideoWithDurationLimit(int seconds) {
        Bundle extras = new Bundle();
        // For capturing Low quality videos; Default is 1: HIGH
        extras.putInt(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        extras.putInt(MediaStore.EXTRA_DURATION_LIMIT, seconds);
        cameraVideoPicker.setExtras(extras);
        cameraVideoPicker.shouldGenerateMetadata(true);
        cameraVideoPicker.shouldGeneratePreviewImages(true);
        cameraVideoPicker.setCacheLocation(cacheLocation.getValue());
        cameraVideoPicker.setVideoPickerCallback(videoPickerCallback);
        pickerPath = cameraVideoPicker.pickVideo();
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Picker.PICK_IMAGE_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    if (imagePicker == null) {
                        if (activity != null) {
                            imagePicker = new ImagePicker(activity);
                        } else if (supportFragment != null) {
                            imagePicker = new ImagePicker(supportFragment);
                        } else if (fragment != null) {
                            imagePicker = new ImagePicker(fragment);
                        }
                        imagePicker.setImagePickerCallback(imagePickerCallback);
                    }
                    imagePicker.submit(data);
                }
                return true;
            case Picker.PICK_IMAGE_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    if (cameraImagePicker == null) {
                        if (activity != null) {
                            cameraImagePicker = new CameraImagePicker(activity);
                        } else if (supportFragment != null) {
                            cameraImagePicker = new CameraImagePicker(supportFragment);
                        } else if (fragment != null) {
                            cameraImagePicker = new CameraImagePicker(fragment);
                        }
                        cameraImagePicker.reinitialize(pickerPath);
                        cameraImagePicker.setImagePickerCallback(imagePickerCallback);
                    }
                    cameraImagePicker.submit(data);
                }
                return true;
            case Picker.PICK_VIDEO_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    if (videoPicker == null) {
                        if (activity != null) {
                            videoPicker = new VideoPicker(activity);
                        } else if (supportFragment != null) {
                            videoPicker = new VideoPicker(supportFragment);
                        } else if (fragment != null) {
                            videoPicker = new VideoPicker(fragment);
                        }
                        videoPicker.setVideoPickerCallback(videoPickerCallback);
                    }
                    videoPicker.submit(data);
                }
                return true;
            case Picker.PICK_VIDEO_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    if (cameraVideoPicker == null) {
                        if (activity != null) {
                            cameraVideoPicker = new CameraVideoPicker(activity, pickerPath);
                        } else if (supportFragment != null) {
                            cameraVideoPicker = new CameraVideoPicker(supportFragment, pickerPath);
                        } else if (fragment != null) {
                            cameraVideoPicker = new CameraVideoPicker(fragment, pickerPath);
                        }
                        cameraVideoPicker.setVideoPickerCallback(videoPickerCallback);
                    }
                    cameraVideoPicker.submit(data);
                }
                return true;
        }
        return false;
    }

    // --- Picker Callback --- start ---

    private ImagePickerCallback imagePickerCallback = new ImagePickerCallback() {
        @Override
        public void onImagesChosen(List<ChosenImage> list) {
            if (pickerUtilListener == null) {
                throw new RuntimeException("PickerUtilListener is not set.");
            }
            pickerUtilListener.onImagesChosen(list);
        }

        @Override
        public void onError(String s) {
            if (pickerUtilListener == null) {
                throw new RuntimeException("PickerUtilListener is not set.");
            }
            pickerUtilListener.onError(s);
        }
    };

    private VideoPickerCallback videoPickerCallback = new VideoPickerCallback() {
        @Override
        public void onVideosChosen(List<ChosenVideo> list) {
            if (pickerUtilListener == null) {
                throw new RuntimeException("PickerUtilListener is not set.");
            }
            pickerUtilListener.onVideosChosen(list);
        }

        @Override
        public void onError(String s) {
            if (pickerUtilListener == null) {
                throw new RuntimeException("PickerUtilListener is not set.");
            }
            pickerUtilListener.onError(s);
        }
    };

    // --- Picker Callback --- end ---

    // --- Permiso Callback --- start ---
    private PermisoWrapper.PermissionListener pickSinglePicturePermissionListener = new PermisoWrapper.PermissionListener() {
        @Override
        public void onPermissionGranted() {
            pickSingleImage();
        }

        @Override
        public void onPermissionDenied() {
            if (pickerUtilListener == null) {
                throw new RuntimeException("PickerUtilListener is not set.");
            }
            pickerUtilListener.onPermissionDenied();
        }
    };

    private PermisoWrapper.PermissionListener pickMultiplePicturePermissionListener = new PermisoWrapper.PermissionListener() {
        @Override
        public void onPermissionGranted() {
            pickMultipleImage();
        }

        @Override
        public void onPermissionDenied() {
            if (pickerUtilListener == null) {
                throw new RuntimeException("PickerUtilListener is not set.");
            }
            pickerUtilListener.onPermissionDenied();
        }

    };

    private PermisoWrapper.PermissionListener takePicturePermissionListener = new PermisoWrapper.PermissionListener() {
        @Override
        public void onPermissionGranted() {
            takePicture();
        }

        @Override
        public void onPermissionDenied() {
            if (pickerUtilListener == null) {
                throw new RuntimeException("PickerUtilListener is not set.");
            }
            pickerUtilListener.onPermissionDenied();
        }

    };

    private PermisoWrapper.PermissionListener pickVideoPermissionListener = new PermisoWrapper.PermissionListener() {
        @Override
        public void onPermissionGranted() {
            pickSingleVideo();
        }

        @Override
        public void onPermissionDenied() {
            if (pickerUtilListener == null) {
                throw new RuntimeException("PickerUtilListener is not set.");
            }
            pickerUtilListener.onPermissionDenied();
        }

    };

    private PermisoWrapper.PermissionListener takeVideoPermissionListener = new PermisoWrapper.PermissionListener() {
        @Override
        public void onPermissionGranted() {
            takeVideo();
        }

        @Override
        public void onPermissionDenied() {
            if (pickerUtilListener == null) {
                throw new RuntimeException("PickerUtilListener is not set.");
            }
            pickerUtilListener.onPermissionDenied();
        }

    };

    private PermisoWrapper.PermissionListener takeVideoWithDurationLimitPermissionListener = new PermisoWrapper.PermissionListener() {
        @Override
        public void onPermissionGranted() {
            takeVideoWithDurationLimit(videoDurationLimit);
        }

        @Override
        public void onPermissionDenied() {
            if (pickerUtilListener == null) {
                throw new RuntimeException("PickerUtilListener is not set.");
            }
            pickerUtilListener.onPermissionDenied();
        }

    };

    // --- Permiso Callback --- end ---

    public interface PickerUtilListener {
        void onPermissionDenied();

        void onImagesChosen(List<ChosenImage> list);

        void onVideosChosen(List<ChosenVideo> list);

        void onError(String s);
    }

    public enum _CacheLocation {
        EXTERNAL_STORAGE_PUBLIC_DIR(CacheLocation.EXTERNAL_STORAGE_PUBLIC_DIR),
        EXTERNAL_STORAGE_APP_DIR(CacheLocation.EXTERNAL_STORAGE_APP_DIR),
        EXTERNAL_CACHE_DIR(CacheLocation.EXTERNAL_CACHE_DIR),
        INTERNAL_APP_DIR(CacheLocation.INTERNAL_APP_DIR);

        private int value;

        _CacheLocation(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
