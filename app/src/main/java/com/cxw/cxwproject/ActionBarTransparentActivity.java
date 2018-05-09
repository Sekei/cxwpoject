package com.cxw.cxwproject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.cxw.cxwproject.transparent.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.MobclickAgent.EScenarioType;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * 透明顶部专用
 * 
 * @author Administrator
 *
 */
public abstract class ActionBarTransparentActivity extends BaseActivity {
	protected Context context;
	private TextView mTitle;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		context = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		if ((getActionBarType() & ACTIONBAR_TRANSLATE) == ACTIONBAR_TRANSLATE) {
			tintManager.setStatusBarTintEnabled(false);// 通知栏透明
			//MIUISetStatusBarLightMode(getWindow(), true);// 电池、字体、图标变色
		} else {
			tintManager.setStatusBarTintEnabled(true);
		}
		tintManager.setStatusBarTintResource(setTranslucentBar());// 通知栏所需颜色
		initActionBar();
		initView();
		// 友盟统计相关======================友盟统计==========================
		// 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
		MobclickAgent.openActivityDurationTrack(false);
		MobclickAgent.setScenarioType(this, EScenarioType.E_UM_NORMAL);
	}

	/**
	 * 设置状态栏字体图标为深色，需要MIUIV6以上
	 * 
	 * @param window
	 *            需要设置的窗口
	 * @param dark
	 *            是否把状态栏字体及图标颜色设置为深色
	 * @return boolean 成功执行返回true
	 *
	 */
	public boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
		boolean result = false;
		if (window != null) {
			Class<? extends Window> clazz = window.getClass();
			try {
				int darkModeFlag = 0;
				Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
				Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
				darkModeFlag = field.getInt(layoutParams);
				Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
				if (dark) {
					extraFlagField.invoke(window, darkModeFlag, darkModeFlag);// 状态栏透明且黑色字体
				} else {
					extraFlagField.invoke(window, 0, darkModeFlag);// 清除黑色字体
				}
				result = true;
			} catch (Exception e) {

			}
		}
		return result;
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	/**
	 * 设置状体栏颜色
	 * 
	 * @return
	 */
	protected int setTranslucentBar() {
		return R.color.uli_bg;
	}

	/**
	 * 初始化控件
	 */
	protected void initView() {

	}

	protected abstract int getLayoutId();

	public static final int ACTIONBAR_DARK = 0;
	public static final int ACTIONBAR_WHITE = 1;
	public static final int ACTIONBAR_TRANSLATE = 2;
	public static final int ACTIONBAR_NOT_SHOW = 4;

	public int getActionBarType() {
		return ACTIONBAR_WHITE;
	}

	private void initActionBar() {
		setContentView(getLayoutId());
	}

	@Override
	public void setTitle(CharSequence title) {
		super.setTitle(title);
		if (mTitle != null) {
			mTitle.setText(title);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getTitle().toString());
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getTitle().toString());
		MobclickAgent.onPause(this);
	}

}
