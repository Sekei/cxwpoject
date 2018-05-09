package com.cxw.cxwproject.adapter;

import com.cxw.cxwproject.R;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
	// 设置组视图的显示文字
	private String[] generalsTypes;
	// 子视图显示文字
	private String[][] generals;
	private Context mContext;
	private int groupPosion = -1;

	public ExpandableListAdapter(Context mContext, String[] generalsTypes, String[][] generals) {
		this.mContext = mContext;
		this.generalsTypes = generalsTypes;
		this.generals = generals;
	}

	// 获取当前点击的Group并刷新
	public void getGroupPosion(int groupPosion) {
		this.groupPosion = groupPosion;
	}

	@Override
	public int getGroupCount() {
		return generalsTypes.length;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return generalsTypes[groupPosition].toString();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return generals[groupPosition].length;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return generals[groupPosition][childPosition].toString();
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.expandable_groupview, null);
		}
		ImageView mGroupImage = (ImageView) convertView.findViewById(R.id.mgroupimage);
		if (groupPosion == groupPosition) {
			if (isExpanded) {
				setRotate(mGroupImage, 0, 90);
			} else {
				setRotate(mGroupImage, 90, 0);
			}
		}
		TextView groupview_str = (TextView) convertView.findViewById(R.id.groupview_str);
		groupview_str.setText((groupPosition + 1) + "." + getGroup(groupPosition));
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.expandable_childview, null);
		}
		System.out.println(getChild(groupPosition, childPosition));
		TextView childview_str = (TextView) convertView.findViewById(R.id.childview_str);
		childview_str.setText(getChild(groupPosition, childPosition).toString());
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	// 旋转效果
	private void setRotate(ImageView mGroupImage, int fromDegrees, int toDegrees) {
		Animation rotateAnimation = new RotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setFillAfter(true);
		rotateAnimation.setDuration(200);
		rotateAnimation.setRepeatCount(0);
		rotateAnimation.setFillAfter(true);// 动画执行完后是否停留在执行完的状态
		rotateAnimation.setInterpolator(new LinearInterpolator());
		mGroupImage.startAnimation(rotateAnimation);
	}

}
