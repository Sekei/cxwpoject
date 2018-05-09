package com.cxw.cxwproject.fragment;

import com.cxw.cxwproject.BaseFragment;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.UserBean;
import com.cxw.cxwproject.dialog.LoadingDialog;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.tool.SharePreferenceUtil;
import com.cxw.cxwproject.widget.ToastUtils;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 快捷登录
 * 
 * @author Administrator
 *
 */
public class LoginFragment_Pager1 extends BaseFragment implements OnClickListener {
	private EditText loginName, loginPwd;
	private TextView circularButton1;
	private Button registercode;
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

	protected int getLayoutId() {
		return R.layout.fragment_page1;
	};

	@Override
	protected void initView(View view) {
		super.initView(view);
		// 用户名/密码
		loginName = (EditText) view.findViewById(R.id.loginname);
		loginName.addTextChangedListener(watcher);
		loginPwd = (EditText) view.findViewById(R.id.loginpwd);
		// 获取验证码
		registercode = (Button) view.findViewById(R.id.registercode);
		registercode.setOnClickListener(this);
		circularButton1 = (TextView) view.findViewById(R.id.circularButton1);
		circularButton1.setOnClickListener(this);
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
	// circularButton1 = (CircularProgressButton)
	// view.findViewById(R.id.circularButton1);
	// circularButton1.setIndeterminateProgressMode(true);
	// circularButton1.setOnClickListener(new View.OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// if (circularButton1.getProgress() == 0) {
	// circularButton1.setProgress(50);
	// // simulateSuccessProgress(circularButton1);
	// } else if (circularButton1.getProgress() == 100) {
	// circularButton1.setProgress(0);
	// } else {
	// circularButton1.setProgress(100);
	// }
	// }
	// });
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
	// circularButton1.setProgress(0);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.registercode:// 验证码
			Code();
			break;
		case R.id.circularButton1:// 登录
			Login();// 测试volley请求
			break;
		default:
			break;
		}

	}

	private void Code() {
		final LoadingDialog dialog = new LoadingDialog(getActivity());
		// 模块0=注册, 1=修改密码, 2= 找回密码, 3= 快速登录
		Api.code(loginName.getText().toString(), 3, new OnResponse<String>() {
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
	 * 验证码登录
	 */
	private void Login() {
		final LoadingDialog mDialog = new LoadingDialog(getActivity());
		Api.CodeLogin(loginName.getText().toString(), loginPwd.getText().toString(), new OnResponse<UserBean>() {
			@Override
			public void onResponse(UserBean o) {
				mDialog.dismiss();
				new SharePreferenceUtil().setMember(loginName.getText().toString());
				UserBean.save(o);
				// 关闭当前页面-----判断是退出过来的还是其它页面
				getActivity().finish();
			}

			@Override
			public void onErrorResponse(String error) {
				mDialog.dismiss();
				ToastUtils.show(error);
			}
		});
	}
}
