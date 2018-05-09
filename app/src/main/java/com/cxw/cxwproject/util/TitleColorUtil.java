package com.cxw.cxwproject.util;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TitleColorUtil {
	private static int alphaMax = 180;
	/**
	 * title 色彩变化
	 * 
	 * @param alpha
	 * @param title_bg
	 * @param bgBackDrawable
	 * @param bgShareDrawable
	 * @param bgrightDrawable
	 * @param title
	 */
	public static void getTitleColor(int alpha, LinearLayout title_bg, Drawable bgBackDrawable, Drawable bgShareDrawable,
			Drawable bgrightDrawable, TextView title) {
		if (alpha <= 255) {
			int alphaReverse = alphaMax - alpha;
			if (alphaReverse < 0) {
				alphaReverse = 0;
			}
			title_bg.setBackgroundColor(Color.argb(alpha, 235,89,27));
			bgShareDrawable.setAlpha(alphaReverse);
			bgBackDrawable.setAlpha(alphaReverse);
			bgrightDrawable.setAlpha(alphaReverse);
			title.setTextColor(Color.argb(alpha, 255, 255, 255));
		} else {
			title_bg.setBackgroundColor(Color.argb(255, 235,89,27));
			title.setTextColor(Color.argb(255, 255, 255, 255));
			bgShareDrawable.setAlpha(0);
			bgBackDrawable.setAlpha(0);
			bgrightDrawable.setAlpha(0);
		}
	}
}
