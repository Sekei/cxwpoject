package com.cxw.cxwproject.view.loading;


import com.cxw.cxwproject.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

public class TextViewLoadView extends View {
	// 最大值
	private static final float MAX = 100;
	// 系统默认:文字正常时颜色
	private static final int TEXT_COLOR_NORMAL = Color.parseColor("#000000");
	// 系统默认:文字高亮颜色
	private static final int TEXT_COLOR_HIGHLIGHT = Color.parseColor("#FF0000");
	// 绘制方向
	private static final int LEFT = 1, TOP = 2, RIGHT = 3, BOTTOM = 4;
	// 文字样式
	private static final int STYLE_TEXT = 1;
	// 图片样式
	private static final int STYLE_BITMAP = 2;
	// 顺序绘制
	private static final int LOAD_ASC = 0;
	// 反向/降序绘制
	private static final int LOAD_DESC = 1;

	/**
	 * 画笔
	 */
	private Paint mPaint;

	/**
	 * 绘制的范围
	 */
	private Rect mBound;

	/**
	 * 控件绘制位置起始的X,Y坐标值
	 */
	private int mStartX = 0, mStartY = 0;

	/**
	 * 文字大小
	 */
	private int mTextSize = 16;

	/**
	 * 文字正常颜色
	 */
	private int mTextColorNormal = TEXT_COLOR_NORMAL;

	/**
	 * 文字高亮颜色
	 */
	private int mTextColorHighLight = TEXT_COLOR_HIGHLIGHT;

	/**
	 * 文字
	 */
	private String mText;

	/**
	 * 绘制方向
	 */
	private int mDirection = LEFT;

	/**
	 * 控件风格
	 */
	private int mLoadStyle = STYLE_TEXT;

	/**
	 * bitmap正常/默认
	 */
	private Bitmap mBitmapNormal;

	/**
	 * bitmap高亮
	 */
	private Bitmap mBitmapHighLight;

	/**
	 * loading刻度
	 */
	private float mProgress = 0;

	/**
	 * 是否正在加载,避免开启多个线程绘图
	 */
	private boolean mIsLoading = false;

	/**
	 * 是否终止线程运行
	 */
	private boolean mCanRun = true;

	/**
	 * 加载方式{顺序,反向}
	 */
	private int mLoadMode = LOAD_ASC;

	public TextViewLoadView(Context context) {
		this(context, null);
	}

