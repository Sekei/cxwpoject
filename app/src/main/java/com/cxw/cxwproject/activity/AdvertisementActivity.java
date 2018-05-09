package com.cxw.cxwproject.activity;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.AppConfig;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.dialog.SignShareDialog;
import com.cxw.cxwproject.dialog.SignShareDialog.OnItemClick;
import com.cxw.cxwproject.inter.UmengShareInter;
import com.cxw.cxwproject.util.UmengShareDao;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 广告和头条、资讯
 * 
 * @author Administrator
 *
 */
@SuppressLint("SetJavaScriptEnabled")
public class AdvertisementActivity extends ActionBarActivity implements UmengShareInter, OnItemClick, OnClickListener {
	private String uri;
	private SignShareDialog mShareDialog;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_base_web;
	}

	@Override
	public int getActionBarType() {
		return ACTIONBAR_NOT_SHOW;
	}

	private TextView titlestr;
	protected WebView mWebView;
	protected ProgressBar mProgressBar;

	protected void initView() {
		titlestr = (TextView) findViewById(R.id.title);
		mWebView = (WebView) findViewById(R.id.web_view);
		mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
		// 设置不调用外部浏览器
		mWebView.setWebViewClient(new WebViewClient());
		mWebView.setWebChromeClient(new WebChromeClient());
		CookieSyncManager.createInstance(this);
		CookieManager cn = CookieManager.getInstance();
		cn.removeSessionCookie();
		cn.removeAllCookie();
		// 是否可以缩放
		WebSettings settings = mWebView.getSettings();
		settings.setSupportZoom(false);
		// 是否显示缩放控制
		settings.setBuiltInZoomControls(false);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		settings.setJavaScriptEnabled(true);
		settings.setDomStorageEnabled(true);
		uri = getIntent().getStringExtra("url");
		if (uri.startsWith("http://") || uri.startsWith("https://") || uri.startsWith("file:///")
				|| uri.startsWith("content://") || uri.startsWith("assets://") || uri.startsWith("drawable://")) {
		} else {
			uri = AppConfig.ICON + uri;
		}
		if (uri != null) {
			mWebView.loadUrl(uri);
		}
		mShareDialog = new SignShareDialog(this);
		mShareDialog.setOnItemClick(this);// 分享点击事件监听
		UmengShareDao.getUmengShare(this);
		ImageView share_btn = (ImageView) findViewById(R.id.share_btn);
		share_btn.setVisibility(View.VISIBLE);
		share_btn.setOnClickListener(this);
		ImageView back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
	}

	public class WebViewClient extends android.webkit.WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url != null && url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:")) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(intent);
				return true;
			}
			return false;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}

	}

	public class WebChromeClient extends android.webkit.WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress < 100) {
				if (mProgressBar.getVisibility() == View.GONE)
					mProgressBar.setVisibility(View.VISIBLE);
				mProgressBar.setProgress(newProgress);
			} else {
				mProgressBar.setProgress(100);
				AlphaAnimation animation = new AlphaAnimation(1, 0);
				animation.setDuration(1000);
				mProgressBar.startAnimation(animation);
				mProgressBar.setVisibility(View.GONE);
			}
			super.onProgressChanged(view, newProgress);
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
			// setTitle(title);
			titlestr.setText(title);
		}

	}

	@Override
	public void onBackPressed() {
		if (mWebView.canGoBack()) {
			mWebView.goBack();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		UMShareAPI.get(this).release();
	}

	/**
	 * 回调展现失败and成功
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		case R.id.share_btn:
			mShareDialog.show();
			break;
		}
	}

	@Override
	public void OnClick(SHARE_MEDIA media) {
		UmengShareDao.getUMWeb(this, media, uri, titlestr.getText().toString());
	}

	@Override
	public void UmengShareOk() {
		mShareDialog.dismiss();
	}

}
