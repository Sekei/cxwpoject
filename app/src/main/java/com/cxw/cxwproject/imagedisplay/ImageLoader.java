package com.cxw.cxwproject.imagedisplay;

import com.cxw.cxwproject.AppConfig;
import com.cxw.cxwproject.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class ImageLoader {

	public static void displayImage(String uri, ImageAware imageAware) {
		displayImage(uri, imageAware, null, null, null);
	}

	public static void displayImage(String uri, ImageAware imageAware, ImageLoadingListener listener) {
		displayImage(uri, imageAware, null, listener, null);
	}

	public static void displayImage(String uri, ImageAware imageAware, DisplayImageOptions options) {
		displayImage(uri, imageAware, options, null, null);
	}

	public static void displayImage(String uri, ImageAware imageAware, DisplayImageOptions options,
			ImageLoadingListener listener) {
		displayImage(uri, imageAware, options, listener, null);
	}

	public static void displayImage(String uri, ImageAware imageAware, DisplayImageOptions options,
			ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
		// 处理一下uri
		if (uri == null) {
			uri = "";
		}
		if (uri.startsWith("http://") || uri.startsWith("https://") || uri.startsWith("file:///")
				|| uri.startsWith("content://") || uri.startsWith("assets://") || uri.startsWith("drawable://")) {
		} else {
			uri = AppConfig.ICON + uri;
		}
		com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(uri, imageAware, options,
				listener, progressListener);
	}

	public static void displayImage(String uri, ImageView imageView) {
		displayImage(uri, new ImageViewAware(imageView), options, null, null);
	}

	public static void displayImage(String uri, ImageView imageView, DisplayImageOptions options) {
		displayImage(uri, new ImageViewAware(imageView), options, null, null);
	}

	public static void displayImage(String uri, ImageView imageView, ImageLoadingListener listener) {
		displayImage(uri, new ImageViewAware(imageView), null, listener, null);
	}

	public static void displayImage(String uri, ImageView imageView, DisplayImageOptions options,
			ImageLoadingListener listener) {
		displayImage(uri, imageView, options, listener, null);
	}

	public static void displayImage(String uri, ImageView imageView, DisplayImageOptions options,
			ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
		displayImage(uri, new ImageViewAware(imageView), options, listener, progressListener);
	}
	/**
	 * 图片下载配置缓存
	 */
	static DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.ic_default)// 设置图片URL为空或是错误的时候显示图片
			.showImageOnFail(R.drawable.ic_default)// image加载失败或解码过程错误显示的图片
			.resetViewBeforeLoading(true)// 设置下载的图片下载前是否重置，复位
			.cacheInMemory(true)// 设置下载图片是否缓存到内存
			.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565)// 设置图片解码类型
			.displayer(new FadeInBitmapDisplayer(300))// 设置用户加载图片的task(这里是渐现)
			.build();
}
