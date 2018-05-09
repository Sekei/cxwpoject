package com.cxw.cxwproject.dialog;

import java.util.Arrays;

import com.cxw.cxwproject.MyApp;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.dialog.base.BaseDialog;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.umeng.socialize.bean.SHARE_MEDIA;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

public class ShareDialog extends BaseDialog implements OnItemClickListener {
	GridView classification;
	int Display;
	String url = "https://www.baidu.com";
	SHARE_MEDIA[] platforms = { SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
			SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SMS, SHARE_MEDIA.MORE, SHARE_MEDIA.SINA };
	Context mContext;
	OnItemClick mOnItemClick;

	public ShareDialog(Context context) {
		super(context);
		this.mContext = context;
	}

	/**
	 * 设置监听分享点击事件
	 * 
	 * @param onScrollListener
	 */
	public void setOnItemClick(OnItemClick onItemClick) {
		this.mOnItemClick = onItemClick;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.dialog_share;
	}

	@Override
	protected void initView() {
		Display = MyApp.getApp().getDisplayHightAndWightPx()[1];
		classification = (GridView) findViewById(R.id.classification);
		classification.setOnItemClickListener(this);
		classification.setAdapter(new QuickAdapter<String>(getContext(), R.layout.item_share,
				Arrays.asList("微信", "微信朋友圈", "微信收藏", "新浪", "QQ", "QQ空间", "短信", "更多")) {
			int res[] = { R.drawable.ic_share_wechat, R.drawable.ic_share_wemoment, R.drawable.umeng_socialize_fav,
					R.drawable.ic_share_sina, R.drawable.ic_share_qq, R.drawable.ic_share_qzone,
					R.drawable.ic_share_msg, R.drawable.ic_share_more };

			@Override
			protected void convert(BaseAdapterHelper helper, String item) {
				helper.setText(R.id.title, item);
				helper.setVisible(R.id.title, true);
				int w = Display / 7;// 宽度
				helper.setImageViewLinearLayout(R.id.img, w, w);
				helper.setImageResource(R.id.img, res[helper.getPosition()]);
			}
		});
		// 取消
		Button cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (platforms[arg2] == SHARE_MEDIA.MORE) {// 更多分享
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("image/*");
			Uri uri = Uri.parse("market://details?id=" + url);
			intent.putExtra(Intent.ACTION_VIEW, uri);
			intent.putExtra(Intent.EXTRA_TEXT, url);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			getContext().startActivity(Intent.createChooser(intent, "选择要分享到的平台"));
		} else {
			if (mOnItemClick != null) {
				mOnItemClick.OnClick(platforms[arg2]);
			}
		}
	}

	/**
	 * 定义点击接口
	 */
	public interface OnItemClick {
		public void OnClick(SHARE_MEDIA media);
	}

}
