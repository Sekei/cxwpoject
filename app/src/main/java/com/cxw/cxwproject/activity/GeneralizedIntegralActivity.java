package com.cxw.cxwproject.activity;

import java.util.Arrays;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.SignBean;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class GeneralizedIntegralActivity extends ActionBarActivity implements OnClickListener {
	int type;
	EditText command;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_generalized_integral;
	}

	@Override
	public int getActionBarType() {
		return ACTIONBAR_NOT_SHOW;
	}

	@Override
	protected void initView() {
		type = getIntent().getIntExtra("id", 0);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(type == 0 ? "关注微信" : "关注微博");
		ImageView back = (ImageView) findViewById(R.id.back); // 返回
		back.setOnClickListener(this);
		// 口令输入框
		command = (EditText) findViewById(R.id.command);
		// 领取
		TextView generalized_btn = (TextView) findViewById(R.id.generalized_btn);
		generalized_btn.setOnClickListener(this);
		ListView generalized_list = (ListView) findViewById(R.id.generalized_list);
		generalized_list
				.setAdapter(new QuickAdapter<String>(this, R.layout.item_receive, Arrays.asList("第一步", "第二步", "第三步")) {
					String str[] = { "关注“瑞坤扬”微信平台（微信号：CXWWORLD）。", "在“瑞坤扬”回复“瑞坤扬”，获取瑞坤扬口令。",
							"返回瑞坤扬APP，在此页面输入口令，即可获得100积分。" };
					String str1[] = { "关注“@瑞坤扬”官方微博。", "关注后，微博自动发送瑞坤扬口令的私信给您；如果您已经关注了微博，那么您可以在微博聊天框中回复“瑞坤扬”，获取正确口令。",
							"返回瑞坤扬APP，在此页面输入口令，即可获得100积分。" };

					@Override
					protected void convert(BaseAdapterHelper helper, String item) {
						helper.setText(R.id.title, item);
						helper.setVisible(R.id.title, true);
						if (type == 0) {
							helper.setText(R.id.step_content, str[helper.getPosition()]);
						} else {
							helper.setText(R.id.step_content, str1[helper.getPosition()]);
						}
					}
				});
	}

	/**
	 * 积分领取
	 */
	private void Generalized() {
		final LoadingDialog loading = new LoadingDialog(this);
		Api.getGeneralized(type, command.getText().toString().trim(), new OnResponse<SignBean>() {
			@Override
			public void onResponse(SignBean o) {
				loading.dismiss();
				// 重新赋值保存
				UserBean.defaultShop().getIntegral().setIntegral(o.getIntegral());
				Intent intent = new Intent(GeneralizedIntegralActivity.this, SignActivity.class);
				intent.putExtra("result", o.getIntegral());
				setResult(100100, intent);
				finish();
			}

			@Override
			public void onErrorResponse(String error) {
				loading.dismiss();
				ToastUtils.show(error);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.generalized_btn:
			Generalized();// 领取积分
			break;
		default:
			break;
		}

	}
}
