package com.cxw.cxwproject.util.mvc;

import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */

public class PullRefreshListView<T> extends PullRefeshListViewBase implements PullToRefreshBase.OnRefreshListener2<ListView> {
    //listivew刷新控件
    private PullToRefreshListView listView;
    private QuickAdapter<T> adapter;
    // 默认分页第一页
    private int currentPage = 0;

    public PullRefreshListView(PullToRefreshListView listView) {
        super(listView);
        this.listView = listView;
        //mode决定刷新方式
        listView.setOnRefreshListener(this);
    }

    //表示adapter适配器
    public void setAdapter(QuickAdapter<T> adapter) {
        this.adapter = adapter;
        this.listView.setAdapter(adapter);
    }

    //封装刷新方式
    protected void request(int page) {
        if (page == 0) {// 刷新
            status = STATUS_REFRESH;
            if (adapter.getDataSize() == 0) {
                getLoadView().showLoading();
            }
        } else {// 加载更多
            if (adapter.getDataSize() == 0) {
                return;
            }
            status = STATUS_LOADMORE;
        }
        if (mRequestListener != null) {
            mRequestListener.requestData(page);
        }
    }

    public void executeOnLoadDataFail(String error) {
        listView.onRefreshComplete();
        if (adapter.getDataSize() == 0) {
            getLoadView().showFail(error);
        } else {
            getLoadView().showFail(error);
        }
        adapter.notifyDataSetChanged();
    }

    public void executeOnLoadDataSuccess(List<T> data) {
        listView.onRefreshComplete();
        if (data == null) {
            data = new ArrayList<>();
        }
        if (status == STATUS_REFRESH) {
            currentPage = 0;
            if (data.isEmpty()) {
                getLoadView().showEmpty();
            } else {
                getLoadView().hide();
                adapter.replaceAll(data);
            }
        } else {
            currentPage++;
            adapter.addAll(data);
        }
    }

    //下拉刷新
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        request(0);
    }

    //上拉加载
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        request(currentPage + 1);
    }
}
