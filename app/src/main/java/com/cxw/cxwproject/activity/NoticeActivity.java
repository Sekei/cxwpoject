package com.cxw.cxwproject.activity;

import java.util.List;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.NoticeDataBean;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.util.mvc.MVCHelper;
import com.cxw.cxwproject.util.mvc.MVCHelper.RequestListener;
import com.cxw.cxwproject.util.mvc.MVCListViewHelper;
import com.cxw.cxwproject.widget.EmptyLayout;
import com.cxw.cxwproject.widget.RefreshLayout;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class NoticeActivity extends ActionBarActivity implements RequestListener, OnClickListener, OnItemClickListener {
	private ListView notice_list;
	private MVCListViewHelper<NoticeDataBean> mvcHelper;
	private QuickAdapter<NoticeDataBean> adapter;
	private List<NoticeDataBean> mdata;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_notice;
	}

	@Override
	public int getActionBarType() {
		return ACTIONBAR_NOT_SHOW;
	}

	@Override
	protected void initView() {
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("消息中心");
		ImageView back = (ImageView) findViewById(R.id.back); // 返回
		back.setOnClickListener(this);
		notice_list = (ListView) findViewById(R.id.notice_list);
		notice_list.setOnItemClickListener(this);

		RefreshLayout swipeLayout = (RefreshLayout) findViewById(R.id.swipelayout);
		EmptyLayout emptyLayout = (EmptyLayout) findViewById(R.id.empty_view);
		adapter = new QuickAdapter<NoticeDataBean>(this, R.layout.notice_item) {
			@Override
			protected void convert(final BaseAdapterHelper helper, NoticeDataBean data) {
				helper.setText(R.id.send_time, data.getSend_time()).setText(R.id.title, data.getTitle())
						.setText(R.id.introduction, data.getIntroduction());
//				helper.setTextColor(R.id.sign_tv, data.getIs_read() == 0 ? getResources().getColor(R.color.uli_bg)
//						: getResources().getColor(R.color.uli_797979));
//				helper.setText(R.id.sign_tv, data.getIs_read() == 0 ? "未读" : "已读");
			}
		};
		// 网络请求
		mvcHelper = MVCHelper.createListHelper(swipeLayout, notice_list, emptyLayout);
		mvcHelper.setAdapter(adapter);
		mvcHelper.setRequestListener(this);
		mvcHelper.refresh();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
		Intent mIntent = new Intent(NoticeActivity.this, NotificationDetailsActivity.class);
		mIntent.putExtra("url", mdata.get(arg2).getUrl());
		startActivity(mIntent);
		
//		final LoadingDialog dialog = new LoadingDialog(this);
//		Api.getRead(mdata.get(arg2).getMessage_id(), new OnResponse<String>() {
//			@Override
//			public void onResponse(String o) {
//				dialog.dismiss();
//				mdata.get(arg2).setIs_read(1);// 1表示已读
//				adapter.notifyDataSetInvalidated(mdata);
//				Intent mIntent = new Intent(NoticeActivity.this, NotificationDetailsActivity.class);
//				mIntent.putExtra("url", mdata.get(arg2).getUrl());
//				startActivity(mIntent);
//			}
//
//			@Override
//			public void onErrorResponse(String error) {
//				dialog.dismiss();
//				Toast.show(error);
//			}
//		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:// 返回
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void requestData(int page) {
		Api.NoticeList(page + 1, new OnResponse<List<NoticeDataBean>>() {
			@Override
			public void onResponse(List<NoticeDataBean> o) {
				mdata = o;
				mvcHelper.executeOnLoadDataSuccess(o);
			}

			@Override
			public void onErrorResponse(String error) {
				mvcHelper.executeOnLoadDataFail(error);
			}
		});
	}

}
