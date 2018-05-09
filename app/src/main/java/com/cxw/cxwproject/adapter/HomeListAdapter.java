package com.cxw.cxwproject.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cxw.cxwproject.MyApp;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.activity.AdvertisementActivity;
import com.cxw.cxwproject.activity.DetailsActivity;
import com.cxw.cxwproject.activity.NationalPageActivity;
import com.cxw.cxwproject.bean.HomesBean;
import com.cxw.cxwproject.bean.HomesBean.HomeContent.Content;
import com.cxw.cxwproject.bean.HomesBean.HomeContent.Content.HomeData;
import com.cxw.cxwproject.bean.UPMarqueeViewData;
import com.cxw.cxwproject.inter.UpmarqueeInter;
import com.cxw.cxwproject.util.GrapeGridview;
import com.cxw.cxwproject.util.ImageCycleView;
import com.cxw.cxwproject.util.ImageCycleView.ImageCycleViewListener;
import com.cxw.cxwproject.view.UPMarqueeView;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ViewFlipper;

public class HomeListAdapter extends QuickAdapter<HomesBean> implements ImageCycleViewListener, UpmarqueeInter {
	private int stype = 1;// 0=长条,1=圆形 ;
	private List<UPMarqueeViewData> UPMarqueedata = null;// 头条消息
	private int width = MyApp.getApp().getDisplayHightAndWightPx()[1];// 屏幕宽度
	private List<HomeData> banner;

	public HomeListAdapter(Context context, int layoutResId) {
		super(context, layoutResId);
	}
	/**
	 * 刷新适配器内数据
	 */
	public void notifyDataSetInvalidated(List<HomesBean> o) {
		this.data = o == null ? new ArrayList<HomesBean>() : new ArrayList<HomesBean>(o);
		notifyDataSetChanged();
	}	
	
