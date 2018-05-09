package com.cxw.cxwproject.adapter;

import com.cxw.cxwproject.MyApp;
import com.cxw.cxwproject.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ClassificationAdapter extends BaseAdapter {
	private String lstDate[] = { "汉族", "蒙古族", "回族", "藏族", "维吾尔族", "苗族", "彝族", "壮族" };
	private LayoutInflater listContainer;
	// 定义数组来存放按钮图片
//	private int ItemImage[] = { R.drawable.han01, R.drawable.cxw_icon, R.drawable.cxw_icon, R.drawable.cxw_icon,
//			R.drawable.cxw_icon, R.drawable.han01, R.drawable.cxw_icon, R.drawable.cxw_icon };

	public class ListItemView {
		private ImageView gridview_img;
		private TextView gridview_text;
	}

	public ClassificationAdapter(Context mContext) {
		listContainer = LayoutInflater.from(mContext);
	}

	public int getCount() {
		return 5;
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ListItemView listItemView;
		if (convertView == null) {
			listItemView = new ListItemView();
			convertView = listContainer.inflate(R.layout.adapter_classification_item, parent, false);
			listItemView.gridview_img = (ImageView) convertView.findViewById(R.id.gridview_img);
			listItemView.gridview_text = (TextView) convertView.findViewById(R.id.gridview_text);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		listItemView.gridview_text.setText(lstDate[position]);
		//listItemView.gridview_img.setImageResource(ItemImage[position]);
		return convertView;
	}

}
