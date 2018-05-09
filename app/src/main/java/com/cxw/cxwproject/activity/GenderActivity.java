package com.cxw.cxwproject.activity;

import java.util.Arrays;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.DataBean;
import com.cxw.cxwproject.bean.UserBean;
import com.cxw.cxwproject.dialog.LoadingDialog;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.widget.ToastUtils;
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

public class GenderActivity extends ActionBarActivity implements OnItemClickListener, OnClickListener {
	QuickAdapter<String> adapter;
	int GENDERPOSITION;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_gender;
	}

	@Override
	public int getActionBarType() {
		return ACTIONBAR_NOT_SHOW;
	}

	@Override
	protected void initView() {
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("修改性别");
		ImageView back = (ImageView) findViewById(R.id.back); // 返回
		back.setOnClickListener(this);
		if (UserBean.Gender() == "男") {
			GENDERPOSITION = 0;
		} else if (UserBean.Gender() == "女") {
			GENDERPOSITION = 1;
		} else {
			GENDERPOSITION = 2;
		}
		ListView gender_list = (ListView) findViewById(R.id.gender_list);
		adapter = new QuickAdapter<String>(this, R.layout.item_gender, Arrays.asList("男", "女", "保密")) {
			@Override
			protected void convert(BaseAdapterHelper helper, String item) {
				helper.setText(R.id.gender_item, item);
				if (helper.getPosition() == GENDERPOSITION) {
					helper.setVisible(R.id.gender_img, true);
				} else {
					helper.setVisible(R.id.gender_img, false);
				}
			}
		};
		gender_list.setAdapter(adapter);
		gender_list.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		this.GENDERPOSITION = position;
		getNickName();
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

	/**
	 * 昵称保存
	 */
	private void getNickName() {
		final LoadingDialog loading=new LoadingDialog(this);
		String Gender_Str = GENDERPOSITION == 0 ? "M" : (GENDERPOSITION == 1 ? "F" : "S");
		Api.getNickName("", "", "", Gender_Str, new OnResponse<DataBean>() {
			@Override
			public void onResponse(DataBean o) {
				loading.dismiss();
				ToastUtils.show("修改成功");
				adapter.notifyDataSetInvalidated();
				UserBean.defaultShop().setGender(o.getGender());
				setResult(3, getIntent());
				finish();// 关闭
			}

			@Override
			public void onErrorResponse(String error) {
				loading.dismiss();
				ToastUtils.show(error);
			}
		});
	}

}