	public TextViewLoadView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.RLoadView);

		mText = a.getString(R.styleable.RLoadView_text);
		mTextColorNormal = a.getColor(R.styleable.RLoadView_text_color_normal,
				TEXT_COLOR_NORMAL);
		mTextColorHighLight = a.getColor(
				R.styleable.RLoadView_text_color_hightlight,
				TEXT_COLOR_HIGHLIGHT);
		mTextSize = a
				.getDimensionPixelSize(R.styleable.RLoadView_text_size, 16);
		mDirection = a.getInt(R.styleable.RLoadView_direction, LEFT);
		mLoadStyle = a.getInt(R.styleable.RLoadView_load_style, STYLE_TEXT);

		// 获取bitmap
		mBitmapNormal = getBitmap(a, R.styleable.RLoadView_bitmap_src_normal);
		mBitmapHighLight = getBitmap(a,
				R.styleable.RLoadView_bitmap_src_hightlight);

		a.recycle();

		/**
		 * 初始化画笔
		 */
		mBound = new Rect();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.FILL);

		if (mLoadStyle == STYLE_TEXT) {
			mPaint.setTextSize(mTextSize);
			mPaint.getTextBounds(mText, 0, mText.length(), mBound);
		} else if (mLoadStyle == STYLE_BITMAP) {
			mBound = new Rect(0, 0, mBitmapNormal.getWidth(),
					mBitmapNormal.getHeight());
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int width = onMeasureR(0, widthMeasureSpec);
		int height = onMeasureR(1, heightMeasureSpec);
		setMeasuredDimension(width, height);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		/**
		 * X,Y控件居中绘制 <br/>
		 * 对于文本居中绘制<br/>
		 * 1.mPaint.measureText(mText)精确度高于mBound.width()
		 * 2.文字高度测量:Math.abs((fontMetrics.bottom - fontMetrics.top))
		 * 3.http://blog.csdn.net/u014702653/article/details/51985821
		 */

		if (mLoadStyle == STYLE_TEXT) {

			// 控件高度/2 + 文字高度/2,绘制文字从文字左下角开始,因此"+"

			FontMetricsInt fm = mPaint.getFontMetricsInt();
			mStartY = getMeasuredHeight() / 2 - fm.descent
					+ (fm.bottom - fm.top) / 2;

			mStartX = (int) (getMeasuredWidth() / 2 - mPaint.measureText(mText) / 2);

		} else if (mLoadStyle == STYLE_BITMAP) {

			mStartX = getMeasuredWidth() / 2 - mBound.width() / 2;
			mStartY = getMeasuredHeight() / 2 - mBound.height() / 2;
		}

		onDrawR(canvas);
	}

	/**
	 * 计算控件宽高
	 * 
	 * @param attr属性
	 *            [0宽,1高]
	 * @param oldMeasure
	 * @author Ruffian
	 */
	public int onMeasureR(int attr, int oldMeasure) {
		int newSize = 0;
		int mode = MeasureSpec.getMode(oldMeasure);
		int oldSize = MeasureSpec.getSize(oldMeasure);

		switch (mode) {
		case MeasureSpec.EXACTLY:
			newSize = oldSize;
			break;
		case MeasureSpec.AT_MOST:
		case MeasureSpec.UNSPECIFIED:

			float value = 0;

			if (attr == 0) {

				if (mLoadStyle == STYLE_TEXT) {

					value = mPaint.measureText(mText);

				} else if (mLoadStyle == STYLE_BITMAP) {

					value = mBound.width();

				}
				// newSize
				newSize = (int) (getPaddingLeft() + value + getPaddingRight());

			} else if (attr == 1) {

				if (mLoadStyle == STYLE_TEXT) {

					FontMetrics fontMetrics = mPaint.getFontMetrics();
					value = Math.abs((fontMetrics.bottom - fontMetrics.top));

				} else if (mLoadStyle == STYLE_BITMAP) {

					value = mBound.height();

				}
				// newSize
				newSize = (int) (getPaddingTop() + value + getPaddingBottom());

			}

			break;
		}

		return newSize;
	}

	/**
	 * 控件绘制
	 * 
	 * @param canvas
	 * @author Ruffian
	 */
	public void onDrawR(Canvas canvas) {

		/**
		 * 主要思想:绘制两遍文字/图像,通过裁剪画布拼接两部分文字/图像,实现进度绘制的效果
		 */

		// 需要变色的宽高总值(长度)
		int drawTotalWidth = 0;
		int drawTotalHeight = 0;

		// X,Y变色的进度实时值
		int spliteXPro = 0;
		int spliteYPro = 0;

		// X,Y变色的最大值(坐标)
		int spliteXMax = 0;
		int spliteYMax = 0;

		// 开始变色的X,Y起始坐标值
		int spliteYStart = 0;
		int spliteXStart = 0;

		FontMetricsInt fm = mPaint.getFontMetricsInt();

		if (mLoadStyle == STYLE_TEXT) {

			drawTotalWidth = (int) mPaint.measureText(mText);
			drawTotalHeight = Math.abs(fm.ascent);

			spliteYStart = (fm.descent - fm.top) - Math.abs(fm.ascent)
					+ getPaddingTop();// 开始裁剪的Y坐标值:(http://img.blog.csdn.net/20160721172427552)图中descent位置+paddingTop
			spliteYMax = Math.abs(fm.top) + (fm.descent);// Y变色(裁剪)的进度最大值(坐标):(http://img.blog.csdn.net/20160721172427552)看图

		} else if (mLoadStyle == STYLE_BITMAP) {

			drawTotalWidth = mBound.width();
			drawTotalHeight = mBound.height();

			spliteYStart = mStartY;// 开始裁剪的Y坐标值:图片开始绘制的地方
			spliteYMax = mStartY + drawTotalHeight;// Y变色(裁剪)的进度最大值(坐标):图片开始绘制的地方+需要变色(裁剪)的高总值(长度)
		}

		spliteXPro = (int) ((mProgress / MAX) * drawTotalWidth);
		spliteYPro = (int) ((mProgress / MAX) * drawTotalHeight);

		spliteXStart = mStartX;// 开始裁剪的X坐标值:文字开始绘画的地方
		spliteXMax = spliteXStart + drawTotalWidth;// X变色(裁剪)的进度最大值(坐标):X变色(裁剪)起始位置+需要变色(裁剪)的宽总值(长度)

		switch (mDirection) {
		case TOP:

			// 从上到下,分界线上边是高亮颜色,下边是原始默认颜色
			onDrawTextOrBitmap(canvas, 1, spliteYStart, spliteYStart
					+ spliteYPro);
			onDrawTextOrBitmap(canvas, 0, spliteYStart + spliteYPro, spliteYMax);

			break;
		case BOTTOM:

			// 从下到上,分界线下边是默认颜色 ,上边是高亮颜色

			onDrawTextOrBitmap(canvas, 0, spliteYStart, spliteYMax - spliteYPro);
			onDrawTextOrBitmap(canvas, 1, spliteYMax - spliteYPro, spliteYMax);

			break;
		case LEFT:

			// 从左到右,分界线左边是高亮颜色,右边是原始默认颜色
			onDrawTextOrBitmap(canvas, 1, spliteXStart, spliteXStart
					+ spliteXPro);
			onDrawTextOrBitmap(canvas, 0, spliteXStart + spliteXPro, spliteXMax);

			break;
		case RIGHT:

			// 从右到左,分界线左边是默认颜色 ,右边是高亮颜色
			onDrawTextOrBitmap(canvas, 0, spliteXStart, spliteXMax - spliteXPro);
			onDrawTextOrBitmap(canvas, 1, spliteXMax - spliteXPro, spliteXMax);

			break;
		}

	}

	/**
	 * 绘制文字或者图片
	 * 
	 * @param canvas
	 * @param normalOrHightLight
	 *            [0:正常模式,1:高亮模式]
	 * @param start
	 * @param end
	 * @author Ruffian
	 */
	protected void onDrawTextOrBitmap(Canvas canvas, int normalOrHightLight,
			int start, int end) {

		canvas.save(Canvas.CLIP_SAVE_FLAG);

		switch (mDirection) {
		case LEFT:
		case RIGHT:

			// X轴画图
			canvas.clipRect(start, 0, end, getMeasuredHeight());

			break;
		case TOP:
		case BOTTOM:

			// Y轴画图
			canvas.clipRect(0, start, getMeasuredWidth(), end);

			break;
		}

		if (mLoadStyle == STYLE_TEXT) {

			// 绘制文字
			if (normalOrHightLight == 0) {
				mPaint.setColor(mTextColorNormal);
			} else {
				mPaint.setColor(mTextColorHighLight);
			}
			canvas.drawText(mText, mStartX, mStartY, mPaint);

		} else if (mLoadStyle == STYLE_BITMAP) {

			// 绘制图片
			if (normalOrHightLight == 0) {
				canvas.drawBitmap(mBitmapNormal, mStartX, mStartY, mPaint);
			} else {
				canvas.drawBitmap(mBitmapHighLight, mStartX, mStartY, mPaint);
			}

		}

		canvas.restore();

	}

	/**
	 * 开始
	 * 
	 * @param duration时间
	 * @param isRepeat是否循环重新变色
	 * @param isReverse是否反向褪色
	 * @author Ruffian
	 */
	public void start(final long duration, final boolean isRepeat,
			final boolean isReverse) {

		// 休眠时间
		final long sleepTime = (long) (duration / MAX);

		if (mIsLoading == false) {

			/**
			 * 设置正在加载,设置可以加载
			 */
			mIsLoading = true;
			mCanRun = true;

			new Thread() {
				public void run() {
					// 一直运行
					while (mCanRun) {
						// 顺序绘图
						if (mLoadMode == LOAD_ASC) {

							while (mProgress < MAX && mCanRun) {

								try {

									mProgress++;
									postInvalidate();// 重绘
									Thread.sleep(sleepTime);// 休眠
									if (mProgress == MAX) {

										if (isReverse) {
											// 如果支持反向绘制,则改变绘制模式
											mLoadMode = LOAD_DESC;
											break;
										} else {
											// 如果不支持反向绘制,则查看是否循环绘制
											if (isRepeat) {
												// 循环绘制从1开始
												mProgress = 0;// 0,1都无所谓
											} else {
												// 退出
												mCanRun = false;
											}
										}

									}
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}

						} else if (mLoadMode == LOAD_DESC) {
							// 反向绘图

							while (mProgress > 0 && mCanRun) {

								try {

									mProgress--;
									postInvalidate();// 重绘
									Thread.sleep(sleepTime);// 休眠
									if (mProgress == 0) {
										// 是否需要循环执行
										if (isRepeat) {
											// 如果需要循环执行,将模式改为顺序绘制,从0开始
											mProgress = 0;// 0才能跳出当前循环
											mLoadMode = LOAD_ASC;
											break;
										} else {
											// 退出
											mCanRun = false;
										}

									}

								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}

						}

					}

					/**
					 * 执行完成设置为false,重置
					 */
					mIsLoading = false;
					mLoadMode = LOAD_ASC;
					// mProgress = 0;
				};
			}.start();

		}

	}

	/**
	 * 停止
	 * 
	 * @author Ruffian
	 */
	public void stop() {

		mIsLoading = false;// 重置
		mCanRun = false;// 终止线程
		// mProgress = 0;// 强制停止清零
		if (mProgress == 0) {
			mLoadMode = LOAD_ASC;// 默认顺序绘图
		}

	}

	/**
	 * 获取进度值
	 */
	public float getProgress() {
		return mProgress;
	}

	/**
	 * 设置进度值
	 * 
	 * @param progress
	 */
	public void setProgress(float progress) {
		this.mProgress = progress;
		postInvalidate();// 重绘
	}

	/**
	 * 获取bitmap
	 * 
	 * @author Ruffian
	 */
	protected Bitmap getBitmap(TypedArray a, int styleable) {

		Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.cxw_icon);
		Drawable drawable = a.getDrawable(styleable);

		if (drawable != null) {

			if (drawable instanceof BitmapDrawable) {
				// 普通图片
				mBitmap = ((BitmapDrawable) drawable).getBitmap();
			} else if (drawable instanceof NinePatchDrawable) {
				// .9图处理
				Bitmap bitmap = Bitmap.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(), Config.ARGB_8888);
				Canvas canvas = new Canvas(bitmap);
				drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
				drawable.draw(canvas);
				mBitmap = bitmap;
			}
		}

		return mBitmap;
	}

	private static final String KEY_STATE_PROGRESS = "key_progress";
	private static final String KEY_DEFAULT_STATE = "key_default_state";

	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		bundle.putFloat(KEY_STATE_PROGRESS, mProgress);
		bundle.putParcelable(KEY_DEFAULT_STATE, super.onSaveInstanceState());
		return bundle;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {

		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			mProgress = bundle.getFloat(KEY_STATE_PROGRESS);
			super.onRestoreInstanceState(bundle
					.getParcelable(KEY_DEFAULT_STATE));
			return;
		}
		super.onRestoreInstanceState(state);
	}

}
