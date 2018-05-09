package com.cxw.cxwproject.fragment;

import com.cxw.cxwproject.AppConfig;
import com.cxw.cxwproject.BaseFragment;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.widget.EmptyLayout;
import com.cxw.cxwproject.widget.InnerScrollView;
import com.cxw.cxwproject.widget.NoScrollView;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class National_Page3 extends BaseFragment implements OnClickListener {
	String url;// url访问链接
	WebView mWebView;
	EmptyLayout empty_view;// loading页面

	@Override
	protected int getLayoutId() {
		return R.layout.national_page3;
	}

	@Override
	protected void initView(View view) {
		super.initView(view);
		// 通过getactivity获取父控件ID
		NoScrollView noScrollView = (NoScrollView) getActivity().findViewById(R.id.msv);
		// 子类scrollview控制父类滚动事件
		InnerScrollView innerScrollView = (InnerScrollView) view.findViewById(R.id.scroll_page3);
		innerScrollView.parentScrollView = noScrollView;
		empty_view = (EmptyLayout) view.findViewById(R.id.empty_view);
		empty_view.setOnClickListener(this);
		// 绑定数据
		mWebView = (WebView) view.findViewById(R.id.web_view);
		// 设置不调用外部浏览器
		mWebView.setWebViewClient(new WebViewClient());
		mWebView.setWebChromeClient(new WebChromeClient());
		// 是否可以缩放
		WebSettings settings = mWebView.getSettings();
		settings.setSupportZoom(false);
		// 是否显示缩放控制
		settings.setBuiltInZoomControls(false);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		settings.setDomStorageEnabled(true);
		url = AppConfig.ICON + getArguments().getString("culture_url");
		if (url != null) {
			mWebView.loadUrl(url);
		}
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
			super.onProgressChanged(view, newProgress);
			if (newProgress < 100) {
				empty_view.setVisibility(View.VISIBLE);
			} else {
				empty_view.setVisibility(View.GONE);
			}
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.empty_view:
			mWebView.loadUrl(url);
			break;
		}
	}
}
