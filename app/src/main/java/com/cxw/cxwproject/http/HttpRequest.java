package com.cxw.cxwproject.http;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cxw.cxwproject.MyApp;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.tool.SharePreferenceUtil;

public class HttpRequest {

	public static void cancel(Object tag) {
		getRequestQueue().cancelAll(tag);
	}

	public static void clearCache() {
		getRequestQueue().getCache().clear();
	}

	public static void clearCache(String key) {
		getRequestQueue().getCache().remove(key);
	}

	private static RequestQueue queue;// 这里不需要写成线程安全的

	public static RequestQueue getRequestQueue() {
		if (queue == null) {
			CookieManager cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
			CookieManager.setDefault(cookieManager);
			queue = Volley.newRequestQueue(MyApp.getApp());
		}
		return queue;
	}

	public static <T> void request(int method, final String url, final Map<String, Object> map, final OnResponse<T> l) {

		BeanRequest<T> request = new BeanRequest<T>(method, url, l) {
			@Override
			protected Map<String, Object> getMultipartParams() {
				return map;
			}

			// Volley请求类提供了一个 getHeaders（）的方法，重载这个方法可以自定义HTTP 的头信息。（也可不实现）
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Content-Type", "application/x-www-form-urlencoded");
				headers.put("Cookie", new SharePreferenceUtil().getCookie());// 向请求头部添加Cookie
				return headers;
			}
			@Override
			public String getBodyContentType() {
				return String.format("application/x-www-form-urlencoded; charset=%s", "utf-8");
			}
		};
		request.setTag(l.getTag());
		// request.setShouldCache(true);//默认支持缓存
		// 增加强制刷新数据的处理
		if (l.getCacheTime() <= 0) {
			clearCache(request.getCacheKey());
		}
		// 默认请求超时时间、失败后尝试请求次数
		request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, 1f));
		getRequestQueue().add(request);
	}

	public static <T> void post(String url, Map<String, Object> map, OnResponse<T> l) {
		request(Request.Method.POST, url, map, l);
	}

}
