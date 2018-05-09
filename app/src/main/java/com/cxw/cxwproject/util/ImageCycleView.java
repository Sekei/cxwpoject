package com.cxw.cxwproject.util;

import java.util.ArrayList;

import com.cxw.cxwproject.AppConfig;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.view.DecoratorViewPager;
import com.loopj.android.image.SmartImageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

/**
 * 广告图片自动轮播控件
 * 
 */
public class ImageCycleView extends LinearLayout {
	/**
	 * 上下文
	 */
	private Context mContext;
	/**
	 * 图片轮播视图
	 */
	private DecoratorViewPager mAdvPager = null;
	/**
	 * 滚动图片视图适配
	 */
	private ImageCycleAdapter mAdvAdapter;
	/**
	 * 图片轮播指示器控件
	 */
	private ViewGroup mGroup;

	/**
	 * 图片轮播指示个图
	 */
	private ImageView mImageView = null;

	/**
	 * 滚动图片指示视图列表
	 */
	private ImageView[] mImageViews = null;

	/**
	 * 图片滚动当前图片下标
	 */

	private boolean isStop;

	/**
	 * 游标是圆形还是长条，要是设置为0是长条，要是1就是圆形 默认是圆形,2表示刷新adapter
	 */
	public int stype = 0;

	/**
	 * 当前选中
	 */
	private int subscript = 0;

	/**
	 * 当前viewpage是否滚动,默认状态是滚动的
	 */
	private boolean roll = true;

	/**
	 * 广告图片点击监听
	 */
	private ImageCycleViewListener mImageCycleViewListener;

	/**
	 * @param context
	 */
	public ImageCycleView(Context context) {
		super(context);
	}

	/**
	 * 图片资源列表
	 */
	private ArrayList<String> mAdList = new ArrayList<String>();

	/**
	 * @param context
	 * @param attrs
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("Recycle")
	public ImageCycleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.ad_cycle_view, this);
		mAdvPager = (DecoratorViewPager) findViewById(R.id.adv_pager);
		mAdvPager.setNestedpParent((ViewGroup)mAdvPager.getParent());
		mAdvPager.setOnPageChangeListener(new GuidePageChangeListener());
		// 滚动图片右下指示器视
		mGroup = (ViewGroup) findViewById(R.id.viewGroup);
	}

	/**
	 * viewpage监听
	 */
	public void setOnImageCycleViewListener(ImageCycleViewListener mImageCycleViewListener) {
		this.mImageCycleViewListener = mImageCycleViewListener;
	}

	/**
	 * 触摸停止计时器，抬起启动计时器
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			// 开始图片滚动
			startImageTimerTask();
		} else {
			// 停止图片滚动
			stopImageTimerTask();
		}
		return super.dispatchTouchEvent(event);
	}

	/**
	 * 装填图片数据
	 * 
	 * @param imageUrlList
	 * @param imageCycleViewListener
	 */
	public void setImageResources(ArrayList<String> imageUrlList, int stype) {
		this.stype = stype;
		this.mAdList = imageUrlList;
		// 清除
		mGroup.removeAllViews();
		// 图片广告数量
		int imageCount = imageUrlList.size();
		mImageViews = new ImageView[imageCount];
		for (int i = 0; i < imageCount; i++) {
			mImageView = new ImageView(mContext);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.leftMargin = 10;
			mImageView.setScaleType(ScaleType.CENTER_CROP);
			mImageView.setLayoutParams(params);
			mImageViews[i] = mImageView;
			if (i == 0) {
				mImageViews[i].setBackgroundResource(R.drawable.dot_selected);
			} else {
				mImageViews[i].setBackgroundResource(R.drawable.dot_unselected);
			}
			mGroup.addView(mImageViews[i]);
		}
		mAdvAdapter = new ImageCycleAdapter();
		mAdvPager.setAdapter(mAdvAdapter);
		// 进入后就能左右滑动的重要代码----刷新有bug
		mAdvPager.setCurrentItem(imageCount * 10);
		startImageTimerTask();
	}

