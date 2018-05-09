package com.cxw.cxwproject.activity;

import com.cxw.cxwproject.ActionBarFragmentActivity;
import com.cxw.cxwproject.MyApp;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.adapter.NationalPageAdapter;
import com.cxw.cxwproject.bean.NationalBean;
import com.cxw.cxwproject.dialog.FollowDialog;
import com.cxw.cxwproject.dialog.FollowDialog.ICoallBack;
import com.cxw.cxwproject.dialog.LoadingDialog;
import com.cxw.cxwproject.dialog.MoreDialog;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.imagedisplay.ImageDisplay;
import com.cxw.cxwproject.tool.SharePreferenceUtil;
import com.cxw.cxwproject.util.mvc.MVCHelper;
import com.cxw.cxwproject.util.mvc.MVCHelper.RequestListener;
import com.cxw.cxwproject.util.mvc.MVCViewHelper;
import com.cxw.cxwproject.widget.CustomViewPager;
import com.cxw.cxwproject.widget.EmptyLayout;
import com.cxw.cxwproject.widget.NoScrollView;
import com.cxw.cxwproject.widget.NoScrollView.OnScrollChangedListener;
import com.cxw.cxwproject.widget.NoScrollView.OnScrollListener;
import com.cxw.cxwproject.widget.NoScrollView.OnScrolledListener;
import com.cxw.cxwproject.widget.NoScrollViewPager;
import com.cxw.cxwproject.widget.PagerSlidingTabStrip;
import com.cxw.cxwproject.widget.ToastUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 民族特产屋
 * 
 * @author Administrator
 */
