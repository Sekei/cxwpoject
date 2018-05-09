package com.cxw.cxwproject.activity;

import java.util.List;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.adapter.ExpandableListAdapter;
import com.cxw.cxwproject.bean.HelpBean;
import com.cxw.cxwproject.dialog.CustomDialog;
import com.cxw.cxwproject.dialog.CustomDialog.Builder;
import com.cxw.cxwproject.dialog.LoadingDialog;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.widget.ToastUtils;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HelpActivity extends ActionBarActivity implements OnGroupClickListener, OnClickListener {
	private ExpandableListAdapter adapter;
	private ExpandableListView expandableListView;
	private LoadingDialog mDialog;
	
	@Override
	protected int getLayoutId() {
		return R.layout.activity_help;
	}

	@Override
	protected void initView() {
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("客服与帮助");
		ImageView back = (ImageView) findViewById(R.id.back); // 返回
		back.setOnClickListener(this);
		LinearLayout online_btn = (LinearLayout) findViewById(R.id.online_btn);
		online_btn.setOnClickListener(this);
		// 电话
		LinearLayout iphone_btn = (LinearLayout) findViewById(R.id.iphone_btn);
		iphone_btn.setOnClickListener(this);
		// 绑定数据
		expandableListView = (ExpandableListView) findViewById(R.id.explist);
		expandableListView.setOnGroupClickListener(this);
		// 网络请求加载显示页面
		mDialog = new LoadingDialog(this);
		Api.getHelp(getHelpHttp);
	}

	OnResponse<List<HelpBean>> getHelpHttp = new OnResponse<List<HelpBean>>() {
		@Override
		public void onResponse(List<HelpBean> o) {
			if (o.size() != 0) {
				// 开始进行数据处理
				String[] generalsTypes= new String[o.size()];
				String[][] generals=new String[o.size()][];
				for (int i = 0; i < o.size(); i++) {
					generalsTypes[i]=o.get(i).getQuestion();
					String[] Children=new String[1];
					Children[0]=o.get(i).getAnswer();
					generals[i]=Children;
				}
				adapter = new ExpandableListAdapter(HelpActivity.this,generalsTypes,generals);
				expandableListView.setAdapter(adapter);
			}
			mDialog.dismiss();
		}

		@Override
		public void onErrorResponse(String error) {
			mDialog.dismiss();
			ToastUtils.show(error);
		}
	};

	/**
	 * 拨打电话提示框
	 */
	private void getIphone() {
		CustomDialog.Builder builder = new Builder(this);
		builder.setTitle("提示");
		builder.setMessage("确定要拨打客服电话么？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
				Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:400-995-7227"));
				startActivity(phoneIntent);
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
		case R.id.back:// 返回
			finish();
			break;
		case R.id.online_btn:
			startActivity(MechatActivity.class);
			break;
		case R.id.iphone_btn:
			getIphone();// 拨打电话
			break;
		}
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
		adapter.getGroupPosion(groupPosition);
		return false;
	}

}
