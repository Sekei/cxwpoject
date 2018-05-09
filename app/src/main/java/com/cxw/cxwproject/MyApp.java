package com.cxw.cxwproject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.cxw.cxwproject.util.TDevice;
import com.igexin.sdk.PushManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;

public class MyApp extends Application {
	private String DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.cxw/log/";
	private String NAME = getCurrentDateString() + ".txt";
	private static MyApp _instance;
	// 获取屏幕分辨率
	private DisplayMetrics dm = new DisplayMetrics();

	@Override
	public void onCreate() {
		super.onCreate();
		_instance = this;
		dm = this.getResources().getDisplayMetrics();
		// 图片jar包初始化
		@SuppressWarnings("deprecation")
		DisplayImageOptions option = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_default)
				.showImageOnFail(R.drawable.ic_default).cacheInMemory(false).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		int memoryCache = (int) (Runtime.getRuntime().maxMemory() / 8);
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.memoryCacheSize(memoryCache).defaultDisplayImageOptions(option).build();
		ImageLoader.getInstance().init(configuration);
		// 个推启动
		getPush();
		// SDK在统计Fragment时，需要关闭Activity自带的页面统计，
		Config.DEBUG = true;// 友盟分享相关保存提醒
		Config.isJumptoAppStore = true;// 防止没有安装对应的应用
		MobclickAgent.setDebugMode(true);
		UMShareAPI.get(this);// U盟分享
		initView();// 实例化key
		// 初始化TDevice
		TDevice.init(this);
		// 抓捕异常情况
		Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
	}

	// 各个平台的配置，建议放在全局Application或者程序入口
	public void initView() {
		// 微信
		PlatformConfig.setWeixin("wx0a8cfd71599ccd8a", "12d897dce86f9839a3afe31d74c046ce");
		// 新浪微博
		PlatformConfig.setSinaWeibo("2965177688", "f5479e7e5aea66e011bf436092427fbd",
				"http://sns.whalecloud.com/sina2/callback");
		// QQ相关
		PlatformConfig.setQQZone("1105786697", "oTUaiiWb8nXpikfG");
	}

	/**
	 * 系统震动
	 */
	public void getVibrator() {
		// 获得一个震动的服务
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		// 等待1秒，震动2秒，等待1秒，震动3秒
		long[] pattern = { 1000, 3000, 1000, 5000 };
		// -1表示不重复, 如果不是-1, 比如改成1, 表示从前面这个long数组的下标为1的元素开始重复.
		vibrator.vibrate(pattern, -1);
	}

	/**
	 * 个推启动
	 * 
	 * @return
	 */
	private void getPush() {
		// SDK初始化，第三方程序启动时，都要进行SDK初始化工作
		Log.d("GetuiSdkDemo", "initializing sdk...");
		PackageManager pkgManager = getPackageManager();
		// 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
		boolean sdCardWritePermission = pkgManager.checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
				getPackageName()) == PackageManager.PERMISSION_GRANTED;
		// read phone state用于获取 imei 设备信息
		boolean phoneSatePermission = pkgManager.checkPermission(Manifest.permission.READ_PHONE_STATE,
				getPackageName()) == PackageManager.PERMISSION_GRANTED;
		if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneSatePermission) {
			requestPermission();
		} else {
			// SDK初始化，第三方程序启动时，都要进行SDK初始化工作
			PushManager.getInstance().initialize(this.getApplicationContext());
		}
	}

	private int REQUEST_PERMISSION = 0;

	private void requestPermission() {
		// ActivityCompat.requestPermissions(Activity,
		// new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE,
		// Manifest.permission.READ_PHONE_STATE },
		// REQUEST_PERMISSION);
	}

	public float getDisplayDensity() {
		return dm.density;
	}

	public int dp2px(int dp) {
		return (int) (dp * getDisplayDensity() + 0.5f);
	}

	public int px2dp(int px) {
		return (int) (px / getDisplayDensity() + 0.5f);
	}

	public int pxToSp(int px) {
		return (int) (px / dm.scaledDensity);
	}

	public int spToPx(int sp) {
		return (int) (sp * dm.scaledDensity);
	}

	/**
	 * 获取 屏幕像素 px
	 * 
	 * @return Integer[高度，宽度]
	 */
	public int[] getDisplayHightAndWightPx() {
		return new int[] { dm.heightPixels, dm.widthPixels };
	}

	public static MyApp getApp() {
		return _instance;
	}

	/**
	 * 捕获错误信息的handler
	 */
	private UncaughtExceptionHandler uncaughtExceptionHandler = new UncaughtExceptionHandler() {

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			String info = null;
			ByteArrayOutputStream baos = null;
			PrintStream printStream = null;
			try {
				baos = new ByteArrayOutputStream();
				printStream = new PrintStream(baos);
				ex.printStackTrace(printStream);
				byte[] data = baos.toByteArray();
				info = new String(data);
				data = null;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (printStream != null) {
						printStream.close();
					}
					if (baos != null) {
						baos.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			writeErrorLog(info);
			System.exit(0); // 杀进程
		}
	};

	/**
	 * 获取当前日期
	 * 
	 * @return
	 */
	private static String getCurrentDateString() {
		String result = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		Date nowDate = new Date();
		result = sdf.format(nowDate);
		return result;
	}

	/**
	 * 向文件中写入错误信息
	 * 
	 * @param info
	 */
	protected void writeErrorLog(String info) {
		File dir = new File(DIR);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, NAME);
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file, true);
			fileOutputStream.write(info.getBytes());
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 上下文全局数据存储
	private String Clientid;
	private boolean Roll;// 民族屋滑动控制

	public String getClientid() {
		return Clientid;
	}

	public void setClientid(String clientid) {
		Clientid = clientid;
	}

	public boolean isRoll() {
		return Roll;
	}

	public void setRoll(boolean roll) {
		Roll = roll;
	}

}
