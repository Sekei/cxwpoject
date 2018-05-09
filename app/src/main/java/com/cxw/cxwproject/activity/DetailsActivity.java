package com.cxw.cxwproject.activity;

import java.util.ArrayList;

import com.cxw.cxwproject.ActionBarTransparentActivity;
import com.cxw.cxwproject.AppConfig;
import com.cxw.cxwproject.MyApp;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.adapter.UitimateAdapter;
import com.cxw.cxwproject.bean.CommentBean;
import com.cxw.cxwproject.bean.CommentsBean;
import com.cxw.cxwproject.bean.DetailsBean;
import com.cxw.cxwproject.dialog.BuyDialog;
import com.cxw.cxwproject.dialog.MoreDialog;
import com.cxw.cxwproject.dialog.SignShareDialog;
import com.cxw.cxwproject.dialog.SignShareDialog.OnItemClick;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.inter.UmengShareInter;
import com.cxw.cxwproject.tool.SharePreferenceUtil;
import com.cxw.cxwproject.util.CustomListView;
import com.cxw.cxwproject.util.ImageCycleView;
import com.cxw.cxwproject.util.ImageCycleView.ImageCycleViewListener;
import com.cxw.cxwproject.util.TitleColorUtil;
import com.cxw.cxwproject.util.UmengShareDao;
import com.cxw.cxwproject.util.mvc.MVCHelper;
import com.cxw.cxwproject.util.mvc.MVCHelper.RequestListener;
import com.cxw.cxwproject.util.mvc.MVCViewHelper;
import com.cxw.cxwproject.widget.EmptyLayout;
import com.cxw.cxwproject.widget.MyScrollView;
import com.cxw.cxwproject.widget.MyScrollView.OnScrollChangedListener;
import com.cxw.cxwproject.widget.ToastUtils;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 商品详情
 * @author Administrator
 */
