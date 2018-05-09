package com.cxw.cxwproject.util;

import com.cxw.cxwproject.adapter.CustomAdapter;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.RelativeLayout;

public class CustomListView extends RelativeLayout {
	private String TAG = CustomListView.class.getSimpleName();
	private CustomAdapter myCustomAdapter;
	private static boolean addChildType;
	private int dividerHeight = 0;
	private int dividerWidth = 0;

	private SparseArray<Point> mLocation;

	private final Handler handler = new Handler(Looper.getMainLooper()) {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			try {
				if (msg.getData().containsKey("getRefreshThreadHandler")) {
					CustomListView.setAddChildType(false);
					CustomListView.this.myCustomAdapter.notifyCustomListView(CustomListView.this);
				}
			} catch (Exception e) {
				Log.w(CustomListView.this.TAG, e);
			}
		}
	};

	public CustomListView(Context context) {
		this(context, null);
	}

	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mLocation = new SparseArray<Point>();
	}

	protected void onLayout(boolean arg0, int argLeft, int argTop, int argRight, int argBottom) {
		Log.e(this.TAG, "---------L:" + argLeft + " T:" + argTop + " R:" + argRight + " B:" + argBottom);
		for (int i = 0; i < mLocation.size(); i++) {
			View view = getChildAt(i);
			Point point = mLocation.get(i);
			view.layout(point.x, point.y, (point.x + view.getMeasuredWidth()), (point.y + view.getMeasuredHeight()));
		}
		if (isAddChildType())
			new Thread(new RefreshCustomThread()).start();
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 得到控件宽度
		int width = MeasureSpec.getSize(widthMeasureSpec);
		// 去掉控件的左右填充
		width = width - getPaddingRight() - getPaddingLeft();
		Log.e(this.getClass().getSimpleName(), "-------------> width =" + width);
		// 记录宽度
		int tempX = 0;
		// 记录高度
		int tempY = 0;
		// 记录行数
		int row = 0;
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			// 测量控件高度
			child.measure(0, 0);
			int w = child.getMeasuredWidth();
			int h = child.getMeasuredHeight();
			if (tempX == 0)
				tempX = w;
			else
				tempX = tempX + w + getDividerWidth();
			// 计算是否换行
			if (tempX <= width) {
				if (row == 0)
					tempY = h;
			} else {
				row++;
				tempY = tempY + getDividerHeight() + h;
				tempX = w;
			}
			// 记录控件位置
			mLocation.put(i, new Point((tempX - w), (tempY - h)));
		}

		Log.e(this.getClass().getSimpleName(), "---------->tempx = " + tempX + " tempy=" + tempY);
		setMeasuredDimension(widthMeasureSpec, tempY);
	}

	static final boolean isAddChildType() {
		return addChildType;
	}

	public static void setAddChildType(boolean addChildType) {
		addChildType = addChildType;
	}

	final int getDividerHeight() {
		return this.dividerHeight;
	}

	public void setDividerHeight(int dividerHeight) {
		this.dividerHeight = dividerHeight;
	}

	final int getDividerWidth() {
		return this.dividerWidth;
	}

	public void setDividerWidth(int dividerWidth) {
		this.dividerWidth = dividerWidth;
	}

	public void setAdapter(CustomAdapter adapter) {
		this.myCustomAdapter = adapter;
		setAddChildType(true);
		adapter.notifyCustomListView(this);
	}
	
	public void clearLocation(){
		mLocation.clear();
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.myCustomAdapter.setOnItemClickListener(listener);
	}

	public void setOnItemLongClickListener(OnItemLongClickListener listener) {
		this.myCustomAdapter.setOnItemLongClickListener(listener);
	}

	private final void sendMsgHanlder(Handler handler, Bundle data) {
		Message msg = handler.obtainMessage();
		msg.setData(data);
		handler.sendMessage(msg);
	}

	private final class RefreshCustomThread implements Runnable {
		private RefreshCustomThread() {
		}

		public void run() {
			Bundle b = new Bundle();
			try {
				Thread.sleep(50L);
			} catch (Exception localException) {
			} finally {
				b.putBoolean("getRefreshThreadHandler", true);
				CustomListView.this.sendMsgHanlder(CustomListView.this.handler, b);
			}
		}
	}
}