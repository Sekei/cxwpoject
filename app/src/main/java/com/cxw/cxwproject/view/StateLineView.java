package com.cxw.cxwproject.view;

import com.cxw.cxwproject.MyApp;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.util.TDevice;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class StateLineView extends View {
	private Paint mCirclePaint;// 圆圈画笔
	private Paint mLinePaint;// 线画笔
	private Paint mTextPaint;// 文字画笔
	private String[] data = { "下单", "付款", "接单", "配货", "出库", "收货", "完成" };
	private int pointPosition;// 最新的点的位置
	private String startPositionText;// 出发地
	private String arrivePositionText;// 目的地
	private int selectedCircleColor = Color.RED;
	private int selectedLineColor = Color.RED;
	// private Drawable mTextBackground =
	// getContext().getResources().getDrawable(R.drawable.bg_pop_dialog);//选中的文字背景

	public StateLineView(Context context) {
		this(context, null);
	}

	public StateLineView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public StateLineView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StateLine);

		for (int i = 0; i < a.getIndexCount(); i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.StateLine_selectedCircleColor:
				selectedCircleColor = a.getColor(attr, Color.RED);
				break;
			case R.styleable.StateLine_selectedLineColor:
				selectedLineColor = a.getColor(attr, Color.RED);
				break;
			}
		}
		a.recycle();
	}

	private void init() {
		// 圆圈画笔
		mCirclePaint = new Paint();
		mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mCirclePaint.setAntiAlias(true);
		// 线画笔
		mLinePaint = new Paint();
		mLinePaint.setAntiAlias(true);
		mLinePaint.setStrokeWidth(6);
		// 文本画笔
		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);

		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int width;
		int height;
		if (MeasureSpec.EXACTLY == widthMode) {
			width = widthSize;
		} else {
			width = dpToPx(200);
			if (MeasureSpec.AT_MOST == widthMode) {
				width = Math.min(width, widthSize);
			}
		}
		if (MeasureSpec.EXACTLY == heightMode) {
			height = heightSize;
		} else {
			height = dpToPx(45);
			if (MeasureSpec.AT_MOST == heightMode) {
				height = Math.min(height, heightSize);
			}
		}
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int dataSize = data.length;
		int circleWidth =getWidth()/dataSize;
		int width =(getWidth()+(circleWidth-90)) / dataSize;
		// 绘制上部文字
		drawTopText(canvas, dataSize, width);
		canvas.save();
		// 把画布向右移动 "已发货"的文本宽度 一半的距离(让第一个圆圈正对着该文本的中间)
		// 同时向下移动30px,即在上部文字的下方30px处画圆圈和线
		canvas.translate(getTextWidth(data[0]) / 2, 30);
		// 绘制圆圈和线
		for (int i = 0; i <dataSize; i++) {
			if (i < pointPosition) {
				mCirclePaint.setColor(selectedCircleColor);
				mLinePaint.setColor(selectedLineColor);
			} else if (i == pointPosition) {
				mCirclePaint.setColor(selectedCircleColor);
				mLinePaint.setColor(Color.GRAY);
			} else {
				mCirclePaint.setColor(Color.GRAY);
				mLinePaint.setColor(Color.GRAY);
			}
			canvas.drawCircle(15 + getPaddingLeft() + i * width, 65 + getPaddingTop(), 15, mCirclePaint);
			// 最后一个点之后不再画线
			if (i != dataSize - 1) {
				canvas.drawLine(45 + getPaddingLeft() + i * width, 65 + getPaddingTop(),
						getPaddingLeft() + (i + 1) * width - 15, 65 + getPaddingTop(), mLinePaint);
			}
		}
		// 重置画布状态(恢复到上次save时的状态,即没有translate时的状态)
		canvas.restore();
		canvas.save();
		// // 再向下平移55px,即在圆圈和线的下方55px处绘制底部文字
		// canvas.translate(0, 55);
		// // 绘制底部文字
		// drawBottomText(canvas, data.length, width);
		// // 再次把画布的状态重置
		// canvas.restore();
	}
	/**
	 * 绘制上部文字
	 */
	private void drawTopText(Canvas canvas, int length, int width) {
		for (int i = 0; i < length; i++) {
			if (i == pointPosition) {//选中后字体可以放大
				mTextPaint.setColor(selectedCircleColor);
				//这里字体进行转换
				mTextPaint.setTextSize(30);
				// 下面两行添加文字背景
				// mTextBackground.setBounds(getTextBound(i, width, data[i]));
				// mTextBackground.draw(canvas);
			} else {
				mTextPaint.setColor(Color.GRAY);
				mTextPaint.setTextSize(30);
			}
			canvas.drawText(data[i], 15 + getPaddingLeft() + i * width, getPaddingTop() + 55, mTextPaint);
		}
	}

	/**
	 * 绘制底部文字
	 */
	private void drawBottomText(Canvas canvas, int length, int width) {
		mTextPaint.setColor(Color.BLACK);
		if (!TextUtils.isEmpty(startPositionText)) {
			canvas.drawText(startPositionText, 5 + getPaddingLeft(), 5 + getPaddingTop(), mTextPaint);
		}
		if (!TextUtils.isEmpty(arrivePositionText)) {
			canvas.drawText(arrivePositionText, 5 + getPaddingLeft() + width * (length - 1), 5 + getPaddingTop(),
					mTextPaint);
		}
	}

	/**
	 * 设置最新点的位置(从0开始) 比如 pointPosition==2,则前3个点和2个线都是选中颜色,其他都是未选中颜色
	 */
	public void setNewestPointPosition(int pointPosition) {
		if (data.length < pointPosition) {
			pointPosition = data.length - 1;
		}
		this.pointPosition = pointPosition;
		invalidate();
	}

	/**
	 * 获取最新点的位置(从0开始)
	 */
	public int getNewestPointPosition() {
		return pointPosition;
	}

	/**
	 * 设置出发地
	 */
	public void setStartPositionText(String startPositionText) {
		this.startPositionText = startPositionText;
		invalidate();
	}

	/**
	 * 获取出发地
	 */
	public String getStartPositionText() {
		return startPositionText;
	}

	/**
	 * 设置目的地
	 */
	public void setArrivePositionText(String arrivePositionText) {
		this.arrivePositionText = arrivePositionText;
		invalidate();
	}

	/**
	 * 获取目的地
	 */
	public String getArrivePositionText() {
		return arrivePositionText;
	}

	/**
	 * 设置选中的圆圈颜色
	 */
	public void setSelectedCircleColor(int color) {
		this.selectedCircleColor = color;
		invalidate();
	}

	/**
	 * 设置选中的线的颜色
	 */
	public void setSelectedLineColor(int color) {
		this.selectedLineColor = color;
		invalidate();
	}

	private int getTextWidth(String text) {
		Rect textBound = new Rect();
		mTextPaint.getTextBounds(text, 0, text.length(), textBound);
		return textBound.width();
	}

	private Rect getTextBound(int i, int width, String text) {
		Rect textBound = new Rect();
		mTextPaint.getTextBounds(text, 0, text.length(), textBound);
		Rect rect = new Rect(getPaddingLeft() + i * width - 5, getPaddingTop() - 15,
				15 + getPaddingLeft() + i * width + textBound.width(), getPaddingTop() + 30 + textBound.height());
		return rect;
	}

	/**
	 * dp转px
	 */
	public int dpToPx(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp,
				getContext().getResources().getDisplayMetrics());
	}
}
