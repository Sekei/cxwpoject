/**
 * Copyright 2013 Joan Zapata
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.joanzapata.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

/**
 * Abstraction class of a BaseAdapter in which you only need to provide the
 * convert() implementation.<br/>
 * Using the provided BaseAdapterHelper, your code is minimalist.
 * 
 * @param <T>
 *            The type of the items in the list.
 */
public abstract class BaseQuickGridViewAdapter<T, H extends BaseAdapterHelper> extends BaseAdapter {

	protected static final String TAG = BaseQuickGridViewAdapter.class.getSimpleName();

	protected final Context context;

	protected final int layoutResId;

	protected List<T> data;

	protected boolean displayIndeterminateProgress = false;

	/**
	 * Create a QuickAdapter.
	 * 
	 * @param context
	 *            The context.
	 * @param layoutResId
	 *            The layout resource id of each item.
	 */
	public BaseQuickGridViewAdapter(Context context, int layoutResId) {
		this(context, layoutResId, null);
	}

	/**
	 * Same as QuickAdapter#QuickAdapter(Context,int) but with some
	 * initialization data.
	 * 
	 * @param context
	 *            The context.
	 * @param layoutResId
	 *            The layout resource id of each item.
	 * @param data
	 *            A new list is created out of this one to avoid mutable list
	 */
	public BaseQuickGridViewAdapter(Context context, int layoutResId, List<T> data) {
		this.data = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
		this.context = context;
		this.layoutResId = layoutResId;
	}

	/**
	 * 刷新适配器内数据
	 */
	public void notifyDataSetInvalidated(List<T> mdata) {
		this.data = mdata == null ? new ArrayList<T>() : new ArrayList<T>(mdata);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		int extra = displayIndeterminateProgress ? 1 : 0;
		return data.size() + extra;
	}

	@Override
	public T getItem(int position) {
		if (position >= data.size())
			return null;
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return position >= data.size() ? 1 : 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (getItemViewType(position) == 0) {
			final H helper = getAdapterHelper(position, convertView, parent);
			T item = getItem(position);
			convert(helper, item);
			helper.setAssociatedObject(item);
			return helper.getView();
		}

		return createIndeterminateProgressView(convertView, parent);
	}

	public static interface OnLastViewCreate {
		View createLastView(View convertView, ViewGroup parent);
	}

	private OnLastViewCreate mListener;

	public void setCreateLastViewListener(OnLastViewCreate l) {
		mListener = l;
	}

	public View createIndeterminateProgressView(View convertView, ViewGroup parent) {
		if (mListener != null) {
			return mListener.createLastView(convertView, parent);
		}
		if (convertView == null) {
			FrameLayout container = new FrameLayout(context);
			container.setForegroundGravity(Gravity.CENTER);
			ProgressBar progress = new ProgressBar(context);
			container.addView(progress);
			convertView = container;
		}
		return convertView;
	}

	@Override
	public boolean isEnabled(int position) {
		return position < data.size();
	}

	public int getDataSize() {
		if (data == null) {
			return 0;
		}
		return data.size();
	}

	public void add(T elem) {
		data.add(elem);
		notifyDataSetChanged();
	}

	public void addAll(List<T> elem) {
		data.addAll(elem);
		notifyDataSetChanged();
	}

	public void set(T oldElem, T newElem) {
		set(data.indexOf(oldElem), newElem);
	}

	public void set(int index, T elem) {
		data.set(index, elem);
		notifyDataSetChanged();
	}

	public void remove(T elem) {
		data.remove(elem);
		notifyDataSetChanged();
	}

	public void remove(int index) {
		data.remove(index);
		notifyDataSetChanged();
	}

	public void replaceAll(List<T> elem) {
		data.clear();
		data.addAll(elem);
		notifyDataSetChanged();
	}

	public boolean contains(T elem) {
		return data.contains(elem);
	}

	/** Clear data list */
	public void clear() {
		data.clear();
		notifyDataSetChanged();
	}

	public void showIndeterminateProgress(boolean display) {
		if (display == displayIndeterminateProgress)
			return;
		displayIndeterminateProgress = display;
		notifyDataSetChanged();
	}

	protected abstract void convert(H helper, T item);

	protected abstract H getAdapterHelper(int position, View convertView, ViewGroup parent);

}
