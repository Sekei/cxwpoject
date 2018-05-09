package com.cxw.cxwproject.activity;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.UserBean;
import com.cxw.cxwproject.dialog.LoadingDialog;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.tool.SharePreferenceUtil;
import com.cxw.cxwproject.widget.ToastUtils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class OpinionActivity extends ActionBarActivity implements OnClickListener {
	private EditText opinion_view;
	private TextView number;
	private LoadingDialog mDialog;
	
	@Override
	protected int getLayoutId() {
		return R.layout.activity_opinion;
	}

	@Override
	protected void initView() {
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("意见反馈");
		ImageView back = (ImageView) findViewById(R.id.back); // 返回
		back.setOnClickListener(this);
		// 提交按钮
		Button opinion_btn = (Button) findViewById(R.id.opinion_btn);
		opinion_btn.setOnClickListener(this);
		// 留言
		opinion_view = (EditText) findViewById(R.id.opinion_view);
		opinion_view.addTextChangedListener(mTextWatcher);
		// 字数限制
		number = (TextView) findViewById(R.id.number);
	}

	TextWatcher mTextWatcher = new TextWatcher() {
		private CharSequence temp;

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			temp = s;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			number.setText(temp.length() + "/150字");
		}
	};
	
	OnResponse<String> opinionSubmit=new OnResponse<String>() {
		@Override
		public void onResponse(String o) {
			mDialog.dismiss();
			ToastUtils.show(o);
			finish();
		}

		@Override
		public void onErrorResponse(String error) {
			mDialog.dismiss();
			ToastUtils.show(error);
		}
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:// 返回
			finish();
			break;
		case R.id.opinion_btn:
			mDialog = new LoadingDialog(this);
			Api.getOpinion(opinion_view.getText().toString(),opinionSubmit);
			break;
		default:
			break;
		}
	}

}
