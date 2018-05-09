package com.cxw.cxwproject.util.mvc;

import java.util.Collection;

import com.cxw.cxwproject.widget.RefreshLayout;

public class MVCViewHelper<T> extends MVCHelper {
	private T data;

	// listview 参数可以去掉 通过getContentView 来获取
	public MVCViewHelper(RefreshLayout refreshLayout) {
		super(refreshLayout);
	}

	public void refresh() {
		request(0);
	}

	protected void request(int page) {
		// 加载中
		if (status == STATUS_REFRESH || status == STATUS_LOADMORE || status == STATUS_NOMORE) {
			return;
		}
		if (isEmpty()) {
			getLoadView().showLoading();
		} else {
			startRefresh();
		}
		status = STATUS_REFRESH;
		if (mRequestListener != null) {
			mRequestListener.requestData(page);
		}
	}

	public void executeOnLoadDataFail(String error) {
		status = STATUS_NONE;
		stopRefresh();
		if (data == null) {
			getLoadView().showFail(error);
		} else {
			getLoadView().tipFail(error);
		}
	}

	public void executeOnLoadDataSuccess(T data) {
		status = STATUS_NONE;
		this.data = data;
		stopRefresh();
		if (isEmpty()) {
			getLoadView().showEmpty();
		} else {
			getLoadView().hide();
		}
	}

	public T getData() {
		return data;
	}

	public boolean isEmpty() {
		if (data == null) {
			return true;
		}
		if (data instanceof Collection) {
				return ((Collection<?>) data).isEmpty();
		}
		return false;
	}

}
