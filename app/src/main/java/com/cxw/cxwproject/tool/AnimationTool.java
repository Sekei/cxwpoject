package com.cxw.cxwproject.tool;

import com.cxw.cxwproject.util.TLog;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class AnimationTool {
	AlphaAnimation mShowAnimation = null;

	public static AnimationTool getAnimation() {
		return new AnimationTool();
	}

	/*
	 * View渐现动画效果
	 */
	public void setShowAnimation(View view, int duration) {
		if (null == view || duration < 0) {
			return;
		}
		if (null != mShowAnimation) {
			mShowAnimation.cancel();
		}
		mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
		mShowAnimation.setDuration(duration);
		mShowAnimation.setFillAfter(true);
		// 为Animation设置监听器
		mShowAnimation.setAnimationListener(new RemoveAnimationListener());
		view.startAnimation(mShowAnimation);
	}


	class RemoveAnimationListener implements AnimationListener {

		@Override
		public void onAnimationEnd(Animation animation) {
			listener.animationEnd();
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			TLog.d("repeat");
		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			TLog.d("start");
		}

	}

	private onAnimationEnd listener;

	public AnimationTool setOnAnimationEnd(onAnimationEnd listener) {
		this.listener = listener;
		return this;
	}

	public interface onAnimationEnd {
		void animationEnd();
	}
}
