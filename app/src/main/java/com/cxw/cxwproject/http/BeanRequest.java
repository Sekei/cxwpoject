package com.cxw.cxwproject.http;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.cxw.cxwproject.tool.SharePreferenceUtil;
import com.cxw.cxwproject.util.TLog;
import com.cxw.cxwproject.widget.ToastUtils;

public class BeanRequest<T> extends MultipartRequest<T> {

	/**
	 * 
	 * 增加对缓存的处理 重写getCacheKey方法 有缓存的情况下，会返回缓存数据，不新鲜时，会再次从网络请求数据（即会返回两次网络请求）
	 * 缓存过期，或者不存在，只会请求一次网络数据，不会返回两次数据。 数据新鲜的情况下，只会返回一次缓存数据。 调用者需要实现，缓存的时间>0
	 * 即可缓存数据
	 *
	 */
	// 以下是Volley的异常列表：
	// AuthFailureError：如果在做一个HTTP的身份验证，可能会发生这个错误。
	// NetworkError：Socket关闭，服务器宕机，DNS错误都会产生这个错误。
	// NoConnectionError：和NetworkError类似，这个是客户端没有网络连接。
	// ParseError：在使用JsonObjectRequest或JsonArrayRequest时，如果接收到的JSON是畸形，会产生异常。
	// SERVERERROR：服务器的响应的一个错误，最有可能的4xx或5xx HTTP状态代码。
	// TimeoutError：Socket超时，服务器太忙或网络延迟会产生这个异常。默认情况下，Volley的超时时间为2.5秒。如果得到这个错误可以使用
	public static abstract class OnResponse<T> extends TypeReference<T> {

		public static final long CATCHE_TIME = 1000 * 60 * 60 * 24 * 1;

		static String errorToString(VolleyError error) {
			// if (AppConfig.DEBUG_API) {
			// return error.getMessage();
			// }
			// if (error instanceof AuthFailureError) {
			// return "服务器异常";
			// } else if (error instanceof ParseError) {
			// return "服务器异常";
			// } else if (error instanceof ServerError) {
			// return "服务器异常";
			// }
			if (error instanceof NetworkError) {
				return "请检查网络连接";
			} else if (error instanceof NoConnectionError) {
				return "请检查网络连接";
			} else if (error instanceof TimeoutError) {
				return "网络请求超时";
			} else if (error instanceof ParseError) {
				return "数据异常";
			} else {
				return error.getMessage();
			}
		}

		public abstract void onResponse(T o);

		public abstract void onErrorResponse(String error);

		/**
		 * 可以使用setKey 代替使用 对 data:{"area":[{bean},{bean}]} 做出相应的处理
		 * 若为data:[{bean},{bean}] getKey() 需要返回 null ，默认返回值null
		 * 
		 * @return
		 */
		private String getKey() {
			return key;
		}

		private String key = null;

		public void setKey(String key) {
			this.key = key;
		}

		public long getCacheTime() {
			return 0;
		}

		// 建议返回 activity 用于当前Activity 被销毁时取消网络请求
		public Object getTag() {
			return null;
		}
	}

	private final String key;
	private OnResponse<T> mListener;
	private final long cacheTime;

	public BeanRequest(int method, String url, OnResponse<T> listener) {
		super(method, url, null);
		this.key = listener.getKey();
		// setTag(listener.getTag());
		mListener = listener;
		cacheTime = listener.getCacheTime();
	}

	private String cacheKey = null;

	@Override
	public String getCacheKey() {
		if (cacheKey == null) {
			cacheKey = getUrl();
			cacheKey += parseMap(getMultipartParams());
		}
		return cacheKey;
	}

	private static String parseMap(Map<String, Object> map) {
		if (map == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("?");
		for (Map.Entry<String, Object> key : map.entrySet()) {
			TLog.v("main", key.getKey(), key.getValue());
			sb.append(key.getKey()).append("=").append(key.getValue()).append("&");
		}
		return sb.toString();

	}

	@SuppressWarnings("unchecked")
	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));// 还没测试网络请求，可以根据情况修改
			TLog.d("request", jsonString);// 打印数据请求
			System.out.println("==========数据>>>" + jsonString);

			JSONObject json = new JSONObject(jsonString);
			// 统一解析提示代码
			// "status": {
			// "msg": "请求成功",
			// "code": 200
			// }
			JSONObject JsonStatus = json.getJSONObject("status");
			if ("200".equals(JsonStatus.getString("code"))) {
				// 这里统一接纳Cookies---为空时不保存
				if (json.getString("sid").equals("20001")) {
					new SharePreferenceUtil().setCookie(response.headers.get("Set-Cookie"));
				}
				if (mListener.getType() instanceof Class) {
					if (mListener.getType().equals(String.class)) {
						return Response.success((T) JsonStatus.getString("msg"), // 这里JSON解析可能会出问题，我要测试
								CacheHelper.parseCacheHeaders(response, 0));
					} else if (mListener.getType().equals(Number.class)
							|| ((Class<?>) mListener.getType()).getSuperclass().equals(Number.class)) {
						return Response.success(JSON.parseObject(JsonStatus.opt("msg").toString(), mListener), // 这里JSON解析可能会出问题，我要测试
								CacheHelper.parseCacheHeaders(response, 0));
					}

				}
				Object data;
				if (key != null) {
					JSONObject json2 = json.getJSONObject("data");
					data = json2.opt(key);
					if (data == null) {
						data = new JSONArray();
					}
				} else {
					data = json.get("data");
				}
				if (mListener.getType().equals(JSONObject.class) || mListener.getType().equals(JSONArray.class)) {
					return Response.success((T) data, // 这里JSON解析可能会出问题，我要测试
							CacheHelper.parseCacheHeaders(response, cacheTime));
				}
				return Response.success(JSON.parseObject(data.toString(), mListener), // 这里JSON解析可能会出问题，我要测试
						CacheHelper.parseCacheHeaders(response, cacheTime));
			} else {
				return Response.error(new VolleyError(JsonStatus.getString("msg")));// 其实是parseError
			}
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
	}

	private boolean hasRequest = false;

	@Override
	protected void deliverResponse(T response) {
		if (!hasRequest) {
			hasRequest = true;
			mListener.onResponse(response);
			// 这里这么处理是防止数据的不新鲜，重新获取的处理
		}
	}

	@Override
	public void deliverError(VolleyError error) {
		error.printStackTrace();
		if (!hasRequest) {
			mListener.onErrorResponse(OnResponse.errorToString(error));
		}
	}

}
