package com.cxw.cxwproject.activity;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.DataBean;
import com.cxw.cxwproject.bean.UserBean;
import com.cxw.cxwproject.dialog.LoadingDialog;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.widget.ToastUtils;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class NicknameActivity extends ActionBarActivity implements OnClickListener {
	EditText nick_name;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_nickname;
	}

	@Override
	public int getActionBarType() {
		return ACTIONBAR_NOT_SHOW;
	}

	@Override
	protected void initView() {
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("尘晓屋昵称");
		ImageView back = (ImageView) findViewById(R.id.back); // 返回
		back.setOnClickListener(this);
		nick_name = (EditText) findViewById(R.id.nick_name);
		nick_name.setText(UserBean.defaultShop().getNickname());
		TextView nickname_btn = (TextView) findViewById(R.id.nickname_btn);
		nickname_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.nickname_btn:
			getNickName();
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
		Api.getNickName("name", "", nick_name.getText().toString().trim(), "", new OnResponse<DataBean>() {
			@Override
			public void onResponse(DataBean o) {
				loading.dismiss();
				ToastUtils.show("修改成功");
				UserBean.defaultShop().setNickname(o.getNickname());
				setResult(1, getIntent());
				finish();
			}

			@Override
			public void onErrorResponse(String error) {
				loading.dismiss();
				ToastUtils.show(error);
			}
		});
	}
}