	/**
	 * 图片轮播(手动控制自动轮播与否，便于资源控件）
	 */
	public void startImageCycle() {
		startImageTimerTask();
	}

	/**
	 * 暂停轮播—用于节省资源
	 */
	public void pushImageCycle() {
		stopImageTimerTask();
	}

	/**
	 * 图片滚动任务
	 */
	private void startImageTimerTask() {
		if (roll == true) { // 判断是否需要滚动
			stopImageTimerTask();
			// 图片滚动
			mHandler.postDelayed(mImageTimerTask, 5000);
		}
	}

	/**
	 * 停止图片滚动任务
	 */
	private void stopImageTimerTask() {
		isStop = true;
		mHandler.removeCallbacks(mImageTimerTask);
	}

	private Handler mHandler = new Handler();

	/**
	 * 整个视图不滚动
	 */
	public void StopIt() {
		roll = false;// 不滚动
		mHandler.removeCallbacks(mImageTimerTask);
	}

	/**
	 * 图片自动轮播Task
	 */
	private Runnable mImageTimerTask = new Runnable() {
		@Override
		public void run() {
			if (mImageViews != null) {
				mAdvPager.setCurrentItem(mAdvPager.getCurrentItem() + 1);
				if (!isStop) {
					// 当你退出后 要把这个给停下来 不然 这个一直存在就一直在后台循环
					mHandler.postDelayed(mImageTimerTask, 5000);
				}

			}
		}
	};

	/**
	 * 轮播图片监听
	 * 
	 */
	private class GuidePageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE)
				startImageTimerTask();
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int index) {
			index = index % mImageViews.length;
			subscript = index;
			// 设置当前显示的图片
			for (int i = 0; i < mImageViews.length; i++) {
				if (index != i) {
					mImageViews[i].setBackgroundResource(R.drawable.dot_selected);
				} else {
					mImageViews[i].setBackgroundResource(R.drawable.dot_unselected);
				}
			}
		}
	}

	private class ImageCycleAdapter extends PagerAdapter {

		private int mChildCount = 0;
		// 图片视图缓存列表
		private ArrayList<SmartImageView> mImageViewCacheList;

		public ImageCycleAdapter() {
			mImageViewCacheList = new ArrayList<SmartImageView>();
		}

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public void notifyDataSetChanged() {
			mChildCount = getCount();
			super.notifyDataSetChanged();
		}

		@Override
		public int getItemPosition(Object object) {
			if (mChildCount > 0) {
				mChildCount--;
				return POSITION_NONE;
			}
			return super.getItemPosition(object);
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			String uri = mAdList.get(position % mAdList.size());
			SmartImageView imageView = null;
			if (mImageViewCacheList.isEmpty()) {
				imageView = new SmartImageView(mContext);
				imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				// 设置图片点击监听
				imageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mImageCycleViewListener.onImageClick(subscript, v);
					}
				});
			} else {
				imageView = mImageViewCacheList.remove(0);
			}
			//判断imageUrl是否含有http://关键字
			if (uri.startsWith("http://") || uri.startsWith("https://") || uri.startsWith("file:///")
					|| uri.startsWith("content://") || uri.startsWith("assets://") || uri.startsWith("drawable://")) {
			} else {
				uri = AppConfig.ICON + uri;
			}
			imageView.setTag(uri);
			container.addView(imageView);
			imageView.setImageUrl(uri);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			SmartImageView view = (SmartImageView) object;
			mAdvPager.removeView(view);
			mImageViewCacheList.add(view);

		}

	}

	/**
	 * 单击图片事件
	 * 
	 * @param position
	 * @param imageView
	 */
	public static interface ImageCycleViewListener {
		public void onImageClick(int position, View imageView);
	}

}
