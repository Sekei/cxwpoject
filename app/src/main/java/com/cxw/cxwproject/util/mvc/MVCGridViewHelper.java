package com.cxw.cxwproject.util.mvc;

import java.util.ArrayList;
import java.util.List;

import com.cxw.cxwproject.R;
import com.cxw.cxwproject.widget.RefreshLayout;
import com.joanzapata.android.BaseQuickGridViewAdapter.OnLastViewCreate;
import com.joanzapata.android.QuickGridViewAdapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MVCGridViewHelper<T> extends MVCHelper implements OnScrollListener {

	private static final int PAGE_SIZE = 20;
	private int pageSize = PAGE_SIZE;

	private GridView listView;
	private QuickGridViewAdapter<T> adapter;
	private LastView lastView;

	private boolean autoLoadMore = true;
	private boolean showNoMore = true;

	private int currentPage = 0;

	public MVCGridViewHelper(RefreshLayout refreshLayout, GridView listView) {
		super(refreshLayout);
		this.listView = listView;
		listView.setOnScrollListener(this);
	}

	public MVCGridViewHelper(GridView listView) {
		this(null, listView);
	}

	public void setPageSize(int size) {
		pageSize = size;
	}

	public int getPageSize() {
		return pageSize;
	}

	class LastView implements OnLastViewCreate {

		public View convertView;
		private TextView footView;
		private View progress;
		private ImageView pull_img;

		public void refresh() {
			if (progress == null) {
				return;
			}
			progress.setVisibility(View.GONE);
			pull_img.setVisibility(View.GONE);
			if (status == STATUS_NETWORK_ERROR) {
				footView.setText("网络异常");
			} else if (status == STATUS_LOADMORE) {
				footView.setText("加载中...");
				progress.setVisibility(View.VISIBLE);
			} else if (status == STATUS_REFRESH) {
				pull_img.setVisibility(View.VISIBLE);
				footView.setText("亲，没有更多了~");// 这里不知道是什么原因
			} else if (status == STATUS_NONE) {
				footView.setText("点击加载更多");
			} else if (status == STATUS_NOMORE) {
				pull_img.setVisibility(View.VISIBLE);
				footView.setText("亲，没有更多了~");
			} else {
				footView.setText("" + status);
			}
		}

		@Override
		public View createLastView(View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(parent.getContext(), R.layout.item_cell_footer, null);
				footView = (TextView) convertView.findViewById(R.id.pull_tv);
				progress = convertView.findViewById(R.id.progressbar);
				footView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						loadMore();
					}
				});
				//这里适配
				this.convertView = convertView;
			}
			refresh();
			return convertView;
		}
	}

	public void setAdapter(QuickGridViewAdapter<T> adapter) {
		lastView = new LastView();
		adapter.setCreateLastViewListener(lastView);
		this.adapter = adapter;
		this.listView.setAdapter(adapter);
		//this.adapter.showIndeterminateProgress(showNoMore);
	}

	public void loadMore() {
		request(currentPage + 1);
	}

	protected void request(int page) {
		// 加载中
		if (status == STATUS_REFRESH || status == STATUS_LOADMORE) {
			return;
		}
		if (page == 0) {// 刷新
			status = STATUS_REFRESH;
			startRefresh();
			listView.setSelection(0);
			if (adapter.getDataSize() == 0) {
				getLoadView().showLoading();
			}

		} else {// 加载更多
			if (adapter.getDataSize() == 0 || status == STATUS_NOMORE || !showNoMore) {// 如果没有数据，怎么可能加载更多,或者没有更多了
				return;
			}
			status = STATUS_LOADMORE;
			if (lastView != null) {
				lastView.refresh();
			}
		}
		setCanRefresh(false);
		if (mRequestListener != null) {
			mRequestListener.requestData(page);
		}
	}

	public void executeOnLoadDataFail(String error) {
		stopRefresh();
		if (adapter.getDataSize() == 0) {
			getLoadView().showFail(error);
		} else {
			if (status == STATUS_REFRESH) {
				getLoadView().showFail(error);
			}
		}
		status = STATUS_NETWORK_ERROR;
		adapter.notifyDataSetChanged();
	}

	public void executeOnLoadDataSuccess(List<T> data) {
		if (data == null) {
			data = new ArrayList<T>();
		}
		stopRefresh();
		if (status == STATUS_REFRESH) {
			currentPage = 0;

			if (data.isEmpty()) {
				getLoadView().showEmpty();
				status = STATUS_NODATA;
			} else {
				if (data.size() < pageSize) {
					status = STATUS_NOMORE;
				} else {
					status = STATUS_NONE;
				}
				getLoadView().hide();
				adapter.replaceAll(data);
				if (lastView != null) {
					lastView.refresh();
				}
			}
		} else {
			currentPage++;
			if (data.size() < pageSize) {
				status = STATUS_NOMORE;
				lastView.refresh();
			} else {
				status = STATUS_NONE;
				lastView.refresh();
			}
			adapter.addAll(data);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 不自动加载，没有更多，记载中
		if (!autoLoadMore || status == STATUS_NOMORE || status == STATUS_LOADMORE) {
			return;
		}
		// 尚未加载过数据
		if (adapter == null || adapter.getDataSize() == 0) {
			return;
		}
		// 判断是否滚动到底部
		boolean scrollEnd = false;
		try {
			if (view.getCount() - 1 == view.getLastVisiblePosition())
				scrollEnd = true;
		} catch (Exception e) {
			scrollEnd = false;
		}
		if (status == STATUS_NONE && scrollEnd) {
			loadMore();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}
}
