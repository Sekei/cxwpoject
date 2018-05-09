package com.cxw.cxwproject;

import com.alibaba.fastjson.JSON;
import com.cxw.cxwproject.http.HttpRequest;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

	public void startActivity(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
	}
	
	public void startActivity(Class<?> cls, Object... objects) {
		Intent intent = new Intent(this, cls);
		for (int i = 0; i < objects.length; i++) {
			intent.putExtra("data_" + i, JSON.toJSONString(objects[i]));
		}
		startActivity(intent);
	}
	
	
	public void startActivityForResult(Class<?> cls,int requestCode) {
		Intent intent = new Intent(this, cls);
		startActivityForResult(intent, requestCode);
	}
	
	public void startActivityForResult(Class<?> cls,int requestCode, Object... objects) {
		Intent intent = new Intent(this, cls);
		for (int i = 0; i < objects.length; i++) {
			intent.putExtra("data_" + i, JSON.toJSONString(objects[i]));
		}
		startActivityForResult(intent, requestCode);
	}
	
	

	public Object[] getIntentObjects(Class<?>... cls) {
		Object[] objs = new Object[cls.length];
		Intent intent = getIntent();
		for (int i = 0; i < objs.length; i++) {
			objs[i] = JSON.parseObject(intent.getStringExtra("data_" + i), cls[i]);
		}
		return objs;
	}

	public <T> T getIntentObject(Class<T> cls) {
		return getIntentObject(cls,0);
	}
	public <T> T getIntentObject(Class<T> cls,int positon) {
		String st = getIntent().getStringExtra("data_"+positon);
		if (st == null) {
			return null;
		}
		return JSON.parseObject(st, cls);
	}

	// 这个方法是用来取消网络请求时用的
	public Object getTAG() {
		return this;
	}

	@Override
	protected void onDestroy() {
		HttpRequest.cancel(getTAG());// 取消网络请求
		super.onDestroy();
	}
}
