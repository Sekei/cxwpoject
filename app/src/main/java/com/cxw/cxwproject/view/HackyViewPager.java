package com.cxw.cxwproject.view;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HackyViewPager extends ViewPager {
	OnSingleHackyViewTouchListener onSingleHackyViewTouchListener;
	/** 触摸时按下的点 **/
	PointF downP = new PointF();
	/** 触摸时当前的点 **/
	PointF curP = new PointF();

	public HackyViewPager(Context context) {
		super(context);
	}

	public HackyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		try {
			return super.onInterceptTouchEvent(arg0);
		} catch (IllegalArgumentException e) {
			// 不理会
			return false;
		} catch (ArrayIndexOutOfBoundsException e) {
			// 不理会
			return false;
		}
	}
	/**
	 * 单击
	 */
	public void onSingleTouch() {
		if (onSingleHackyViewTouchListener != null) {
			onSingleHackyViewTouchListener.onSingleTouch();
		}
	}

	// 点击接口监听数据
	public interface OnSingleHackyViewTouchListener {
		public void onSingleTouch();
	}

	public void setOnSingleHackyViewTouchListener(
			OnSingleHackyViewTouchListener onSingleHackyViewTouchListener2) {
		// TODO Auto-generated method stub
		this.onSingleHackyViewTouchListener = onSingleHackyViewTouchListener2;
	}

}
