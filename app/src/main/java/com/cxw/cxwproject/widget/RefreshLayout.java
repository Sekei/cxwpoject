package com.cxw.cxwproject.widget;

import com.cxw.cxwproject.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * 强大的熟悉控件
 * 
 * @author Administrator
 *
 */
public class RefreshLayout extends PtrFrameLayout {

	public RefreshLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public RefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public RefreshLayout(Context context) {
		super(context);
		initView();
	}

	private void initView() {
		HeadView head = new HeadView(getContext());
		setHeaderView(head);
		addPtrUIHandler(head);
	}

	public static class HeadView extends FrameLayout implements PtrUIHandler {

		public HeadView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		private TextView pull_down_view;
		public HeadView(Context context) {
			super(context);
			View view = View.inflate(context, R.layout.refresh_view_header, null);
			pull_down_view = (TextView) view.findViewById(R.id.pull_down_view);
			addView(view);
		}

		@Override
		public void onUIReset(PtrFrameLayout frame) {
//			pull_down_view.setText("加载完成…");
			// parogressBar.setVisibility(View.INVISIBLE);
		}

		@Override
		public void onUIRefreshPrepare(PtrFrameLayout frame) {
			pull_down_view.setText("下拉刷新…");
			// parogressBar.setVisibility(View.INVISIBLE);
		}

		@Override
		public void onUIRefreshBegin(PtrFrameLayout frame) {
			pull_down_view.setText("正在载入…");
			//((AnimationDrawable) mHeaderImage.getDrawable()).start();
			// parogressBar.setVisibility(View.VISIBLE);
		}

		@Override
		public void onUIRefreshComplete(PtrFrameLayout frame) {
		}

		@Override
		public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status,
				PtrIndicator ptrIndicator) {
			final int mOffsetToRefresh = frame.getOffsetToRefresh();
			final int currentPos = ptrIndicator.getCurrentPosY();
			final int lastPos = ptrIndicator.getLastPosY();

			if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
				if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
					pull_down_view.setText("下拉刷新…");
				}
			} else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
				if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
					pull_down_view.setText("放开刷新…");
				}
			}
		}

	}

}
