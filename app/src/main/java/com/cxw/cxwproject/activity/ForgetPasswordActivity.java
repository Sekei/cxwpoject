package com.cxw.cxwproject.activity;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.UserBean;
import com.cxw.cxwproject.dialog.LoadingDialog;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.widget.ToastUtils;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 忘记密码
 * 
 * @author Sekei
 * 
 */
public class ForgetPasswordActivity extends ActionBarActivity implements OnClickListener {
	private EditText forget_mon, forget_code, forget_pwd;
	private Button registercode;
	private TextView registration_button;
	private ImageView show_password;
	private boolean show_password_bool = true;
	// 倒计时
	private CountDownTimer timer = new CountDownTimer(60000, 1000) {
		@Override
		public void onTick(long millisUntilFinished) {
			registercode.setText((millisUntilFinished / 1000) + "(s)重新获取");
			registercode.setEnabled(false);
		}

		@Override
		public void onFinish() {
			registercode.setText("获取验证码");
			registercode.setEnabled(true);
		}
	};

	@Override
	protected int getLayoutId() {
		return R.layout.activity_forgetpassword;
	}

	@Override
	public int getActionBarType() {
		return ACTIONBAR_NOT_SHOW;
	}

	@Override
	protected void initView() {
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("重置密码");
		forget_mon = (EditText) findViewById(R.id.registration_mon);
		forget_mon.addTextChangedListener(watcher);
		forget_pwd = (EditText) findViewById(R.id.registration_pwd);
		forget_code = (EditText) findViewById(R.id.registration_verification_code);
		// 获取验证码
		registercode = (Button) findViewById(R.id.registercode);
		registercode.setOnClickListener(this);
		registercode.setEnabled(false);
		// 确定点击事件
		registration_button = (TextView) findViewById(R.id.registration_button);
		registration_button.setOnClickListener(this);
		// 返回按钮
		ImageView back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		// 控制密码是否显示问题
		show_password = (ImageView) findViewById(R.id.show_password);
		show_password.setOnClickListener(this);
	}

	private TextWatcher watcher = new TextWatcher() {
		private CharSequence temp; // 监听前的文本
		// 输入文本之前的状态

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			temp = s;
		}

		// 输入文字中的状态，
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// tip.setText((s.length()) + / + charMaxNum);
		}

		// 输入文字后的状态
		@Override
		public void afterTextChanged(Editable s) {
			/** 得到光标开始和结束位置 ,超过最大数后记录刚超出的数字索引进行控制 */
			if (temp.length() == 11) {
				registercode.setEnabled(true);
				registercode.setBackgroundResource(R.drawable.textview_style_blue_5);
			} else {
				registercode.setEnabled(false);
				registercode.setBackgroundResource(R.drawable.textview_style_gray_5);
			}
		}
	};

	/**
	 * 密码是否显示
	 */
	private void PasswordDisplay() {
		if (show_password_bool == true) {
			show_password_bool = false;
			show_password.setBackgroundResource(R.drawable.password_off_blue);
			// 文本正常显示
			forget_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			Editable etable = forget_pwd.getText();
			Selection.setSelection(etable, etable.length());
		} else {
			show_password_bool = true;
			show_password.setBackgroundResource(R.drawable.password_on_blue);
			// 文本以密码形式显示
			forget_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			// 下面两行代码实现: 输入框光标一直在输入文本后面
			Editable etable = forget_pwd.getText();
			Selection.setSelection(etable, etable.length());
		}

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:// 返回
			finish();
			break;
		case R.id.registercode:// 验证码获取
			Code();
			break;
		case R.id.show_password:// 密码是否显示
			PasswordDisplay();
			break;
		case R.id.registration_button:// 找回密码
			Modify();
			break;
		default:
			break;
		}
	}

	/**
	 * 获取验证码
	 */
	private void Code() {
		final LoadingDialog loading = new LoadingDialog(this);
		// 模块 0=注册, 1=修改密码, 2= 找回密码
		Api.code(forget_mon.getText().toString(), 1, new OnResponse<String>() {
			@Override
			public void onResponse(String o) {
				loading.dismiss();
				timer.start();
			}

			@Override
			public void onErrorResponse(String error) {
				loading.dismiss();
				ToastUtils.show(error);
			}
		});
	}

	/**
	 * 找回密码接口
	 */
	private void Modify() {
		final LoadingDialog loading = new LoadingDialog(this);
		Api.modify(forget_mon.getText().toString(), forget_code.getText().toString(), forget_pwd.getText().toString(),
				new OnResponse<String>() {
					@Override
					public void onResponse(String o) {
						ToastUtils.show(o);
						loading.dismiss();
					}

					@Override
					public void onErrorResponse(String error) {
						loading.dismiss();
						ToastUtils.show(error);
					}
				});
	}
}