package com.cxw.cxwproject.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cxw.cxwproject.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 侧滑栏视图
 */
public class SideBar extends View {
	private String[] letterStrings = { "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
			"P", "Q", "R", "S", "T", "U", "V", "W", "S", "Y", "Z" };
	private Paint paint;
	// 字母列表
	private List<String> letter;
	// 绘制字母的x,y坐标
	private int x, y;
	// 字母的间距
	private int letterSpace = 0;
	// 字母是否大写
	private boolean isLetterUpper = true;

	private OnCurrentLetterListener onCurrentLetterListener;

	public void setLetter(List<String> letter) {
		this.letter = letter;
	}

	// 设置回调接口
	public void setOnCurrentLetterListener(OnCurrentLetterListener onCurrentLetterListener) {
		this.onCurrentLetterListener = onCurrentLetterListener;
	}

	public SideBar(Context context) {
		this(context, null);
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);

		if (letter == null) {
			letter = new ArrayList<String>();
			letter = Arrays.asList(letterStrings);
		}

		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SideBar);
		letterSpace = typedArray.getInt(R.styleable.SideBar_SB_Letter_Space, 0);
		isLetterUpper = typedArray.getBoolean(R.styleable.SideBar_SB_Is_Letter_Upper, true);
		typedArray.recycle();
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
		paint.setTextSize(45);// 3CAC48
		paint.setColor(getResources().getColor(R.color.uli_bg));
		// paint.setTypeface(Typeface.create(Typeface.SANS_SERIF,
		// Typeface.BOLD));
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int finalWidth = 0, finalHeight = 0;
		// 测量单个字符的宽度和高度
		float charWidthAndHeight = paint.measureText(letter.get(0)) + letterSpace;
		if (widthMode == MeasureSpec.EXACTLY) {
			finalWidth = MeasureSpec.getSize(widthMeasureSpec);
		} else if (widthMode == MeasureSpec.AT_MOST) {
			finalWidth = (int) charWidthAndHeight + getPaddingLeft() + getPaddingRight();
		}
		if (heightMode == MeasureSpec.EXACTLY) {
			finalHeight = MeasureSpec.getSize(heightMeasureSpec);
		} else if (heightMode == MeasureSpec.AT_MOST) {
			// 注意measureText的值与paint.setTextSize的值有关
			finalHeight = (int) charWidthAndHeight * letter.size() + getPaddingBottom() + getPaddingTop();
		}
		setMeasuredDimension(finalWidth, finalHeight);
	}

	@SuppressLint("DefaultLocale")
	@Override
	protected void onDraw(Canvas canvas) {
		// 背景颜色
		// setBackgroundColor(Color.parseColor("#00000000"));
		// 27个字符（包含#）均分整个视图的高度，例如视图高度为270，270/27均分之后，每个字符的y坐标为10
		y = getHeight() / letter.size();
		Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
		for (int i = 0; i < letter.size(); i++) {
			// 计算绘制字符X的坐标，整个视图宽度的一半减去字符宽度的一半
			x = getWidth() / 2 - (int) paint.measureText(letter.get(i)) / 2;
			int correctY = ((y * i + y) + (y * i) - fontMetricsInt.bottom - fontMetricsInt.top) / 2;
			String tempString = isLetterUpper ? letter.get(i).toUpperCase() : letter.get(i).toLowerCase();
			canvas.drawText(tempString, x, correctY, paint);
			// 字母分割线
			// canvas.drawLine(0, y * i + y, 100, y * i + y, paint);
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float y = event.getY();
		// 获取当前侧滑栏字母的下标
		float currentLetterIndex = y / getHeight() * letter.size();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:

			if (onCurrentLetterListener != null) {
				// 对上下边界对限制
				if (currentLetterIndex >= letter.size()) {
					currentLetterIndex = letter.size() - 1;
				} else if (currentLetterIndex < 0) {
					currentLetterIndex = 0;
				}
				onCurrentLetterListener.showCurrentLetter(letter.get((int) currentLetterIndex));

			}
			return true;

		case MotionEvent.ACTION_UP:
			if (onCurrentLetterListener != null) {
				onCurrentLetterListener.hideCurrentLetter();
			}
			return true;

		default:
			return true;
		}
	}

	/**
	 * 回调接口。
	 */
	public interface OnCurrentLetterListener {
		void showCurrentLetter(String currentLetter);

		void hideCurrentLetter();
	}

}
