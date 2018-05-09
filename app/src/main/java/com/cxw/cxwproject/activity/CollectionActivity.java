package com.cxw.cxwproject.activity;

import java.util.List;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.CollectionBean;
import com.cxw.cxwproject.dialog.CustomDialog;
import com.cxw.cxwproject.dialog.CustomDialog.Builder;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.util.mvc.MVCHelper;
import com.cxw.cxwproject.util.mvc.MVCHelper.RequestListener;
import com.cxw.cxwproject.util.mvc.MVCListViewHelper;
import com.cxw.cxwproject.widget.EmptyLayout;
import com.cxw.cxwproject.widget.RefreshLayout;
import com.cxw.cxwproject.widget.ToastUtils;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CollectionActivity extends ActionBarActivity
		implements OnClickListener, RequestListener, OnItemClickListener {
	private ListView collection_list;
	MVCListViewHelper<CollectionBean> mvcHelper;
	QuickAdapter<CollectionBean> adapter;
	List<CollectionBean> mcollectionBean;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_collection;
	}

	@Override
	public int getActionBarType() {
		return ACTIONBAR_NOT_SHOW;
	}

	@Override
	protected void initView() {
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("我的收藏");
		ImageView back = (ImageView) findViewById(R.id.back); // 返回
		back.setOnClickListener(this);
		collection_list = (ListView) findViewById(R.id.collection_list);
		collection_list.setOnItemClickListener(this);// 点击事件
		adapter = new QuickAdapter<CollectionBean>(this, R.layout.item_collection) {
			@Override
			protected void convert(final BaseAdapterHelper helper, final CollectionBean goods) {
				helper.setImageUrl(R.id.collection_img, goods.getAvatar());
				helper.setText(R.id.collection_name, goods.getName());
				helper.setOnClickListener(R.id.cancelcollection_btn, new OnClickListener() {
					@Override
					public void onClick(View v) {
						CancelCollection(goods.getNation_id(), helper.getPosition());
					}
				});
			}
		};
		RefreshLayout swipeLayout = (RefreshLayout) findViewById(R.id.swipelayout);
		EmptyLayout emptyLayout = (EmptyLayout) findViewById(R.id.empty_view);
		// 网络请求
		mvcHelper = MVCHelper.createListHelper(swipeLayout, collection_list, emptyLayout);
		mvcHelper.setAdapter(adapter);
		mvcHelper.setRequestListener(this);
		mvcHelper.refresh();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent mIntent = new Intent(CollectionActivity.this, NationalPageActivity.class);
		mIntent.putExtra("id", mcollectionBean.get(position).getNation_id());
		startActivity(mIntent);
	}

	@Override
	public void requestData(int page) {
		Api.getCollection(page + 1, new OnResponse<List<CollectionBean>>() {
			@Override
			public void onResponse(List<CollectionBean> o) {
				mcollectionBean = o;
				mvcHelper.executeOnLoadDataSuccess(o);
			}

			@Override
			public void onErrorResponse(String error) {
				mvcHelper.executeOnLoadDataFail(error);
			}
		});
	}

	/**
	 * 取消收藏
	 */
	private void CancelCollection(final String Nation_id, final int Position) {
		CustomDialog.Builder builder = new Builder(this);
		builder.setTitle("温馨提示");
		builder.setMessage("确定取消当前收藏");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				Api.getCancelCollection(Nation_id, new OnResponse<String>() {
					@Override
					public void onResponse(String o) {
						ToastUtils.show(o);
						mvcHelper.refresh();
					}

					@Override
					public void onErrorResponse(String error) {
						ToastUtils.show(error);
					}
				});
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});
		builder.create().show();

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
