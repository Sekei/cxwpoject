package com.cxw.cxwproject.fragment;

import com.cxw.cxwproject.BaseFragment;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.activity.ForgetPasswordActivity;
import com.cxw.cxwproject.bean.UserBean;
import com.cxw.cxwproject.dialog.LoadingDialog;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.tool.SharePreferenceUtil;
import com.cxw.cxwproject.widget.ToastUtils;

import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 快捷登录
 * 
 * @author Administrator
 *
 */
public class LoginFragment_Pager2 extends BaseFragment implements OnClickListener {
	private EditText loginName, loginPwd;
	private TextView circularButton1;
	private ImageView show_password;
	private boolean show_password_bool = true;

	protected int getLayoutId() {
		return R.layout.fragment_page2;
	};

	@Override
	protected void initView(View view) {
		super.initView(view);
		// 用户名/密码
		loginName = (EditText) view.findViewById(R.id.loginname);
		loginPwd = (EditText) view.findViewById(R.id.loginpwd);
		TextView forget_btn = (TextView) view.findViewById(R.id.forget_btn);
		forget_btn.setOnClickListener(this);
		// 控制密码是否显示问题
		show_password = (ImageView) view.findViewById(R.id.show_password);
		show_password.setOnClickListener(this);
		circularButton1 = (TextView) view.findViewById(R.id.circularButton1);
		circularButton1.setOnClickListener(this);
	}

	/*
	 * circularButton1 = (CircularProgressButton)
	 * view.findViewById(R.id.circularButton1);
	 * circularButton1.setIndeterminateProgressMode(true);
	 * circularButton1.setOnClickListener(new View.OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { if (circularButton1.getProgress()
	 * == 0) { circularButton1.setProgress(50); //
	 * simulateSuccessProgress(circularButton1); } else if
	 * (circularButton1.getProgress() == 100) { circularButton1.setProgress(0);
	 * } else { circularButton1.setProgress(100); } } });
	 * 
	 * private void simulateSuccessProgress(final CircularProgressButton button)
	 * { ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);
	 * widthAnimation.setDuration(1500); widthAnimation.setInterpolator(new
	 * AccelerateDecelerateInterpolator()); widthAnimation.addUpdateListener(new
	 * ValueAnimator.AnimatorUpdateListener() {
	 * 
	 * @Override public void onAnimationUpdate(ValueAnimator animation) {
	 * Integer value = (Integer) animation.getAnimatedValue();
	 * button.setProgress(value); } }); // 添加动画的执行监听
	 * widthAnimation.addListener(new AnimatorListener() { // 动画开始
	 * 
	 * @Override public void onAnimationStart(Animator animation) {
	 * 
	 * }
	 * 
	 * // 动画正在执行
	 * 
	 * @Override public void onAnimationRepeat(Animator animation) {
	 * 
	 * }
	 * 
	 * // 动画结束
	 * 
	 * @Override public void onAnimationEnd(Animator animation) {
	 * circularButton1.setProgress(0); }
	 * 
	 * // 动画取消
	 * 
	 * @Override public void onAnimationCancel(Animator animation) {
	 * 
	 * } }); widthAnimation.start(); }
	 */
	/*
	 * 密码是否显示
	 */
	private void PasswordDisplay() {
		if (show_password_bool == true) {
			show_password_bool = false;
			show_password.setBackgroundResource(R.drawable.password_off_blue);
			// 文本正常显示
			loginPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			Editable etable = loginPwd.getText();
			Selection.setSelection(etable, etable.length());
		} else {
			show_password_bool = true;
			show_password.setBackgroundResource(R.drawable.password_on_blue);
			// 文本以密码形式显示
			loginPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			// 下面两行代码实现: 输入框光标一直在输入文本后面
			Editable etable = loginPwd.getText();
			Selection.setSelection(etable, etable.length());
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.forget_btn:// 忘记密码
			startActivity(new Intent(getActivity(), ForgetPasswordActivity.class));
			break;
		case R.id.show_password:// 密码开关
			PasswordDisplay();
			break;
		case R.id.circularButton1:// 密码登录接口
			Login();
			break;
		default:
			break;
		}

	}

	/*
	 * 密码登录界面
	 */
	private void Login() {
		final LoadingDialog mDialog = new LoadingDialog(getActivity());
		Api.login(loginName.getText().toString(), loginPwd.getText().toString(), new OnResponse<UserBean>() {
			@Override
			public void onResponse(UserBean o) {
				mDialog.dismiss();
				new SharePreferenceUtil().setMember(loginName.getText().toString());
				new SharePreferenceUtil().setPwd(loginPwd.getText().toString());
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