	@Override
	protected void convert(BaseAdapterHelper helper, HomesBean item) {
		// 这里进行业务判断，view是否显示等
		List<Content> content = item.getContent().getContent();
		// 首先进行非空判断，不能为空，否则数组越界
		if (content.size() != 0) {
			String getTpl = content.get(0).getTpl();
			if (getTpl.equals("80001")) {
				// 80001表示显示广告
				BaseViewVisible(helper, R.id.page_view);
				ImageCycleView viewpage = helper.getView(R.id.page_view);
				banner = content.get(0).getData();
				helper.setImageViewLinearLayout(R.id.page_view, (width / 3), width);
				viewpage.setOnImageCycleViewListener(this);
				ArrayList<String> mImageUrl = new ArrayList<String>();// 广告
				for (int i = 0; i < banner.size(); i++) {
					mImageUrl.add(banner.get(i).getImg_url());
				}
				viewpage.setImageResources(mImageUrl, stype);
			} else if (getTpl.equals("80003")) {
				// 80003表示新闻资讯
				BaseViewVisible(helper, R.id.information_ll);
				((UPMarqueeView) helper.getView(R.id.upview)).getUpmarqueeInter(this);
				List<HomeData> data = content.get(0).getData();
				UPMarqueedata = new ArrayList<UPMarqueeViewData>();
				for (int j = 0; j < data.size(); j++) {
					UPMarqueedata.add(new UPMarqueeViewData(data.get(j).getSlogan(), data.get(j).getTitle(),
							data.get(j).getJump().getUrl()));
				}
				((UPMarqueeView) helper.getView(R.id.upview)).setViews(UPMarqueedata);
				((ViewFlipper) helper.getView(R.id.upview)).startFlipping();
			} else if (getTpl.equals("80004")) {
				// 80003表示民族推荐一行1张图
				BaseViewVisible(helper, R.id.view_recommend1_top_img);
				final List<HomeData> data = content.get(0).getData();
				helper.setImageViewLinearLayout(R.id.view_recommend1_top_img, (width / 3), width);
				helper.setImageUrl(R.id.view_recommend1_top_img, data.get(0).getImg_url());
				// 这里判断是否可以点击
				if (data.get(0).getJump().getUrl() != null && !data.get(0).getJump().getUrl().equals("")) {
					helper.setOnClickListener(R.id.view_recommend1_top_img, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							DetailsNation(data.get(0).getJump().getActivity_id(),
									data.get(0).getParams().getNation_id(), data.get(0).getParams().getHas_product());
						}
					});
				}
			} else if (getTpl.equals("80006")) {
				// 80006展示民族 一行2张图
				BaseViewVisible(helper, R.id.view_recommend1_gridview);
				final List<HomeData> data = content.get(0).getData();
				PushGridView mpushgridview = new PushGridView(context, R.layout.item_nation_region,
						content.get(0).getData());
				((GrapeGridview) helper.getView(R.id.view_recommend1_gridview)).setAdapter(mpushgridview);
				helper.setOnItemClickListener(R.id.view_recommend1_gridview, new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						DetailsNation(data.get(position).getJump().getActivity_id(),
								data.get(position).getParams().getNation_id(),
								data.get(position).getParams().getHas_product());
					}
				});
			} else if (getTpl.equals("80007")) {
				// 80007表示民族 一行3张图
				BaseViewVisible(helper, R.id.region_gridview);
				final List<HomeData> data = content.get(0).getData();
				NationalsAdapter Nadapter = new NationalsAdapter(context, R.layout.item_nation_region,
						content.get(0).getData());
				((GrapeGridview) helper.getView(R.id.region_gridview)).setAdapter(Nadapter);
				helper.setOnItemClickListener(R.id.region_gridview, new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						DetailsNation(data.get(position).getJump().getActivity_id(),
								data.get(position).getParams().getNation_id(),
								data.get(position).getParams().getHas_product());
					}
				});
			}
		}
	}

	/**
	 * 资讯头条 url表示跳转的链接
	 */
	@Override
	public void UPMarqueeOnclik(String url) {
		Intent intent = new Intent(context, AdvertisementActivity.class);
		intent.putExtra("url", url);
		context.startActivity(intent);

	}

	/**
	 * 民族屋与商品详情跳转判断
	 * 
	 * @param activity_id
	 * @param nation_id
	 * @param subscript
	 */
	public void DetailsNation(String activity_id, String nation_id, String has_product) {
		// 10100-民族屋详情,10200- 商品详情
		if (activity_id.equals("10100")) {
			boolean subscript = false;
			if (has_product.equals("yes")) {
				subscript = true;
			}
			Intent mIntent = new Intent(context, NationalPageActivity.class);
			mIntent.putExtra("id", nation_id);
			mIntent.putExtra("subscript", subscript);
			context.startActivity(mIntent);
		} else if (activity_id.equals("10200")) {
			Intent mIntent = new Intent(context, DetailsActivity.class);
			mIntent.putExtra("data_0", nation_id);
			context.startActivity(mIntent);
		}
	}

	/**
	 * 控件显示和隐藏方法
	 * 
	 * @param helper
	 * @param viewId
	 */
	private void BaseViewVisible(BaseAdapterHelper helper, int viewId) {
		// 广告、新闻资讯、一行一图布局、一行二图布局、一行三图布局
		int[] ArrayViewId = { R.id.page_view, R.id.information_ll, R.id.view_recommend1_top_img,
				R.id.view_recommend1_gridview, R.id.region_gridview };
		for (int i = 0; i < ArrayViewId.length; i++) {
			if (ArrayViewId[i] == viewId) {
				helper.setVisible(ArrayViewId[i], true);
			} else {
				helper.setVisible(ArrayViewId[i], false);
			}
		}

	}

	@Override
	public void onImageClick(int position, View imageView) {
		// 判断是纯广告还是商品、民族
		if (banner.get(position).getJump().getUrl() != null) {
			Intent mIntent = new Intent(context, AdvertisementActivity.class);
			mIntent.putExtra("url", banner.get(position).getJump().getUrl());
			context.startActivity(mIntent);
		} else {
			DetailsNation(banner.get(position).getJump().getActivity_id(),
					banner.get(position).getParams().getProduct_id(),
					banner.get(position).getParams().getHas_product());
		}
	}
}
