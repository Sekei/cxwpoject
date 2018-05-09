package com.cxw.cxwproject.tool;

import com.cxw.cxwproject.R;
import com.cxw.cxwproject.adapter.ViewPagerAdapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class StartGuideViewPage {
	private int[] imgIdArray = new int[] { R.drawable.guide_01, R.drawable.guide_02, R.drawable.guide_04 };
	private PageOnClik mPageOnClik;
	/**
	 * ViewPage点击事件
	 * @author Administrator
	 *
	 */
	public interface PageOnClik {
		void onPageOnClik();
	}

	public StartGuideViewPage mPageOnClik(PageOnClik mPageOnClik) {
		this.mPageOnClik = mPageOnClik;
		return this;
	}

	public static StartGuideViewPage getStartPage() {
		return new StartGuideViewPage();
	}

	public void initViews(Context context, ViewPager pager) {
		ImageView[] mImageViews = new ImageView[imgIdArray.length];
		for (int i = 0; i < mImageViews.length; i++) {
			ImageView imageView = new ImageView(context);
			mImageViews[i] = imageView;
			imageView.setBackgroundResource(imgIdArray[i]);
		}
		mImageViews[imgIdArray.length - 1].setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mPageOnClik != null) {
					mPageOnClik.onPageOnClik();
				}
			}
		});
		pager.setAdapter(new ViewPagerAdapter(mImageViews));
	}
}