public class NationalPageActivity extends ActionBarFragmentActivity
		implements RequestListener, OnScrolledListener, OnScrollListener, ICoallBack, OnScrollChangedListener {
	private int searchLayoutTop;
	// 关注or未关注
	private TextView followFalse, followTrue;
	private RelativeLayout follow_ll;
	private LinearLayout search01, search02;
	private FollowDialog canceldialog;
	private PagerSlidingTabStrip tabs;
	private CustomViewPager pager;

	// 自定义变色状态栏
	private LinearLayout title_bg;
	private TextView title, introduction, name;
	private ImageView nationalpage_icon, nationalpage_bg;
	// 变态抽屉效果

	private NoScrollView noScrollView = null;
	private NoScrollViewPager mViewPager = null;

	// title+ActionBar高度
	private int bartle = 48;// 默认4.4以下系统距离，48dp->title、18dp->通知栏

	private MVCViewHelper<NationalBean> mvcViewHelper;

	private int follow = 0;// 设置0未关注，1关注

	int display;// 屏幕宽度
	public String nationId;// 民族id,文化ulr

	// loading

	LoadingDialog loading;

	NationalBean mNaModel;
	ImageAdapter mimageadapter;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_nationalpage;
	}

	@Override
	public int getActionBarType() {
		return ACTIONBAR_WHITE;
	}

	@Override
	protected void initView() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			bartle = 66;// 19以上通知栏透明
		}
		// 获取民族id
		nationId = getIntent().getStringExtra("id");
		display = MyApp.getApp().getDisplayHightAndWightPx()[1];
		search01 = (LinearLayout) findViewById(R.id.search01);
		search02 = (LinearLayout) findViewById(R.id.search02);
		// 取消关注弹框实例化
		canceldialog = new FollowDialog(NationalPageActivity.this);
		canceldialog.setonClick(this);// 实例化接口
		initData();// 数据
	}

	/**
	 * 数据绑定加载
	 */
	private void initData() {
		// 变态效果尝试
		noScrollView = (NoScrollView) findViewById(R.id.msv);
		noScrollView.setOnScrollListener(this);
		noScrollView.setOnScrollChangedListener(this);
		mViewPager = (NoScrollViewPager) findViewById(R.id.topvp);
		pager = (CustomViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		noScrollView.setListener(this);
		// 计算内容加载页面高度/宽度
		int height = MyApp.getApp().getDisplayHightAndWightPx()[0];
		int DISPLAYW = height - MyApp.getApp().dp2px(bartle);
		LinearLayout content = (LinearLayout) findViewById(R.id.content);
		content.setLayoutParams(new LinearLayout.LayoutParams(display, DISPLAYW));
		// 网络请求加载显示页面
		EmptyLayout emptyLayout = (EmptyLayout) findViewById(R.id.empty_view);
		mvcViewHelper = MVCHelper.createViewHelper(null, emptyLayout);
		mvcViewHelper.setRequestListener(this);
		mvcViewHelper.refresh();
	}

	/**
	 * 获取头部高度
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			// 获取searchLayout的顶部位置
			int mtop = MyApp.getApp().dp2px(bartle);
			searchLayoutTop = mViewPager.getBottom() - mtop;
		}
	}

	/**
	 * 监听滚动Y值变化，通过addView和removeView来实现悬停效果
	 */
	@Override
	public void onScroll(int scrollY) {
		if (scrollY >= searchLayoutTop) {// 滑动到顶部
			MyApp.getApp().setRoll(true);
			if (tabs.getParent() != search01) {
				search02.removeView(tabs);
				search01.addView(tabs);
			}
		} else {
			MyApp.getApp().setRoll(false);
			if (tabs.getParent() != search02) {
				search01.removeView(tabs);
				search02.addView(tabs);
			}
		}
	}

	class ImageAdapter extends PagerAdapter implements OnClickListener {
		private NationalBean mNaModel;
		private int mChildCount = 0;

		public ImageAdapter(NationalBean NaModel) {
			this.mNaModel = NaModel;
		}

		@Override
		public void notifyDataSetChanged() {
			mChildCount = getCount();
			super.notifyDataSetChanged();
		}

		@Override
		public int getItemPosition(Object object) {
			if (mChildCount > 0) {
				mChildCount--;
				return POSITION_NONE;
			}
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			View view = LayoutInflater.from(NationalPageActivity.this).inflate(R.layout.nation_view_header, container,
					false);
			// 店铺简介
			LinearLayout shopsimple = (LinearLayout) view.findViewById(R.id.shopsimple);
			shopsimple.setOnClickListener(this);
			// 自定义状态栏
			title_bg = (LinearLayout) view.findViewById(R.id.title_bg);
			title_bg.setBackgroundColor(Color.argb(0, 1, 137, 197));
			title = (TextView) view.findViewById(R.id.title);
			title.setTextColor(Color.argb(0, 255, 255, 255));
			title.setText(mNaModel.getName());
			name = (TextView) view.findViewById(R.id.name);// 民族名称
			name.setText(mNaModel.getName());
			introduction = (TextView) view.findViewById(R.id.introduction);// 民族简介
			introduction.setText(mNaModel.getIntroduction());
			// 关注1表示关注、否则未关注
			followFalse = (TextView) view.findViewById(R.id.follow_false);
			followTrue = (TextView) view.findViewById(R.id.follow_true);
			follow_ll = (RelativeLayout) view.findViewById(R.id.follow_ll);
			if (follow == 1) {
				follow_ll.setBackgroundResource(R.drawable.textview_style_while_90);
				followTrue.setVisibility(View.VISIBLE);
				followFalse.setVisibility(View.GONE);
			} else {
				follow_ll.setBackgroundResource(R.drawable.textview_style_theme_90);
				followFalse.setVisibility(View.VISIBLE);
				followTrue.setVisibility(View.GONE);
			}
			follow_ll.setOnClickListener(this);
			ImageView back = (ImageView) view.findViewById(R.id.back); // 返回
			back.setOnClickListener(this);
			// 更多选择
			ImageView right_btn_img = (ImageView) view.findViewById(R.id.right_btn_img);
			right_btn_img.setOnClickListener(this);
			// 民族屋背景
			nationalpage_bg = (ImageView) view.findViewById(R.id.nationalpage_bg);
			ImageDisplay.getSingleton().ImageLoader(nationalpage_bg, mNaModel.getHouse_theme_bg(), false);
			// 民族logo
			nationalpage_icon = (ImageView) view.findViewById(R.id.nationalpage_icon);
			ImageDisplay.getSingleton().ImageLoader(nationalpage_icon, mNaModel.getAvatar(), false);
			container.addView(view);
			return view;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.shopsimple:
				Intent miIntent = new Intent(NationalPageActivity.this, ShopSimpleActivity.class);
				miIntent.putExtra("introduction", mNaModel.getIntroduction());
				startActivity(miIntent);
				break;
			case R.id.right_btn_img:
				new MoreDialog(NationalPageActivity.this).show();
				break;
			case R.id.back:
				finish();
				break;
			case R.id.follow_ll:
				if (new SharePreferenceUtil().getCookie() != null) {
					if (follow == 1) {
						canceldialog.show();
					} else {
						Follow();// 接口请求
					}
				} else {
					startActivity(LoginActivity.class, 1);
				}
				break;
			}

		}
	}

	/**
	 * 取消关注接口
	 */
	@Override
	public void onClickButton() {
		Follow();// 取消关注
	}

	// 颜色渐变设置
	@SuppressWarnings("deprecation")
	@SuppressLint("UseValueOf")
	@Override
	public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
		int mviewpage = mViewPager.getHeight() - MyApp.getApp().dp2px(bartle);
		if (mViewPager != null && mviewpage > 0) {
			if (t < mviewpage) {
				int progress = (int) (new Float(t) / new Float(mviewpage) * 200);
				title_bg.setBackgroundColor(Color.argb(progress, 235, 89, 27));
				title.setTextColor(Color.argb(progress, 255, 255, 255));
				// TopBg---view变色
				follow_ll.getBackground().setAlpha(255 - progress);
				nationalpage_bg.setAlpha(255 - progress);
				nationalpage_icon.setAlpha(255 - progress);
				name.setTextColor(Color.argb(255 - progress, 255, 255, 255));
				introduction.setTextColor(Color.argb(255 - progress, 255, 255, 255));
			} else {
				title_bg.setBackgroundColor(Color.argb(255, 235, 89, 27));
				title.setTextColor(Color.argb(255, 255, 255, 255));
				// title.startAnimation(AnimationUtils.loadAnimation(this,
				// R.anim.view_scalebig));
				// title_bg.getBackground().setAlpha(255);// 导航栏透明度，直接减去55
			}
		}
	}

	/**
	 * 最外层scrollview滑动监听
	 */
	@Override
	public void scroll(int y) {
		mViewPager.scrollTo(display, -y);
	}

	/**
	 * 关注接口访问
	 */
	private void Follow() {
		loading = new LoadingDialog(this);
		Api.getFollow(nationId, new OnResponse<String>() {
			@Override
			public void onResponse(String o) {
				// 先重新赋值
				loading.dismiss();
				mNaModel.setIs_follow(follow == 1 ? 0 : 1);
				follow = (follow == 1 ? 0 : 1);
				mimageadapter.notifyDataSetChanged();
				ToastUtils.show(o);
			}

			@Override
			public void onErrorResponse(String error) {
				loading.dismiss();
				ToastUtils.show(error);
			}
		});
	}

	/**
	 * 民族信息相关
	 */
	@Override
	public void requestData(int page) {
		Api.getNational(nationId, new OnResponse<NationalBean>() {
			@Override
			public void onResponse(NationalBean o) {
				mvcViewHelper.executeOnLoadDataSuccess(o);
				follow = o.getIs_follow();
				// 开始赋值
				mNaModel = o;
				mimageadapter = new ImageAdapter(mNaModel);
				mViewPager.setAdapter(mimageadapter);
				pager.setAdapter(new NationalPageAdapter(getSupportFragmentManager(), nationId, o.getCulture_url()));
				tabs.setViewPager(pager);
				if (getIntent().getBooleanExtra("subscript", true) == false) {
					pager.setCurrentItem(1);
				}
			}

			@Override
			public void onErrorResponse(String error) {
				mvcViewHelper.executeOnLoadDataFail(error);
			}
		});
	}

}
