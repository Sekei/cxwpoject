package com.cxw.cxwproject.util;

import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.Defaultcontent;
import com.cxw.cxwproject.inter.UmengShareInter;
import com.cxw.cxwproject.widget.ToastUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

/**
 * 友盟分享工具类
 * 
 * @author Administrator
 *
 */
final public class UmengShareDao {

	private static UMWeb web;
	// 分享成功接口
	private static UmengShareInter umengshareinter;

	public static void getUmengShare(UmengShareInter mumengshareinter) {
		umengshareinter = mumengshareinter;
	}

	/**
	 * 分享链接
	 * 
	 * @param context
	 */
	public static void getUMWeb(Context context, SHARE_MEDIA media) {
		// 先压缩图片
		UMImage image = new UMImage(context, R.drawable.cxw_icon);
		image.compressStyle = UMImage.CompressStyle.SCALE;// 大小压缩，默认为大小压缩，适合普通很大的图
		image.compressStyle = UMImage.CompressStyle.QUALITY;// 质量压缩，适合长图的分享
		// 压缩格式设置：
		image.compressFormat = Bitmap.CompressFormat.PNG;// 用户分享透明背景的图片可以设置这种方式，但是qq好友，微信朋友圈，不支持透明背景图片，会变成黑色
		web = new UMWeb(Defaultcontent.url);
		web.setTitle(Defaultcontent.title);// 标题
		web.setThumb(image); // 缩略图
		web.setDescription(Defaultcontent.text);// 描述
		new ShareAction((Activity) context).setPlatform(media).withMedia(web).setCallback(shareListener).share();
	}

	/**
	 * 带参分享
	 * 
	 * @param context
	 * @param media
	 * @param url
	 * @param content
	 */
	public static void getUMWeb(Context context, SHARE_MEDIA media, String url, String content) {
		// 先压缩图片
		UMImage image = new UMImage(context, R.drawable.cxw_icon);
		image.compressStyle = UMImage.CompressStyle.SCALE;// 大小压缩，默认为大小压缩，适合普通很大的图
		image.compressStyle = UMImage.CompressStyle.QUALITY;// 质量压缩，适合长图的分享
		// 压缩格式设置：
		image.compressFormat = Bitmap.CompressFormat.PNG;// 用户分享透明背景的图片可以设置这种方式，但是qq好友，微信朋友圈，不支持透明背景图片，会变成黑色
		web = new UMWeb(url);
		web.setTitle(Defaultcontent.title);// 标题
		web.setThumb(image); // 缩略图
		web.setDescription(content);// 描述
		new ShareAction((Activity) context).setPlatform(media).withMedia(web).setCallback(shareListener).share();
	}

	/**
	 * 分享后状态获取
	 */
	private static UMShareListener shareListener = new UMShareListener() {
		@Override
		public void onResult(SHARE_MEDIA platform) {
			ToastUtils.show("分享成功");
			umengshareinter.UmengShareOk();
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			ToastUtils.show("分享失败");
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			ToastUtils.show("分享取消");
		}

		@Override
		public void onStart(SHARE_MEDIA arg0) {
			ToastUtils.show("准备分享...");
		}
	};
}