public class DetailsActivity extends ActionBarTransparentActivity implements RequestListener, UmengShareInter,
		ImageCycleViewListener, OnScrollChangedListener, OnItemClick, OnClickListener {
	ImageCycleView viewPager;
	protected WebView mWebView;
	private LinearLayout ask_btn;
	private TextView title;
	private TextView all_evaluate;
	SignShareDialog mShareDialog;
	LinearLayout title_bg;// 顶部背景颜色
	private int alphaMax = 180;
	private ListView evaluate_list;// 评价列表
	// 游标是圆形还是长条，要是设置为0是长条，要是1就是圆形 默认是圆形
	public int stype = 1;
	private Drawable bgBackDrawable, bgShareDrawable, bgrightDrawable;
	UMWeb web;
	MVCViewHelper<DetailsBean> mvcViewHelper;
	QuickAdapter<CommentsBean> adapter;

	TextView details_name, details_description, goods_price;// 商品名
	TextView good_rate;// 评价相关
	LinearLayout evaluate_view_ll;

	DetailsBean mdata = new DetailsBean();

	public static DetailsActivity instance = null;

	private String product_id;
	@Override
	protected int getLayoutId() {
		return R.layout.activity_details;
	}

	@Override
	public int getActionBarType() {
		return ACTIONBAR_TRANSLATE | ACTIONBAR_NOT_SHOW;
	}

	@Override
	protected void initView() {
		instance = this;
		title_bg = (LinearLayout) findViewById(R.id.nav_bar);
		title_bg.setBackgroundColor(Color.argb(0, 1, 137, 197));
		// 滚动效果
		MyScrollView noScrollView = (MyScrollView) findViewById(R.id.msv);
		noScrollView.setOnScrollChangedListener(this);
		// title
		title = (TextView) findViewById(R.id.title);
		title.setText("商品详情");
		title.setTextColor(Color.argb(0, 255, 255, 255));
		product_id=getIntent().getStringExtra("data_0");
		// 图片相关
		int w = MyApp.getApp().getDisplayHightAndWightPx()[1];// 宽度
		viewPager = (ImageCycleView) findViewById(R.id.viewPager);
		viewPager.setLayoutParams(new android.widget.LinearLayout.LayoutParams(w, w));
		viewPager.setOnImageCycleViewListener(this);
		// 商品名称、简介、价格
		details_name = (TextView) findViewById(R.id.details_name);
		details_description = (TextView) findViewById(R.id.details_description);
		goods_price = (TextView) findViewById(R.id.goods_price);
		// 评价标签
		evaluate_view_ll = (LinearLayout) findViewById(R.id.evaluate_view_ll);
		good_rate = (TextView) findViewById(R.id.good_rate);
		CustomListView mcustomlist = (CustomListView) findViewById(R.id.mcustomlist);
		String[] arr = { "全部评价(100)", "好评(60)", "中评(30)" };
		UitimateAdapter uitimate = new UitimateAdapter(this, arr);
		mcustomlist.setAdapter(uitimate);
		mShareDialog = new SignShareDialog(this);
		mShareDialog.setOnItemClick(this);// 分享点击事件监听
		// 购物车
		ImageView gwc_btn_img = (ImageView) findViewById(R.id.gwc_btn_img);
		bgShareDrawable = gwc_btn_img.getBackground();
		bgShareDrawable.setAlpha(alphaMax);
		gwc_btn_img.setOnClickListener(this);
		// 右侧更多
		ImageView right_btn_img = (ImageView) findViewById(R.id.right_btn_img);
		bgrightDrawable = right_btn_img.getBackground();
		bgrightDrawable.setAlpha(alphaMax);
		right_btn_img.setOnClickListener(this);
		// 分享
		ImageView share_btn_img = (ImageView) findViewById(R.id.share_btn_img);
		share_btn_img.setOnClickListener(this);
		UmengShareDao.getUmengShare(this);
		// 问一问
		ask_btn = (LinearLayout) findViewById(R.id.ask_btn);
		ask_btn.setOnClickListener(this);
		// 立即购买
		TextView buyimmediately = (TextView) findViewById(R.id.buyimmediately);
		buyimmediately.setOnClickListener(this);
		// 加入购物车
		TextView joinashoppingcart = (TextView) findViewById(R.id.joinashoppingcart);
		joinashoppingcart.setOnClickListener(this);
		// 返回
		ImageView back = (ImageView) findViewById(R.id.back);
		bgBackDrawable = back.getBackground();
		bgBackDrawable.setAlpha(alphaMax);
		back.setOnClickListener(this);
		// 详情展示
		mWebView = (WebView) findViewById(R.id.web_view);
		// 网络请求加载显示页面
		EmptyLayout emptyLayout = (EmptyLayout) findViewById(R.id.empty_view);
		mvcViewHelper = MVCHelper.createViewHelper(null, emptyLayout);
		mvcViewHelper.setRequestListener(this);
		mvcViewHelper.refresh();
	}

	/**
	 * html浏览器相关
	 */
	private void mWebHtml(String url) {
		mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		mWebView.loadUrl(AppConfig.ICON + url);
		// mWebView.loadDataWithBaseURL(null, getNewContent(url), "text/html",
		// "utf-8", null);
	}

	/**
	 * 回调展现失败and成功
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 图文详解加载
	 * 
	 * @author Administrator
	 *
	 */
	public class WebViewClient extends android.webkit.WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url != null && url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:")) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(intent);
				return true;
			}
			return false;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}

	}

	@Override
	public void OnClick(SHARE_MEDIA media) {
		UmengShareDao.getUMWeb(this, media);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		UMShareAPI.get(this).release();
	}

	/**
	 * 点击事件
	 * 
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.share_btn_img:// 分享
			mShareDialog.show();
			break;
		case R.id.right_btn_img:// 右侧更多
			new MoreDialog(context).show();
			break;
		case R.id.gwc_btn_img:
			Intent mIntent = new Intent(this, HomeActivity.class);
			mIntent.putExtra("NewIntent", "1000101");
			startActivity(mIntent);
			break;
		case R.id.ask_btn:// 问一问
			startActivity(MechatActivity.class);
			break;
		case R.id.back:// 返回
			finish();
			break;
		case R.id.buyimmediately:// 购买
			if (new SharePreferenceUtil().getCookie() != null) {
				new BuyDialog(DetailsActivity.this, mdata).show();
			} else {
				startActivity(LoginActivity.class, 1);
			}
			break;
		case R.id.joinashoppingcart:// 加入购物车
			if (new SharePreferenceUtil().getCookie() != null) {
				getShoppingCart();
			} else {
				startActivity(LoginActivity.class, 1);
			}
			break;
		case R.id.all_evaluate:// 全部评价
			startActivity(AllEvaluateActivity.class,product_id);
			break;
		default:
			break;
		}

	}

	/**
	 * 加入购物车
	 */
	private void getShoppingCart() {
		Api.getJoinAShoppingCart(mdata.getProduct_id(), new OnResponse<String>() {
			@Override
			public void onResponse(String item) {
				ToastUtils.show(item);
			}

			@Override
			public void onErrorResponse(String error) {
				ToastUtils.show(error);
			}
		});
	}

	@Override
	public void requestData(int page) {
		Api.getDetails(product_id, new OnResponse<DetailsBean>() {
			@Override
			public void onResponse(DetailsBean item) {
				mvcViewHelper.executeOnLoadDataSuccess(item);
				mdata = item;
				// 开始绑定数据
				ArrayList<String> userList = new ArrayList<String>(item.getImages().length);
				for (String uid : item.getImages()) {
					userList.add(uid);
				}
				viewPager.setImageResources(userList, stype);
				viewPager.StopIt();// 默认不滚动，节约资源
				details_name.setText(item.getName() + "\t\t" + item.getAttribute());
				details_description.setText(item.getIntroduction());
				goods_price.setText("￥" + item.getPrice());
				// 评价信息相关
				if (item.getComment().getComments().size() != 0) {
					evaluate_view_ll.setVisibility(View.VISIBLE);
					getEvaluate(item.getComment());
				}
				mWebHtml(item.getUrl());// 展示相关
			}

			@Override
			public void onErrorResponse(String error) {
				mvcViewHelper.executeOnLoadDataFail(error);
			}
		});
	}

	/**
	 * 评价赋值
	 * 
	 * @param item
	 */
	private void getEvaluate(CommentBean item) {
		// 判断是否超过85%的好评
		String good_rate_str = item.getGood_rate();
		if (good_rate_str.contains("%")) {
			String s = new String(good_rate_str);
			String a[] = s.split("%");
			if (Integer.parseInt(a[0]) >= 85) {
				good_rate.setVisibility(View.VISIBLE);
			}
		}
		evaluate_list = (ListView) findViewById(R.id.evaluate_list);
		// 评价列表
		adapter = new QuickAdapter<CommentsBean>(this, R.layout.item_details_evaluate, item.getComments()) {
			@Override
			protected void convert(BaseAdapterHelper helper, CommentsBean item) {
				helper.setImageUrl(R.id.user_img, item.getAvatar());
				helper.setText(R.id.user_name, item.getNickname());
				helper.setText(R.id.datatime, item.getCreated());
				helper.setText(R.id.details_evaluate_content, item.getContent());
			}
		};
		evaluate_list.setAdapter(adapter);
		// 全部评价跳转
		all_evaluate = (TextView) findViewById(R.id.all_evaluate);
		all_evaluate.setOnClickListener(this);
		all_evaluate.setText("查看全部评价（共" + item.getAll_cnt() + "条）");
	}

	@Override
	public void onImageClick(int position, View imageView) {
		startActivity(ShowBigPictrueActivity.class, position, mdata.getImages());
	}

	@Override
	public void onScrollChanged(ScrollView who, int l, int alpha, int oldl, int oldt) {
		TitleColorUtil.getTitleColor(alpha, title_bg, bgBackDrawable, bgShareDrawable, bgrightDrawable, title);
	}

	@Override
	public void UmengShareOk() {
		mShareDialog.dismiss();
	}
}
