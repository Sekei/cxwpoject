package com.cxw.cxwproject.adapter;

import java.util.List;

import com.cxw.cxwproject.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

public class UitimateAdapter extends CustomAdapter {
	private String[] list=null;
	private LayoutInflater inflater;

	public UitimateAdapter(Context context, String[] data) {
		inflater = LayoutInflater.from(context);
		this.list = data;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_style_uitimate, parent);
			vh.view = (CheckBox) convertView.findViewById(R.id.item_filter_menu);
			//vh.view.setOnCheckedChangeListener(this);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.view.setText(list[position].toString());
//		try {
//			vh.view.setText(list.get(position).getName());
//			if (mSelected == position) {
//				vh.view.setChecked(true);
//				vh.view.setBackgroundResource(R.drawable.shoppingguide_bg);
//				vh.view.setTextColor(con.getResources().getColor(R.color.rose_red));
//			} else {
//				vh.view.setChecked(false);
//				vh.view.setBackgroundResource(R.drawable.shoppingguide_view_bg);
//				vh.view.setTextColor(con.getResources().getColor(R.color.shoppingguide_black));
//			}
//		} catch (NullPointerException e) {
//			e.printStackTrace();
//		}
		return convertView;
	}

	public class ViewHolder {
		public CheckBox view;
	}
}
