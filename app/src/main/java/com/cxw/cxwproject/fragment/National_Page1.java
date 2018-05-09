package com.cxw.cxwproject.fragment;

import java.util.ArrayList;
import java.util.List;

import com.cxw.cxwproject.BaseFragment;
import com.cxw.cxwproject.MyApp;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.activity.DetailsActivity;
import com.cxw.cxwproject.bean.CommodityBean;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.util.GrapeGridview;
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

public class National_Page1 extends BaseFragment implements OnItemClickListener, RequestListener {
	GrapeGridview specialty;
	MVCViewHelper<CommodityBean> mvcViewHelper;
	QuickAdapter<CommodityBean> adapter;

	List<CommodityBean> mcommbean = new ArrayList<CommodityBean>();

	protected int getLayoutId() {
		return R.layout.national_page1;
	}

	@Override
	protected void initView(View view) {
		super.initView(view);
		// 通过getactivity获取父控件ID
		NoScrollView noScrollView = (NoScrollView) getActivity().findViewById(R.id.msv);
		// 子类scrollview控制父类滚动事件
		InnerScrollView innerScrollView = (InnerScrollView) view.findViewById(R.id.scroll_2);
		innerScrollView.parentScrollView = noScrollView;
		// 绑定数据
		specialty = (GrapeGridview) view.findViewById(R.id.specialty);
		specialty.setOnItemClickListener(this);
		// 网络请求加载显示页面
		EmptyLayout emptyLayout = (EmptyLayout) view.findViewById(R.id.empty_view);
		mvcViewHelper = MVCHelper.createViewHelper(null, emptyLayout);
		mvcViewHelper.setRequestListener(this);
		mvcViewHelper.refresh();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent mIntent = new Intent(getActivity(), DetailsActivity.class);
		mIntent.putExtra("data_0", mcommbean.get(position).getProduct_id());
		startActivity(mIntent);
	}

	@Override
	public void requestData(int page) {
		Api.getCommodity(getArguments().getString("id"), new OnResponse<List<CommodityBean>>() {
			@Override
			public void onResponse(List<CommodityBean> item) {
				if (item.size() != 0) {
					mvcViewHelper.executeOnLoadDataSuccess(item.get(0));
				} else {
					mvcViewHelper.executeOnLoadDataSuccess(null);
				}
				mcommbean = item;
				specialty.setAdapter(new QuickAdapter<CommodityBean>(getActivity(), R.layout.item_specialty01, item) {
					@Override
					protected void convert(BaseAdapterHelper helper, CommodityBean item) {
						helper.setImageUrl(R.id.img, item.getImage());
						int display = MyApp.getApp().getDisplayHightAndWightPx()[1];
						int width = display / 2;
						helper.setImageViewLinearLayout(R.id.img, width, width);
						helper.setText(R.id.name, item.getName());
						helper.setText(R.id.price, item.getPrice());
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
