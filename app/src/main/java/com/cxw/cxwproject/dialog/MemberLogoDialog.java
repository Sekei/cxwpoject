package com.cxw.cxwproject.dialog;

import com.cxw.cxwproject.R;
import com.cxw.cxwproject.dialog.base.BaseDialog;
import com.cxw.cxwproject.util.camera.CameraProxy;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

public class MemberLogoDialog extends BaseDialog {
	Context mContext;
	CameraProxy cameraProxy;

	public MemberLogoDialog(Context context) {
		super(context);
	}

	public MemberLogoDialog(Context context, CameraProxy p) {
		super(context);
		this.mContext = context;
		this.cameraProxy = p;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.img_upload;
	}

	@Override
	protected void initView() {
		TextView share_cancell = (TextView) findViewById(R.id.share_cancel);// 取消
		share_cancell.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		// 相册
		TextView share_weibo_button = (TextView) findViewById(R.id.share_weibo_button);
		share_weibo_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (cameraProxy != null) {
					cameraProxy.pickImage();
				} else if (pickImageListener != null) {
					pickImageListener.onClick(MemberLogoDialog.this, BUTTON_NEGATIVE);
				}
				dismiss();
			}
		});
		// 相机
		TextView share_weixin_py_button = (TextView) findViewById(R.id.share_weixin_py_button);
		share_weixin_py_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (cameraProxy != null) {
					cameraProxy.takePhoto();
				} else if (takePhotoListener != null) {
					takePhotoListener.onClick(MemberLogoDialog.this, BUTTON_POSITIVE);
				}
				dismiss();
			}
		});
	}

	DialogInterface.OnClickListener takePhotoListener, pickImageListener;

	public MemberLogoDialog setTakePhotoListener(DialogInterface.OnClickListener l) {
		takePhotoListener = l;
		return this;
	}

	public MemberLogoDialog setPickImageListener(DialogInterface.OnClickListener l) {
		pickImageListener = l;
		return this;
	}
}
