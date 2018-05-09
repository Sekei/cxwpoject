package com.cxw.cxwproject.fragment;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cxw.cxwproject.BaseFragment;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.activity.LoginActivity;
import com.cxw.cxwproject.activity.NoticeActivity;
import com.cxw.cxwproject.activity.SignActivity;
import com.cxw.cxwproject.adapter.HomeListAdapter;
import com.cxw.cxwproject.bean.HomesBean;
import com.cxw.cxwproject.imp.HomeDateImp;
import com.cxw.cxwproject.tool.SharePreferenceUtil;
import com.cxw.cxwproject.util.mvc.PullRefeshListViewBase;
import com.cxw.cxwproject.util.mvc.PullRefreshListView;
import com.cxw.cxwproject.widget.EmptyLayout;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.joanzapata.android.QuickAdapter;

/*
 * 首页
 * @author Sekei
 */
public class FragmentPage1 extends BaseFragment implements PullRefeshListViewBase.RequestListener, OnClickListener{
    //改版刷新
    private PullRefreshListView<HomesBean> mvcHelper;
    private QuickAdapter<HomesBean> adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_1;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        // 首页大改版用listview来加载每一个item
        PullToRefreshListView pullRefresh = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_homelist);
        // 通知消息类型
        TextView notice = (TextView) view.findViewById(R.id.notice);
        notice.setOnClickListener(this);
        // 签到类型
        TextView sign_stn = (TextView) view.findViewById(R.id.sign_stn);
        sign_stn.setOnClickListener(this);
        EmptyLayout emptyLayout = (EmptyLayout) view.findViewById(R.id.empty_view);
        adapter = new HomeListAdapter(getActivity(), R.layout.homelist_item);
        // 网络请求
        mvcHelper = PullRefeshListViewBase.createListHelper(pullRefresh, emptyLayout);
        mvcHelper.setAdapter(adapter);
        mvcHelper.setRequestListener(this);
        mvcHelper.refresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notice:// 消息
                startActivity(NoticeActivity.class);
                break;
            case R.id.sign_stn:// 签到
                setStartActivity(SignActivity.class);
                break;
        }

    }

    /*
     * 页面跳转统一处理
     */
    private void setStartActivity(Class<?> cls) {
        if (new SharePreferenceUtil().getCookie() != null) {
            startActivity(cls);
        } else {
            startActivity(LoginActivity.class, 1);
        }
    }

    @Override
    public void requestData(int page) {
        HomeDateImp.getInstance().HomeDateImpApi(mvcHelper);
    }
}