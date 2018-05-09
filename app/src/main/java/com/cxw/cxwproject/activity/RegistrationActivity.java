package com.cxw.cxwproject.activity;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.UserBean;
import com.cxw.cxwproject.dialog.LoadingDialog;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.tool.SharePreferenceUtil;
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
 * 注册
 * 
 * @author Sekei
 * 
 */
public class RegistrationActivity extends ActionBarActivity implements OnClickListener {
	private EditText registration_mon, registration_pwd, registration_verification_code;
	private Button registercode;
	private boolean show_password_bool = true;
	private TextView registration_button;
	LoadingDialog dialog;
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
	private ImageView show_password;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_registration;
	}

	@Override
	public int getActionBarType() {
		return ACTIONBAR_NOT_SHOW;
	}

	@Override
	protected void initView() {
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("注册");
		// 右侧注册按钮
		TextView right_btn = (TextView) findViewById(R.id.right_btn);
		right_btn.setVisibility(View.VISIBLE);
		right_btn.setText("登录");
		right_btn.setOnClickListener(this);
		registration_mon = (EditText) findViewById(R.id.registration_mon);
		registration_mon.addTextChangedListener(watcher);
		registration_pwd = (EditText) findViewById(R.id.registration_pwd);
		registration_verification_code = (EditText) findViewById(R.id.registration_verification_code);
		// 获取验证码
		registercode = (Button) findViewById(R.id.registercode);
		registercode.setOnClickListener(this);
		registercode.setEnabled(false);
		// 注册点击事件
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
	/*
	 * 特殊按钮控件
	 */
	// 点击事件
	// if (registration_button.getProgress() == 0) {
	// registration_button.setProgress(50);
	// // simulateSuccessProgress(registration_button);
	// }
	// <com.cxw.cxwproject.view.button.CircularProgressButton
	// android:id="@+id/registration_button"
	// android:layout_width="match_parent"
	// android:layout_height="40dp"
	// android:layout_marginLeft="15dp"
	// android:layout_marginRight="15dp"
	// android:layout_marginTop="40dp"
	// android:textColor="@color/cpb_white"
	// android:textSize="15sp"
	// app:cpb_cornerRadius="48dp"
	// app:cpb_textIdle="注 册" />
	// private void simulateSuccessProgress(final CircularProgressButton button)
	// {
	// ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);
	// widthAnimation.setDuration(1500);
	// widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
	// widthAnimation.addUpdateListener(new
	// ValueAnimator.AnimatorUpdateListener() {
	// @Override
	// public void onAnimationUpdate(ValueAnimator animation) {
	// Integer value = (Integer) animation.getAnimatedValue();
	// button.setProgress(value);
	// }
	// });
	// // 添加动画的执行监听
	// widthAnimation.addListener(new AnimatorListener() {
	// // 动画开始
	// @Override
	// public void onAnimationStart(Animator animation) {
	//
	// }
	//
	// // 动画正在执行
	// @Override
	// public void onAnimationRepeat(Animator animation) {
	//
	// }
	//
	// // 动画结束
	// @Override
	// public void onAnimationEnd(Animator animation) {
	// registration_button.setProgress(0);
	// }
	//
	// // 动画取消
	// @Override
	// public void onAnimationCancel(Animator animation) {
	//
	// }
	// });
	// widthAnimation.start();
	// }

	/*
	 * 密码是否显示
	 */
	private void PasswordDisplay() {
		if (show_password_bool == true) {
			show_password_bool = false;
			show_password.setBackgroundResource(R.drawable.password_off_blue);
			// 文本正常显示
			registration_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			Editable etable = registration_pwd.getText();
			Selection.setSelection(etable, etable.length());
		} else {
			show_password_bool = true;
			show_password.setBackgroundResource(R.drawable.password_on_blue);
			// 文本以密码形式显示
			registration_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			// 下面两行代码实现: 输入框光标一直在输入文本后面
			Editable etable = registration_pwd.getText();
			Selection.setSelection(etable, etable.length());
		}

	}

	/*
	 * 点击事件
	 */
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:// 返回
		case R.id.right_btn:// 登录
			finish();
			break;
		case R.id.registercode:// 验证码获取
			Code();
			break;
		case R.id.show_password:// 密码是否显示
			PasswordDisplay();
			break;
		case R.id.registration_button:// 注册按钮
			Register();
			break;
		}
	}

	/**
	 * 获取验证码
	 */
	private void Code() {
		final LoadingDialog dialog = new LoadingDialog(this);
		// 模块 0=注册, 1=修改密码, 2= 找回密码
		Api.code(registration_mon.getText().toString(), 0, new OnResponse<String>() {
			@Override
			public void onResponse(String o) {
				dialog.dismiss();
				timer.start();// 验证码倒计时
			}

			@Override
			public void onErrorResponse(String error) {
				dialog.dismiss();
				ToastUtils.show(error);
			}
		});
	}

	/**
	 * 注册接口
	 */
	private void Register() {
		dialog = new LoadingDialog(this);
		Api.regist(registration_mon.getText().toString(), registration_verification_code.getText().toString(),
				registration_pwd.getText().toString(), new OnResponse<String>() {
					@Override
					public void onResponse(String o) {
						Login();// 继续访问登录接口
					}

					@Override
					public void onErrorResponse(String error) {
						dialog.dismiss();
						ToastUtils.show(error);
					}
				}

		);
	}

	/**
	 * 注册成功后直接访问登录接口
	 */
	private void Login() {
		Api.login(registration_mon.getText().toString(), registration_pwd.getText().toString(),
				new OnResponse<UserBean>() {
					@Override
					public void onResponse(UserBean o) {
						dialog.dismiss();
						// 保存登录数据
						new SharePreferenceUtil().setMember(registration_mon.getText().toString());
						new SharePreferenceUtil().setPwd(registration_pwd.getText().toString());
						UserBean.save(o);
						// 关闭当前页面-----判断是退出过来的还是其它页面
						LoginActivity.instance.finish();
						finish();
					}

					@Override
					public void onErrorResponse(String error) {
						dialog.dismiss();
						ToastUtils.show(error);
					}
				}

		);
	}
}