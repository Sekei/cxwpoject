package com.cxw.cxwproject.activity;

import java.io.File;
import java.util.Properties;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.UserBean;
import com.cxw.cxwproject.dialog.CustomDialog;
import com.cxw.cxwproject.dialog.CustomDialog.Builder;
import com.cxw.cxwproject.tool.AppConfigTool;
import com.cxw.cxwproject.tool.DataCleanManager;
import com.cxw.cxwproject.tool.MethodsCompat;
import com.cxw.cxwproject.tool.SharePreferenceUtil;
import com.cxw.cxwproject.util.FileUtil;
import com.cxw.cxwproject.util.UpdateManager;
import com.cxw.cxwproject.widget.ToastUtils;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SetupActivity extends ActionBarActivity implements OnClickListener {
	TextView setup_cache;
	// ------****** 缓存相关****----------
	private final int CLEAN_SUC = 1001;
	private final int CLEAN_FAIL = 1002;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_setup;
	}

	@Override
	public int getActionBarType() {
		return ACTIONBAR_NOT_SHOW;
	}

	@Override
	protected void initView() {
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("设置");
		ImageView back = (ImageView) findViewById(R.id.back); // 返回
		back.setOnClickListener(this);
		// 退出
		TextView submit = (TextView) findViewById(R.id.submit);
		submit.setOnClickListener(this);
		if (new SharePreferenceUtil().getCookie() != null) {
			submit.setVisibility(View.VISIBLE);
		}
		// 关于我们
		TextView aboutus_btn = (TextView) findViewById(R.id.aboutus_btn);
		aboutus_btn.setOnClickListener(this);
		// 消息通知
		TextView news_btn = (TextView) findViewById(R.id.news_btn);
		news_btn.setOnClickListener(this);
		// 检查版本更新
		TextView setup_update = (TextView) findViewById(R.id.setup_update);
		setup_update.setOnClickListener(this);
		// 缓存大小
		setup_cache = (TextView) findViewById(R.id.setup_cache);
		caculateCacheSize();// 计算缓存大小
		// 清除缓存
		LinearLayout setup_empty = (LinearLayout) findViewById(R.id.setup_empty);
		setup_empty.setOnClickListener(this);
	}

	/**
	 * 系统退出登录提示框
	 */
	private void AlertDialog() {
		CustomDialog.Builder builder = new Builder(SetupActivity.this);
		builder.setTitle("温馨提示");
		builder.setMessage("此操作将退出当前会员账号？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				// 清空数据及Cookie
				new SharePreferenceUtil().setCookie("");
				// 清空用户登录数据
				new SharePreferenceUtil().setMember("");
				UserBean.save(null);// 13166383205
				// 页面跳转
				startActivity(LoginActivity.class, 0);
				dialog.dismiss();
				finish();
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

	/**
	 * 计算缓存的大小
	 */
	private void caculateCacheSize() {
		long fileSize = 0;
		String cacheSize = "0KB";
		File filesDir = this.getFilesDir();
		File cacheDir = this.getCacheDir();

		fileSize += FileUtil.getDirSize(filesDir);
		fileSize += FileUtil.getDirSize(cacheDir);
		// 2.2版本才有将应用缓存转移到sd卡的功能
		if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
			File externalCacheDir = MethodsCompat.getExternalCacheDir(this);
			fileSize += FileUtil.getDirSize(externalCacheDir);
			// fileSize += FileUtil.getDirSize(
			// new File(org.kymjs.kjframe.utils.FileUtils.getSDCardPath() +
			// File.separator + "KJLibrary/cache"));
		}
		if (fileSize > 0) {
			cacheSize = FileUtil.formatFileSize(fileSize);
		}
		setup_cache.setText(cacheSize);
	}

	public static boolean isMethodsCompat(int VersionCode) {
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		return currentVersion >= VersionCode;
	}

	/**
	 * 清除app缓存
	 */
	public void myclearaAppCache() {
		// DataCleanManager.cleanDatabases(this);
		// 清除数据缓存
		DataCleanManager.cleanInternalCache(this);
		// 2.2版本才有将应用缓存转移到sd卡的功能
		// if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
		// DataCleanManager.cleanCustomCache(MethodsCompat.getExternalCacheDir(this));
		// }
		// 清除编辑器保存的临时内容
		Properties props = getProperties();
		for (Object key : props.keySet()) {
			String _key = key.toString();
			if (_key.startsWith("temp"))
				removeProperty(_key);
		}
		// Core.getKJBitmap().cleanCache();
	}

	/**
	 * 清除保存的缓存
	 */
	public Properties getProperties() {
		return AppConfigTool.getAppConfig(this).get();
	}

	public void removeProperty(String... key) {
		AppConfigTool.getAppConfig(this).remove(key);
	}

	/**
	 * 清除app缓存
	 *
	 * @param
	 */
	public void clearAppCache() {

		new Thread() {
			@Override
			public void run() {
				Message msg = new Message();
				try {
					myclearaAppCache();
					msg.what = CLEAN_SUC;
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = CLEAN_FAIL;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CLEAN_FAIL:
				ToastUtils.show("清除失败");
				break;
			case CLEAN_SUC:
				ToastUtils.show("清除成功");
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.submit:// 退出
			AlertDialog();
			break;
		case R.id.aboutus_btn:// 关于我们
			startActivity(AboutusActivity.class);
			break;
		case R.id.news_btn:// 消息
			startActivity(NewsSetupActivity.class);
			break;
		case R.id.setup_update:// 检查版本更新
			new UpdateManager(this).checkLatestVersion();
			break;
		case R.id.setup_empty:// 清理缓存数据
			clearAppCache();
			break;
		default:
			break;
		}

	}
}
