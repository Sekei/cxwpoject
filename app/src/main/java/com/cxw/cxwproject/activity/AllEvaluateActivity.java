package com.cxw.cxwproject.activity;

import java.util.List;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.adapter.UitimateAdapter;
import com.cxw.cxwproject.bean.CommentsBean;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.util.CustomListView;
import com.cxw.cxwproject.util.mvc.MVCHelper.RequestListener;
import com.cxw.cxwproject.util.mvc.MVCListViewHelper;
import com.cxw.cxwproject.widget.EmptyLayout;
import com.cxw.cxwproject.widget.RefreshLayout;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AllEvaluateActivity extends ActionBarActivity implements RequestListener, OnClickListener {
	MVCListViewHelper<CommentsBean> mvcHelper;
	QuickAdapter<CommentsBean> adapter;
	ListView evaluate_list;
	String product_id;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_allevaluate;
	}

	@Override
	public int getActionBarType() {
		return ACTIONBAR_NOT_SHOW;
	}

	@Override
	protected void initView() {
		product_id = getIntentObject(String.class);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("全部评价");
		ImageView back = (ImageView) findViewById(R.id.back); // 返回
		back.setOnClickListener(this);
		CustomListView mcustomlist = (CustomListView) findViewById(R.id.mcustomlist);
		String[] arr = { "全部评价(100)", "好评(60)", "中评(30)", "差评(10)", "有图(45)" };
		UitimateAdapter uitimate = new UitimateAdapter(this, arr);
		mcustomlist.setAdapter(uitimate);
		evaluate_list = (ListView) findViewById(R.id.evaluate_list);
		// 开始绑定数据
		adapter = new QuickAdapter<CommentsBean>(this, R.layout.item_evaluate) {
			@Override
			protected void convert(BaseAdapterHelper helper, CommentsBean item) {
				helper.setImageUrl(R.id.user_img, item.getAvatar());
				helper.setText(R.id.user_name, item.getNickname());
				helper.setText(R.id.datatime, item.getCreated());
				helper.setText(R.id.content, item.getContent());
			}
		};
		// 网络请求加载显示页面
		EmptyLayout emptyLayout = (EmptyLayout) findViewById(R.id.empty_view);
		RefreshLayout swipeLayout = (RefreshLayout)findViewById(R.id.swipelayout);
		// 网络请求
		mvcHelper = MVCListViewHelper.createListHelper(swipeLayout, evaluate_list, emptyLayout);
		mvcHelper.setAdapter(adapter);
		mvcHelper.setRequestListener(this);
		mvcHelper.refresh();
	}

	@Override
	public void requestData(int page) {
		Api.getEvaluationList(page+1,product_id,0,new OnResponse<List<CommentsBean>>() {
			@Override
			public void onResponse(List<CommentsBean> o) {
				mvcHelper.executeOnLoadDataSuccess(o);
			}

			@Override
			public void onErrorResponse(String error) {
				mvcHelper.executeOnLoadDataFail(error);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}

	}
}
