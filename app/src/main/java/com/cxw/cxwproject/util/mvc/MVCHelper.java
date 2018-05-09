package com.cxw.cxwproject.util.mvc;

import com.cxw.cxwproject.widget.EmptyLayout;
import com.cxw.cxwproject.widget.RefreshLayout;

import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public abstract class MVCHelper {

	public static final int STATUS_NONE = 0;// 点击加载更多
	public static final int STATUS_REFRESH = 1;// 下拉刷新中
	public static final int STATUS_LOADMORE = 2;// 上拉加载中
	public static final int STATUS_NODATA = 3;// 没有数据
	public static final int STATUS_NOMORE = 4;// 没有更多
	public static final int STATUS_NETWORK_ERROR = 5;

	private RefreshLayout refreshLayout;
	private ILoadView loadView;
	protected int status = STATUS_NONE;
	protected RequestListener mRequestListener;

	public MVCHelper(RefreshLayout refreshLayout) {
		this.refreshLayout = refreshLayout;
		if (this.refreshLayout != null) {
			refreshLayout.setPtrHandler(new PtrHandler() {

				@Override
				public void onRefreshBegin(PtrFrameLayout frame) {
					refresh();
				}

				@Override
				public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
					return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
				}
			});
		}
	}

	public final void setLoadView(ILoadView l) {
		loadView = l;
	}
	public final void setRequestListener(RequestListener l) {
		mRequestListener = l;
	}

	public void refresh() {
		request(0);
	}

	protected abstract void request(int page);

	public abstract void executeOnLoadDataFail(String error);

	protected void stopRefresh() {
		if (refreshLayout != null) {
			refreshLayout.refreshComplete();
			setCanRefresh(true);
		}
	}

	protected void startRefresh() {
		if (refreshLayout != null) {
			// refreshLayout.setRefreshing(true);
			refreshLayout.autoRefresh(true);
			setCanRefresh(false);
		}
		// 防止多次重复刷新

	}

	protected ILoadView getLoadView() {
		if (loadView == null) {
			loadView = new ILoadView() {
				@Override
				public void showLoading() {

				}

				@Override
				public void showFail(String e) {

				}

				@Override
				public void showEmpty() {

				}

				@Override
				public void tipFail(String e) {

				}

				@Override
				public void hide() {

				}
			};
		}
		return loadView;
	}

	protected void setCanRefresh(boolean b) {
		if (refreshLayout != null) {
			refreshLayout.setEnabled(b);
		}
	}
	public static interface RequestListener {
		void requestData(int page);
	}
	/**
	 * GridView刷新
	 * 
	 * @param refreshLayout
	 * @param
	 * @param emptyLayout
	 * @return
	 */
	public static <T> MVCGridViewHelper<T> createGridHelper(RefreshLayout refreshLayout, GridView listView,
			EmptyLayout emptyLayout) {
		final MVCGridViewHelper<T> helper = new MVCGridViewHelper<T>(refreshLayout, listView);
		helper.setLoadView(emptyLayout);
		emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				helper.refresh();
			}
		});
		return helper;
	}

	/**
	 * ListView刷新
	 * 
	 * @param refreshLayout
	 * @param listView
	 * @param emptyLayout
	 * @return
	 */
	public static <T> MVCListViewHelper<T> createListHelper(RefreshLayout refreshLayout, ListView listView,
			EmptyLayout emptyLayout) {
		final MVCListViewHelper<T> helper = new MVCListViewHelper<T>(refreshLayout, listView);
		helper.setLoadView(emptyLayout);
		emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				helper.refresh();
			}
		});
		return helper;
	}

	public static <T> MVCViewHelper<T> createViewHelper(RefreshLayout refreshLayout, EmptyLayout emptyLayout) {
		final MVCViewHelper<T> helper = new MVCViewHelper<T>(refreshLayout);
		helper.setLoadView(emptyLayout);
		emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				helper.refresh();
			}
		});
		return helper;
	}
}
