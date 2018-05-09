package com.cxw.cxwproject.util.camera;

import com.cxw.cxwproject.widget.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class CameraProxy {
	private Uri origUri;
	private Uri cropUri;

	public static interface PhotoHelper {
		void success(String path);
	}

	private Activity activity;
	private PhotoHelper photoHelper;

	public CameraProxy(Activity activity) {
		this.activity = activity;
	}

	public void setPhotoHelper(PhotoHelper l) {
		photoHelper = l;
	}

	private boolean needCrop;
	private int x, y;

	public void setCrop(int x, int y) {
		this.x = x;
		this.y = y;
		needCrop = x * y != 0;
	}

	public void cancelCrop() {
		needCrop = false;
	}

	public void takePhoto() {
		origUri = ImageUtils.startTakePhoto(activity);
	}

	public void pickImage() {
		ImageUtils.startImagePick(activity);
	}

	public void onActivityResult(final int requestCode, final int resultCode, final Intent imageReturnIntent) {
		if (resultCode != Activity.RESULT_OK)
			return;

		switch (requestCode) {
		case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
			if(origUri == null){
				ToastUtils.show("获取图片失败，请重新选择图片！");
				return;
			}
			if (needCrop) {
				cropUri = ImageUtils.startActionCrop(activity, origUri, x, y);// 拍照后裁剪
			} else {
				processPhoto(origUri);
			}
			break;
		case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
			if(imageReturnIntent == null){
				ToastUtils.show("获取图片失败，请重新选择图片！");
				return;
			}
			if (needCrop) {				
				cropUri = ImageUtils.startActionCrop(activity, imageReturnIntent.getData(), x, y);// 选图后裁剪
			} else {
				processPhoto(imageReturnIntent.getData());
			}
			break;
		case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
			processPhoto(cropUri);
			break;
		}
	}

	private void processPhoto(Uri uri) {
		if (photoHelper != null) {
			photoHelper.success(ImageUtils.getRealFilePath(activity, uri));
		}
	}
}
