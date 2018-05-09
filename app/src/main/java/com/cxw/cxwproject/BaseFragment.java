package com.cxw.cxwproject;

import com.alibaba.fastjson.JSON;
import com.cxw.cxwproject.http.HttpRequest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
	protected View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(getLayoutId(), container, false);
			initView(view);
		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		return view;
	}

	protected void initView(View view) {

	}

	protected abstract int getLayoutId();

	public void startActivity(Class<?> cls) {
		Intent intent = new Intent(getActivity(), cls);
		startActivity(intent);
	}

	public void startActivity(Class<?> cls, Object... objects) {
		Intent intent = new Intent(getActivity(), cls);
		for (int i = 0; i < objects.length; i++) {
			intent.putExtra("data_" + i, JSON.toJSONString(objects[i]));
		}
		startActivity(intent);
	}

	public void startActivityForResult(Class<?> cls, int requestCode) {
		Intent intent = new Intent(getActivity(), cls);
		startActivityForResult(intent, requestCode);
	}

	public void startActivityForResult(Class<?> cls, int requestCode, Object... objects) {
		Intent intent = new Intent(getActivity(), cls);
		for (int i = 0; i < objects.length; i++) {
			intent.putExtra("data_" + i, JSON.toJSONString(objects[i]));
		}
		startActivityForResult(intent, requestCode);
	}

	// 这个方法是用来取消网络请求时用的
	public Object getTAG() {
		return this;
	}

	@Override
	public void onDestroyView() {
		HttpRequest.cancel(getTAG());// 取消网络请求
		super.onDestroyView();
	}

}
