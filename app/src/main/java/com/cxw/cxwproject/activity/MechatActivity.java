package com.cxw.cxwproject.activity;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;

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

@SuppressLint("SetJavaScriptEnabled")
public class MechatActivity extends ActionBarActivity implements OnClickListener {

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
		// title.setText("客服中心");
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
		String url = "https://static.meiqia.com/dist/standalone.html?eid=36093";
		if (url != null) {
			mWebView.loadUrl(url);
		}
		// 返回
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		}
	}

}
