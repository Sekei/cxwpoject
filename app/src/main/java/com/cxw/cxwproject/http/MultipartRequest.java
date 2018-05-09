package com.cxw.cxwproject.http;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;

public abstract class MultipartRequest<T> extends Request<T> {

	private MultipartEntity entity = new MultipartEntity();


	public MultipartRequest(String url, 
			Response.ErrorListener errorListener) {
		super(Method.POST, url, errorListener);
	}

	public MultipartRequest(int method, String url,
			
			Response.ErrorListener errorListener) {
		super(method, url, errorListener);

	}

	protected Map<String, Object> getMultipartParams() {
		return null;
	}

	@Override
	public String getBodyContentType() {
		if (hasFile) {
			return entity.getContentType().getValue();
		}
		return super.getBodyContentType();
	}

	/**
	 * Map<String, String> params = getParams();
        if (params != null && params.size() > 0) {
            return encodeParameters(params, getParamsEncoding());
        }
	 */
	
	private boolean hasFile = false;
	@Override
	public byte[] getBody() throws AuthFailureError {

		//添加另一种编写方式
		StringBuilder encodedParams = new StringBuilder();
		
		try {
			Map<String, Object> mParams = getMultipartParams();
			
			
			if (mParams != null && mParams.size() > 0) {
				for (Map.Entry<String, Object> entry : mParams.entrySet()) {
					Object value = entry.getValue();
						if (value instanceof File) {
							entity.addPart(entry.getKey(), new FileBody(
									(File) value));
							hasFile = true;
						} else {
							encodedParams.append(URLEncoder.encode(entry.getKey(), getParamsEncoding()));
							encodedParams.append('=');
							encodedParams.append(URLEncoder.encode(value+"", getParamsEncoding()));
							encodedParams.append('&');
							entity.addPart(entry.getKey(),
									new StringBody(value + "",
											Charset.forName(getParamsEncoding())));
						}
						
				}
			}
		} catch (UnsupportedEncodingException e) {
			VolleyLog.e("UnsupportedEncodingException");
		}

		if (!hasFile) {
			try {
				return encodedParams.toString().getBytes(getParamsEncoding());
			} catch (UnsupportedEncodingException e) {
				return null;
			}
		}
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			entity.writeTo(bos);
		} catch (IOException e) {
			VolleyLog.e("IOException writing to ByteArrayOutputStream");
		}
		return bos.toByteArray();
	}

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.volley.Request#getHeaders()
	 */
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = super.getHeaders();

		if (headers == null || headers.equals(Collections.emptyMap())) {
			headers = new HashMap<String, String>();
		}

		return headers;
	}

}
