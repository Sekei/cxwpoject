package com.cxw.cxwproject.fragment;

import java.util.List;

import com.cxw.cxwproject.BaseFragment;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.activity.AdvertisementActivity;
import com.cxw.cxwproject.activity.NotificationDetailsActivity;
import com.cxw.cxwproject.bean.NewsBean;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.util.mvc.MVCHelper;
import com.cxw.cxwproject.util.mvc.MVCHelper.RequestListener;
import com.cxw.cxwproject.util.mvc.MVCViewHelper;
import com.cxw.cxwproject.widget.EmptyLayout;
import com.cxw.cxwproject.widget.InnerScrollView;
import com.cxw.cxwproject.widget.NoScrollView;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class National_Page2 extends BaseFragment implements RequestListener, OnItemClickListener {
	ListView journalismdata;
	MVCViewHelper<NewsBean> mvcViewHelper;

	QuickAdapter<NewsBean> adapter;

	List<NewsBean> mdata;

	protected int getLayoutId() {
		return R.layout.national_page2;
	}

	@Override
	protected void initView(View view) {
		super.initView(view);
		// 通过getactivity获取父控件ID
		NoScrollView noScrollView = (NoScrollView) getActivity().findViewById(R.id.msv);
		// 子类scrollview控制父类滚动事件
		InnerScrollView innerScrollView = (InnerScrollView) view.findViewById(R.id.scroll_page2);
		innerScrollView.parentScrollView = noScrollView;
		journalismdata = (ListView) view.findViewById(R.id.journalismdata);
		journalismdata.setOnItemClickListener(this);
		// 网络请求加载显示页面
		EmptyLayout emptyLayout = (EmptyLayout) view.findViewById(R.id.empty_view);
		mvcViewHelper = MVCHelper.createViewHelper(null, emptyLayout);
		mvcViewHelper.setRequestListener(this);
		mvcViewHelper.refresh();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(getActivity(), AdvertisementActivity.class);
		intent.putExtra("url", mdata.get(position).getUrl());
		startActivity(intent);
	}

	@Override
	public void requestData(int page) {
		Api.getNews(getArguments().getString("id"), new OnResponse<List<NewsBean>>() {
			@Override
			public void onResponse(List<NewsBean> item) {
				if (item.size() != 0) {
					mvcViewHelper.executeOnLoadDataSuccess(item.get(0));
				} else {
					mvcViewHelper.executeOnLoadDataSuccess(null);
				}
				mdata = item;
				journalismdata.setAdapter(new QuickAdapter<NewsBean>(getActivity(), R.layout.item_specialty02, item) {
					@Override
					protected void convert(BaseAdapterHelper helper, NewsBean item) {
						helper.setImageUrl(R.id.img, item.getImg_url());
						helper.setText(R.id.title, item.getTitle());
						helper.setText(R.id.time, item.getTime());
					}
				});
			}

			@Override
			public void onErrorResponse(String error) {
				mvcViewHelper.executeOnLoadDataFail(error);
			}
		});
	}

}
