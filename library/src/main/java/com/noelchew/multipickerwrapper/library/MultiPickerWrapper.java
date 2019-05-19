package com.noelchew.multipickerwrapper.library;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.kbeanie.multipicker.api.AudioPicker;
import com.kbeanie.multipicker.api.CacheLocation;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.CameraVideoPicker;
import com.kbeanie.multipicker.api.FilePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.VideoPicker;
import com.kbeanie.multipicker.api.callbacks.AudioPickerCallback;
import com.kbeanie.multipicker.api.callbacks.FilePickerCallback;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.callbacks.VideoPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenAudio;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.noelchew.permisowrapper.PermisoWrapper;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by noelchew on 15/08/2016.
 */
public class MultiPickerWrapper {

    public static final String TAG = "MultiPickerWrapper";

    public static final int UCROP_PICKER_REQUEST = 2201;
    public static final int UCROP_CAMERA_REQUEST = 2202;

    private ImagePicker imagePicker;
    private VideoPicker videoPicker;
    private CameraImagePicker cameraImagePicker;
    private CameraVideoPicker cameraVideoPicker;
    private FilePicker filePicker;
    private AudioPicker audioPicker;
    private PickerUtilListener pickerUtilListener;
    public String pickerPath;
    private Context context;
    private Activity activity;
    private android.support.v4.app.Fragment supportFragment;
    private Fragment fragment;
    private _CacheLocation cacheLocation;

    // true if user chooses to crop image from gallery;
    // false if user chooses to crop image taken from camera
    private boolean isUCropPicker = true;

    private UCrop.Options uCropOptions;
    private boolean setAspectRatio = false;
    private float aspectRatioWidth = 1;
    private float aspectRatioHeight = 1;
    private int videoDurationLimit = 15;

    // filePicker param
    private String filePickerMimeType;

    public MultiPickerWrapper(Activity activity, _CacheLocation cacheLocation) {
        imagePicker = new ImagePicker(activity);
        videoPicker = new VideoPicker(activity);
        cameraImagePicker = new CameraImagePicker(activity);
        cameraVideoPicker = new CameraVideoPicker(activity);
        filePicker = new FilePicker(activity);
        audioPicker = new AudioPicker(activity);
        this.context = activity;
        this.activity = activity;
        this.cacheLocation = cacheLocation;
    }

    public MultiPickerWrapper(Activity activity) {
        this(activity, _CacheLocation.EXTERNAL_CACHE_DIR);
    }

    public MultiPickerWrapper(android.support.v4.app.Fragment supportFragment, _CacheLocation cacheLocation) {
        imagePicker = new ImagePicker(supportFragment);
        videoPicker = new VideoPicker(supportFragment);
        cameraImagePicker = new CameraImagePicker(supportFragment);
        cameraVideoPicker = new CameraVideoPicker(supportFragment);
        filePicker = new FilePicker(supportFragment);
        audioPicker = new AudioPicker(supportFragment);
        this.context = supportFragment.getActivity();
        this.supportFragment = supportFragment;
        this.cacheLocation = cacheLocation;
    }

    public MultiPickerWrapper(android.support.v4.app.Fragment supportFragment) {
        this(supportFragment, _CacheLocation.EXTERNAL_CACHE_DIR);
    }

    public MultiPickerWrapper(Fragment fragment, _CacheLocation cacheLocation) {
        imagePicker = new ImagePicker(fragment);
        videoPicker = new VideoPicker(fragment);
        cameraImagePicker = new CameraImagePicker(fragment);
        cameraVideoPicker = new CameraVideoPicker(fragment);
        filePicker = new FilePicker(fragment);
        audioPicker = new AudioPicker(fragment);
        this.context = fragment.getActivity();
        this.fragment = fragment;
        this.cacheLocation = cacheLocation;
    }

