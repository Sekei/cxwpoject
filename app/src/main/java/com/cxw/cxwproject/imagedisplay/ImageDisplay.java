package com.cxw.cxwproject.imagedisplay;

import com.cxw.cxwproject.AppConfig;
import com.cxw.cxwproject.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 图片加载单例模式
 * 
 * @author Sekei
 * 
 */
public class ImageDisplay {
	private volatile static ImageDisplay singleton;

	private ImageDisplay() {
	}

	public static ImageDisplay getSingleton() {
		if (singleton == null) {
			synchronized (ImageDisplay.class) {
				if (singleton == null) {
					singleton = new ImageDisplay();
				}
			}
		}
		return singleton;
	}

	/**
	 * 图片获取
	 * 
	 * @param img
	 * @param imgpath
	 */
	@SuppressWarnings("deprecation")
	public void ImageLoader(ImageView img, String imgpath, boolean boole) {
		if (boole == true) {
			ImageLoader.getInstance().displayImage(AppConfig.ICON + imgpath, img,
					new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.ic_default)
					.showImageOnFail(R.drawable.ic_default)
					.cacheInMemory(false)
					.cacheOnDisc(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.displayer(new SimpleBitmapDisplayer())
					.build());
		} else {
			ImageLoader.getInstance().displayImage(AppConfig.ICON + imgpath, img,
					new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.ic_default)
					.showImageOnFail(R.drawable.ic_default)
					.cacheInMemory(false)
					.cacheOnDisc(true)
					.displayer(new SimpleBitmapDisplayer())
					.bitmapConfig(Bitmap.Config.RGB_565)
					.build());
		}
	}

}
