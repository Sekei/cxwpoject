package com.cxw.cxwproject.util.mvc;

import android.view.View;

import com.cxw.cxwproject.widget.EmptyLayout;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by Administrator on 2017/8/24.
 */

public abstract class PullRefeshListViewBase{
    protected int status = STATUS_NONE;
    public static final int STATUS_NONE = 0;// 加载中
    public static final int STATUS_REFRESH = 1;// 下拉刷新中
    public static final int STATUS_LOADMORE = 2;// 上拉加载中
    protected PullToRefreshListView refreshLayout;
    protected RequestListener mRequestListener;
    private ILoadView loadView;//界面加载覆盖

    public PullRefeshListViewBase(PullToRefreshListView refreshLayout) {
        this.refreshLayout = refreshLayout;
        if (this.refreshLayout != null) {
            ILoadingLayout startLabels = refreshLayout.getLoadingLayoutProxy(true, false);
            startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
            startLabels.setRefreshingLabel("正在载入...");// 刷新时
            startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
            ILoadingLayout endLabels = refreshLayout.getLoadingLayoutProxy(false, true);
            endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
            endLabels.setRefreshingLabel("正在载入...");// 刷新时
            endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        }
    }

    public void setLoadView(ILoadView l) {
        loadView = l;
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

    //ListView请求相关
    public static <T> PullRefreshListView<T> createListHelper(PullToRefreshListView listView, EmptyLayout emptyLayout) {
        final PullRefreshListView helper = new PullRefreshListView<>(listView);
        helper.setLoadView(emptyLayout);
        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.refresh();
            }
        });
        return helper;
    }



    public final void setRequestListener(RequestListener l) {
        mRequestListener = l;
    }

    public void refresh() {
        request(0);
    }

    protected abstract void request(int page);

    public abstract void executeOnLoadDataFail(String error);


    public interface RequestListener {
        void requestData(int page);
    }
}
