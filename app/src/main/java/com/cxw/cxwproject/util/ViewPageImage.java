package com.cxw.cxwproject.util;

import java.util.ArrayList;

import com.cxw.cxwproject.R;
import com.loopj.android.image.SmartImageView;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 广告图片轮播控件
 */
public class ViewPageImage extends LinearLayout {
	private Context mContext;
	private ViewPager mAdvPager = null;
	public int stype = 0;
	public ViewPageImage(Context context) {
		super(context);
	}

	private ArrayList<Object> mAdList = new ArrayList<Object>();

	/**
	 * @param context
	 * @param attrs
	 */
	public ViewPageImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.viewpageimage_view, this);
		mAdvPager = (ViewPager) findViewById(R.id.adv_pager);
	}
	/**
	 * 装填图片数据
	 * 
	 * @param imageUrlList
	 * @param imageCycleViewListener
	 */
	public void setImageResources(ArrayList<Object> imageUrlList, int stype) {
		this.stype = stype;
		this.mAdList = imageUrlList;
		mAdvPager.setAdapter(new ImageCycleAdapter());
	}

	private class ImageCycleAdapter extends PagerAdapter {
		// 图片视图缓存列表
		private ArrayList<SmartImageView> mImageViewCacheList;

		public ImageCycleAdapter() {
			mImageViewCacheList = new ArrayList<SmartImageView>();
		}

		@Override
		public int getCount() {
			return mAdList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			SmartImageView imageView = null;
			if (mImageViewCacheList.isEmpty()) {
				imageView = new SmartImageView(mContext);
				imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			} else {
				imageView = mImageViewCacheList.remove(0);
			}
			container.addView(imageView);
			imageView.setBackgroundResource((Integer)mAdList.get(position % mAdList.size()));
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			SmartImageView view = (SmartImageView) object;
			mAdvPager.removeView(view);
			mImageViewCacheList.add(view);
		}
	}
}
