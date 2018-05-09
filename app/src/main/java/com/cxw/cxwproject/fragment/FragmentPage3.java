package com.cxw.cxwproject.fragment;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import com.cxw.cxwproject.BaseFragment;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.activity.BuyOrderActivity;
import com.cxw.cxwproject.activity.DetailsActivity;
import com.cxw.cxwproject.activity.HomeActivity;
import com.cxw.cxwproject.activity.LoginActivity;
import com.cxw.cxwproject.adapter.CartAdapter;
import com.cxw.cxwproject.bean.GoodsBean;
import com.cxw.cxwproject.bean.ShoppingCartBean;
import com.cxw.cxwproject.cart.CartDataHttpApi;
import com.cxw.cxwproject.cart.CartDataHttpApi.OnCartHttpData;
import com.cxw.cxwproject.data.BuyOrderData;
import com.cxw.cxwproject.tool.CartTool;
import com.cxw.cxwproject.tool.ControlVisibleGoone;
import com.cxw.cxwproject.tool.CxwBackgroundColor;
import com.cxw.cxwproject.tool.SharePreferenceUtil;
import com.cxw.cxwproject.widget.ToastUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 购物车
 * 
 * @author Sekei
 * 
 */
public class FragmentPage3 extends BaseFragment
		implements OnItemClickListener, OnCartHttpData, OnRefreshListener<ListView>, OnClickListener {
	DecimalFormat df = new DecimalFormat("0.00");// 保留2位小数
	LinearLayout login_bg, pricedisplay;// 登录、合计价格展示
	RelativeLayout content_bg;
	PullToRefreshListView pull_refresh_cartlist;
	CartAdapter adapter;
	CheckBox checkBox, edit_btn;// 、编辑
	List<ShoppingCartBean> listItems = null;
	TextView popTotalPrice, buy_btn, cart_title;// 价格、结算数量、title
	View mContentView;
	int shopNumber = 0;// 购买或删除数量

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_3;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (new SharePreferenceUtil().getCookie() != null) {
			ControlVisibleGoone.setVisibility(View.GONE, login_bg);
			ControlVisibleGoone.setChecked(false, checkBox, edit_btn);
			popTotalPrice.setText("￥0.00");
			shopNumber = 0;// 重新赋值商品总数
			getEdit_Btn();// 编辑状态下逻辑处理
			CartDataHttpApi.getShoppingCart(pull_refresh_cartlist);
		} else {
			ControlVisibleGoone.setVisibility(View.GONE, mContentView, edit_btn);
			ControlVisibleGoone.setVisibility(View.VISIBLE, login_bg, content_bg);
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (listItems != null) {
			adapter.notifyDataSetInvalidated(listItems);
		}
	}

	@Override
	protected void initView(View view) {
		super.initView(view);
		CartDataHttpApi.setOnCartHttpData(this);
		login_bg = (LinearLayout) view.findViewById(R.id.login_bg);
		content_bg = (RelativeLayout) view.findViewById(R.id.content_bg);
		TextView cart_login_btn = (TextView) view.findViewById(R.id.cart_login_btn);
		cart_login_btn.setOnClickListener(this);
		TextView stroll_btn = (TextView) view.findViewById(R.id.stroll_btn);
		stroll_btn.setOnClickListener(this);
		mContentView = (View) view.findViewById(R.id.cart_view);// 内容显示
		pull_refresh_cartlist = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_cartlist);
		pull_refresh_cartlist.setOnRefreshListener(this);// 刷新
		pull_refresh_cartlist.setOnItemClickListener(this);
		popTotalPrice = (TextView) view.findViewById(R.id.poptotalprice); // 价钱
		buy_btn = (TextView) view.findViewById(R.id.buy); // 结算数量
		buy_btn.setOnClickListener(this);
		checkBox = (CheckBox) view.findViewById(R.id.all_check);// 全选中
		checkBox.setOnClickListener(this);
		edit_btn = (CheckBox) view.findViewById(R.id.edit_btn); // 编辑
		edit_btn.setOnClickListener(this);
		pricedisplay = (LinearLayout) view.findViewById(R.id.pricedisplay);
		cart_title = (TextView) view.findViewById(R.id.cart_title);
	}

	/**
	 * 购买按钮处理
	 */
	private void getBuyHandle() {
		if (edit_btn.isChecked()) {
			CartDataHttpApi.getDelete(pull_refresh_cartlist, getActivity(), listItems, adapter);
		} else {
			if (CartTool.getInstance().deleteOrCheckOutShop(listItems, adapter).equals("")) {
				ToastUtils.show("请选择要购买的商品");
			}else{
				List<GoodsBean> mListGoods = BuyOrderData.GoodsData(adapter, listItems);
				Intent mIntent = new Intent(getActivity(), BuyOrderActivity.class);
				mIntent.putExtra("data", (Serializable) mListGoods);
				startActivity(mIntent);
			}
		}
	}

	/**
	 * 编辑
	 */
	private void getEdit_Btn() {
		if (edit_btn.isChecked() == true) {// 選中
			edit_btn.setText("完成");
			ControlVisibleGoone.setVisibility(View.INVISIBLE, pricedisplay);
		} else {
			edit_btn.setText("编辑");
			ControlVisibleGoone.setVisibility(View.VISIBLE, pricedisplay);
		}
		getEditDelete(shopNumber);
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 10) { // 更改选中商品的总价格
				popTotalPrice.setText("￥" + df.format((Float) msg.obj));
			} else if (msg.what == 11) {
				checkBox.setChecked((Boolean) msg.obj);
			} else if (msg.what == 12) { // 更改选中商品的总价格====商品刷新
				adapter.notifyDataSetChanged();
				popTotalPrice.setText("￥" + df.format((Float) msg.obj));
			} else if (msg.what == 13) {// 多少商品
				shopNumber = Integer.parseInt((msg.obj).toString());
				getEditDelete(shopNumber);
			}
		}
	};

	/**
	 * 删除--结算
	 */
	private void getEditDelete(int shopNumber) {
		if (edit_btn.isChecked() == true) {// 選中
			buy_btn.setText("删除(" + shopNumber + ")");
			CxwBackgroundColor.setBackgroundColor(getActivity(), buy_btn, 1);
		} else {
			buy_btn.setText("结算(" + shopNumber + ")");
			CxwBackgroundColor.setBackgroundColor(getActivity(), buy_btn, 0);
		}
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		CartDataHttpApi.getShoppingCart(pull_refresh_cartlist);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent mIntent = new Intent(getActivity(), DetailsActivity.class);
		mIntent.putExtra("data_0",  listItems.get(position - 1).getProduct().getProduct_id());
		startActivity(mIntent);
	}

	@Override
	public void onCartData(List<ShoppingCartBean> itemShoppingCartBean) {
		if (itemShoppingCartBean.size() == 0) {
			ControlVisibleGoone.setVisibility(View.VISIBLE, content_bg);
			ControlVisibleGoone.setVisibility(View.GONE, mContentView, cart_title, edit_btn);
		} else {
			ControlVisibleGoone.setVisibility(View.GONE, content_bg);
			ControlVisibleGoone.setVisibility(View.VISIBLE, mContentView, cart_title, edit_btn);
			cart_title.setText("(" + itemShoppingCartBean.size() + ")");
			if (listItems != null) {
				listItems = itemShoppingCartBean;
				checkBox.setChecked(false);
				CartTool.getInstance().AllCheck(listItems, adapter, false, handler);
				adapter.notifyDataSetInvalidated(listItems);
			} else {
				listItems = itemShoppingCartBean;
				adapter = new CartAdapter(getActivity(), handler, listItems);
				pull_refresh_cartlist.setAdapter(adapter);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cart_login_btn:
			startActivity(LoginActivity.class, 1);
			break;
		case R.id.stroll_btn:
			HomeActivity parentActivity = (HomeActivity) getActivity();
			parentActivity.pageListener();
			break;
		case R.id.all_check://
			CartTool.getInstance().AllCheck(listItems, adapter, checkBox.isChecked(), handler);
			break;
		case R.id.edit_btn:// 编辑
			getEdit_Btn();
			break;
		case R.id.buy:// 购买===删除
			getBuyHandle();
			break;
		}
	}
}