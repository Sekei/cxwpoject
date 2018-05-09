package com.cxw.cxwproject.fragment;

import com.cxw.cxwproject.BaseFragment;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.activity.ShowBigPictrueActivity;
import com.cxw.cxwproject.imagedisplay.ImageLoader;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * 显示大图的实现，并且可以放大缩小
 */
@SuppressLint("ValidFragment")
public class PictrueFragment extends BaseFragment implements OnClickListener {

	private int resId;
	private String[] strImg;

	@SuppressLint("ValidFragment")
	public PictrueFragment(int resId, String[] strImg) {
		this.resId = resId;
		this.strImg = strImg;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_scale_pic_item;
	}

	@Override
	protected void initView(View view) {
		super.initView(view);
		ImageView imageView = (ImageView) view.findViewById(R.id.scale_pic_item);
		imageView.setOnClickListener(this);
		ImageLoader.displayImage(strImg[resId], imageView);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.scale_pic_item:
			ShowBigPictrueActivity parentActivity = (ShowBigPictrueActivity) getActivity();
			parentActivity.pageListener();
			break;
		}
	}
}
