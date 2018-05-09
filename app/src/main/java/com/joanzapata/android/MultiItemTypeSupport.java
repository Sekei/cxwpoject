package com.joanzapata.android;

/**
 * 扩展，支持多种item布局
 * 
 * @author Administrator
 *
 */
public interface MultiItemTypeSupport<T> {
	int getLayoutId(int position, T t);

	int getViewTypeCount();

	int getItemViewType(int postion, T t);
}