    public MultiPickerWrapper(Fragment fragment) {
        this(fragment, _CacheLocation.EXTERNAL_CACHE_DIR);
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

    public void getPermissionAndPickSingleImageAndCrop(UCrop.Options uCropOptions) {
        this.isUCropPicker = true;
        this.setAspectRatio = false;
        this.uCropOptions = uCropOptions;
        PermisoWrapper.getPermissionPickPictureVideo(context, pickSinglePictureAndCropPermissionListener);
    }

    public void getPermissionAndPickSingleImageAndCrop(UCrop.Options uCropOptions, float aspectRatioWidth, float aspectRatioHeight) {
        this.isUCropPicker = true;
        this.setAspectRatio = true;
        this.aspectRatioWidth = aspectRatioWidth;
        this.aspectRatioHeight = aspectRatioHeight;
        this.uCropOptions = uCropOptions;
        PermisoWrapper.getPermissionPickPictureVideo(context, pickSinglePictureAndCropPermissionListener);
    }

    public void getPermissionAndPickMultipleImage() {
        PermisoWrapper.getPermissionPickPictureVideo(context, pickMultiplePicturePermissionListener);
    }

    public void getPermissionAndTakePicture() {
        PermisoWrapper.getPermissionTakePicture(context, takePicturePermissionListener);
    }

    public void getPermissionAndTakePictureAndCrop(UCrop.Options uCropOptions) {
        this.isUCropPicker = false;
        this.setAspectRatio = false;
        this.uCropOptions = uCropOptions;
        PermisoWrapper.getPermissionTakePicture(context, takePictureAndCropPermissionListener);
    }

    public void getPermissionAndTakePictureAndCrop(UCrop.Options uCropOptions, float aspectRatioWidth, float aspectRatioHeight) {
        this.isUCropPicker = false;
        this.setAspectRatio = true;
        this.aspectRatioWidth = aspectRatioWidth;
        this.aspectRatioHeight = aspectRatioHeight;
        this.uCropOptions = uCropOptions;
        PermisoWrapper.getPermissionTakePicture(context, takePictureAndCropPermissionListener);
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

    public void getPermissionAndPickSingleFile() {
        getPermissionAndPickSingleFile("*/*");
    }

    public void getPermissionAndPickSingleFile(String filePickerMimeType) {
        this.filePickerMimeType = filePickerMimeType;
        PermisoWrapper.getPermissionPickFolderFile(context, pickSingleFilePermissionListener);
    }

    public void getPermissionAndPickMultipleFile() {
        getPermissionAndPickMultipleFile("*/*");
    }

    public void getPermissionAndPickMultipleFile(String filePickerMimeType) {
        this.filePickerMimeType = filePickerMimeType;
        PermisoWrapper.getPermissionPickFolderFile(context, pickMultipleFilePermissionListener);
    }

    public void getPermissionAndPickAudio() {
        PermisoWrapper.getPermissionPickAudio(context, pickSingleAudioPermissionListener);
    }

    public void getPermissionAndPickMultipleAudio() {
        PermisoWrapper.getPermissionPickAudio(context, pickMultipleAudioPermissionListener);
    }

    // ----------------- //

    private void pickSingleImage() {
        imagePicker.shouldGenerateMetadata(false);
        imagePicker.shouldGenerateThumbnails(false);
        imagePicker.setCacheLocation(cacheLocation.getValue());
        imagePicker.setImagePickerCallback(imagePickerCallback);
        imagePicker.pickImage();
    }

    private void pickSingleImageAndCrop() {
        imagePicker.shouldGenerateMetadata(false);
        imagePicker.shouldGenerateThumbnails(false);
        imagePicker.setCacheLocation(cacheLocation.getValue());
        imagePicker.setImagePickerCallback(imagePickerCallbackWithCrop);
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

    private void takePictureAndCrop() {
        cameraImagePicker.shouldGenerateMetadata(false);
        cameraImagePicker.shouldGenerateThumbnails(false);
        cameraImagePicker.setCacheLocation(cacheLocation.getValue());
        cameraImagePicker.setImagePickerCallback(imagePickerCallbackWithCrop);
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

    public void pickSingleFile() {
        if (!TextUtils.isEmpty(filePickerMimeType)) {
            filePicker.setMimeType(filePickerMimeType);
        } else {
            filePicker.setMimeType("");
        }
        filePicker.setCacheLocation(cacheLocation.getValue());
        filePicker.setFilePickerCallback(filePickerCallback);
        filePicker.pickFile();
    }

    public void pickMultipleFile() {
        if (!TextUtils.isEmpty(filePickerMimeType)) {
            filePicker.setMimeType(filePickerMimeType);
        } else {
            filePicker.setMimeType("");
        }
        filePicker.allowMultiple();
        filePicker.setCacheLocation(cacheLocation.getValue());
        filePicker.setFilePickerCallback(filePickerCallback);
        filePicker.pickFile();
    }

    public void pickSingleAudio() {
        audioPicker.setCacheLocation(cacheLocation.getValue());
        audioPicker.setAudioPickerCallback(audioPickerCallback);
        audioPicker.pickAudio();
    }

    public void pickMultipleAudio() {
        audioPicker.allowMultiple();
        audioPicker.setCacheLocation(cacheLocation.getValue());
        audioPicker.setAudioPickerCallback(audioPickerCallback);
        audioPicker.pickAudio();
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

            case UCROP_PICKER_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    if (imagePicker == null) {
                        if (activity != null) {
                            imagePicker = new ImagePicker(activity);
                        } else if (supportFragment != null) {
                            imagePicker = new ImagePicker(supportFragment);
                        } else if (fragment != null) {
                            imagePicker = new ImagePicker(fragment);
                        }
                    }
                    imagePicker.setImagePickerCallback(imagePickerCallback);
                    Uri uri = UCrop.getOutput(data);
                    Intent intentData = new Intent();
                    intentData.putExtra("uris", new ArrayList(Arrays.asList(uri)));
                    imagePicker.submit(intentData);
                }
                return true;

            case UCROP_CAMERA_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    if (cameraImagePicker == null) {
                        if (activity != null) {
                            cameraImagePicker = new CameraImagePicker(activity);
                        } else if (supportFragment != null) {
                            cameraImagePicker = new CameraImagePicker(supportFragment);
                        } else if (fragment != null) {
                            cameraImagePicker = new CameraImagePicker(fragment);
                        }
                    }
                    cameraImagePicker.reinitialize(pickerPath);
                    cameraImagePicker.setImagePickerCallback(imagePickerCallback);
                    Uri uri = UCrop.getOutput(data);
                    Intent intentData = new Intent();
                    intentData.putExtra("uris", new ArrayList(Arrays.asList(uri)));
                    cameraImagePicker.submit(intentData);
                }
                return true;
            case Picker.PICK_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    if (filePicker == null) {
                        if (activity != null) {
                            filePicker = new FilePicker(activity);
                        } else if (supportFragment != null) {
                            filePicker = new FilePicker(supportFragment);
                        } else if (fragment != null) {
                            filePicker = new FilePicker(fragment);
                        }
                        filePicker.setFilePickerCallback(filePickerCallback);
                    }
                    filePicker.submit(data);
                }
                return true;
            case Picker.PICK_AUDIO:
                if (resultCode == Activity.RESULT_OK) {
                    if (audioPicker == null) {
                        if (activity != null) {
                            audioPicker = new AudioPicker(activity);
                        } else if (supportFragment != null) {
                            audioPicker = new AudioPicker(supportFragment);
                        } else if (fragment != null) {
                            audioPicker = new AudioPicker(fragment);
                        }
                        audioPicker.setAudioPickerCallback(audioPickerCallback);
                    }
                    audioPicker.submit(data);
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

    private ImagePickerCallback imagePickerCallbackWithCrop = new ImagePickerCallback() {
        @Override
        public void onImagesChosen(List<ChosenImage> list) {
            if (pickerUtilListener == null) {
                throw new RuntimeException("PickerUtilListener is not set.");
            }

            // assume only one image is picked/taken
            if (list.isEmpty()) {
                pickerUtilListener.onImagesChosen(list);
            } else {
                File imageFile = new File(list.get(0).getOriginalPath());

                Uri sourceUri = Uri.fromFile(imageFile);

                String fileName = imageFile.getName();
                String tmp[] = fileName.split("\\.");
                String fileNameWithoutExtension = fileName.replace("." + tmp[tmp.length - 1], "");
                String destinationFilePath = imageFile.getParentFile().getAbsolutePath() + "/" + fileNameWithoutExtension + "_cropped" + "." + tmp[tmp.length - 1];

                // this line is required for Android 7.0 (not sure why)
                pickerPath = destinationFilePath;

                Uri destinationUri = Uri.fromFile(new File(destinationFilePath));
                UCrop uCrop = UCrop.of(sourceUri, destinationUri)
                        .withOptions(uCropOptions);

                if (setAspectRatio) {
                    uCrop.withAspectRatio(aspectRatioWidth, aspectRatioHeight);
                }

                int requestCode;
                if (isUCropPicker) {
                    requestCode = UCROP_PICKER_REQUEST;
                } else {
                    requestCode = UCROP_CAMERA_REQUEST;
                }

                if (activity != null) {
                    uCrop.start(activity, requestCode);
                } else if (fragment != null) {
                    uCrop.start(fragment.getActivity(), fragment, requestCode);
                } else if (supportFragment != null) {
                    uCrop.start(supportFragment.getActivity(), supportFragment, requestCode);
                }
            }
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

    private FilePickerCallback filePickerCallback = new FilePickerCallback() {

        @Override
        public void onFilesChosen(List<ChosenFile> list) {
            if (pickerUtilListener == null) {
                throw new RuntimeException("PickerUtilListener is not set.");
            }
            pickerUtilListener.onFilesChosen(list);
        }

        @Override
        public void onError(String s) {
            if (pickerUtilListener == null) {
                throw new RuntimeException("PickerUtilListener is not set.");
            }
            pickerUtilListener.onError(s);
        }
    };

    private AudioPickerCallback audioPickerCallback = new AudioPickerCallback() {

        @Override
        public void onAudiosChosen(List<ChosenAudio> list) {
            if (pickerUtilListener == null) {
                throw new RuntimeException("PickerUtilListener is not set.");
            }
            pickerUtilListener.onAudiosChosen(list);
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

    private PermisoWrapper.PermissionListener pickSinglePictureAndCropPermissionListener = new PermisoWrapper.PermissionListener() {
        @Override
        public void onPermissionGranted() {
            pickSingleImageAndCrop();
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

    private PermisoWrapper.PermissionListener takePictureAndCropPermissionListener = new PermisoWrapper.PermissionListener() {
        @Override
        public void onPermissionGranted() {
            takePictureAndCrop();
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

    private PermisoWrapper.PermissionListener pickSingleFilePermissionListener = new PermisoWrapper.PermissionListener() {
        @Override
        public void onPermissionGranted() {
            pickSingleFile();
        }

        @Override
        public void onPermissionDenied() {
            if (pickerUtilListener == null) {
                throw new RuntimeException("PickerUtilListener is not set.");
            }
            pickerUtilListener.onPermissionDenied();
        }
    };

    private PermisoWrapper.PermissionListener pickMultipleFilePermissionListener = new PermisoWrapper.PermissionListener() {
        @Override
        public void onPermissionGranted() {
            pickMultipleFile();
        }

        @Override
        public void onPermissionDenied() {
            if (pickerUtilListener == null) {
                throw new RuntimeException("PickerUtilListener is not set.");
            }
            pickerUtilListener.onPermissionDenied();
        }
    };

    private PermisoWrapper.PermissionListener pickSingleAudioPermissionListener = new PermisoWrapper.PermissionListener() {
        @Override
        public void onPermissionGranted() {
            pickSingleAudio();
        }

        @Override
        public void onPermissionDenied() {
            if (pickerUtilListener == null) {
                throw new RuntimeException("PickerUtilListener is not set.");
            }
            pickerUtilListener.onPermissionDenied();
        }
    };

    private PermisoWrapper.PermissionListener pickMultipleAudioPermissionListener = new PermisoWrapper.PermissionListener() {
        @Override
        public void onPermissionGranted() {
            pickMultipleAudio();
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

        void onAudiosChosen(List<ChosenAudio> list);

        void onFilesChosen(List<ChosenFile> list);

        void onError(String s);
    }

    public enum _CacheLocation {
        //        EXTERNAL_STORAGE_PUBLIC_DIR(CacheLocation.EXTERNAL_STORAGE_PUBLIC_DIR),
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
