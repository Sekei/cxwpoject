package com.cxw.cxwproject.activity;

import java.text.DecimalFormat;
import java.util.List;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.BuyBean;
import com.cxw.cxwproject.bean.DefaultAddressBean;
import com.cxw.cxwproject.bean.GoodsBean;
import com.cxw.cxwproject.dialog.LoadingDialog;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.tool.OperationTool;
import com.cxw.cxwproject.util.MyListView;
import com.cxw.cxwproject.widget.ToastUtils;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BuyOrderActivity extends ActionBarActivity implements OnClickListener {
	// 收货地址相关
	LinearLayout add_bg_btn, defaultaddress_btn, sendto_bg;
	TextView default_name, default_phone, default_address, address;
	DecimalFormat df = new DecimalFormat("0.00");// 保留2位小数
	String address_id;// 收货地址id
	EditText remark;// 备注留言
	TextView right_btn;// 修改地址
	MyListView goodslist;// 商品展示
	QuickAdapter<GoodsBean> adapter;
	List<GoodsBean> mGoodsBean;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_buyorder;
	}

	@Override
	public int getActionBarType() {
		return ACTIONBAR_NOT_SHOW;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initView() {
		ImageView back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("确认订单");
		right_btn = (TextView) findViewById(R.id.right_btn);
		right_btn.setText("修改地址");
		right_btn.setOnClickListener(this);
		// 收货地址相关
		add_bg_btn = (LinearLayout) findViewById(R.id.add_bg_btn);
		add_bg_btn.setOnClickListener(this);
		defaultaddress_btn = (LinearLayout) findViewById(R.id.defaultaddress_btn);
		default_name = (TextView) findViewById(R.id.default_name);
		default_phone = (TextView) findViewById(R.id.default_phone);
		default_address = (TextView) findViewById(R.id.default_address);
		// 备注留言
		remark = (EditText) findViewById(R.id.remark);
		goodslist = (MyListView) findViewById(R.id.goodslist);
		mGoodsBean = (List<GoodsBean>) getIntent().getSerializableExtra("data");
		adapter = new QuickAdapter<GoodsBean>(this, R.layout.item_buyordergoods, mGoodsBean) {
			@Override
			protected void convert(BaseAdapterHelper helper, GoodsBean item) {
				helper.setText(R.id.goods_name, item.getName());
				helper.setImageUrl(R.id.order_goodsimg, item.getImages());
				helper.setText(R.id.goods_description, item.getAttribute());
				helper.setText(R.id.goods_price, "￥" + item.getPrice());
				helper.setText(R.id.goods_num, item.getStock());

			}
		};
		goodslist.setAdapter(adapter);
		// 金额明细
		TextView paymentamount = (TextView) findViewById(R.id.paymentamount);
		TextView total = (TextView) findViewById(R.id.total);
		TextView buyprice = (TextView) findViewById(R.id.buyprice); // 结算
		double TotalNum = 0.000;// 默认总数和
		for (GoodsBean bean : mGoodsBean) {
			double andNum = OperationTool.mul(Float.valueOf(bean.getPrice()), Float.valueOf(bean.getStock()));
			TotalNum = OperationTool.add(TotalNum, andNum);
		}
		paymentamount.setText("￥" + df.format(TotalNum));
		total.setText("￥" + df.format(TotalNum));
		buyprice.setText("￥" + df.format(TotalNum));
		// 配送什么地方
		address = (TextView) findViewById(R.id.address);
		sendto_bg = (LinearLayout) findViewById(R.id.sendto_bg);
		// 去支付
		TextView buyimmediately = (TextView) findViewById(R.id.buyimmediately);
		buyimmediately.setOnClickListener(this);
		// 请求收货地址
		getDefaultAddress();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.right_btn:// 修改收货地址
			startActivityForResult(new Intent(BuyOrderActivity.this, AddressActivity.class), 0);
			break;
		case R.id.buyimmediately:// 下订单
			if (mGoodsBean.get(0).getSource().equals("0")) {// 直接购买
				BuyGood();
			} else {
				BuyCityGood();
			}
			break;
		case R.id.add_bg_btn:// 新增收货地址
			startActivityForResult(new Intent(BuyOrderActivity.this, AddressActivity.class), 0);
			break;
		default:
			break;
		}

	}

	/**
	 * 默认收货地址
	 */
	private void getDefaultAddress() {
		final LoadingDialog dialog = new LoadingDialog(this);
		Api.getDefaultAddress(new OnResponse<DefaultAddressBean>() {
			@Override
			public void onResponse(DefaultAddressBean item) {
				dialog.dismiss();
				add_bg_btn.setVisibility(View.GONE);
				defaultaddress_btn.setVisibility(View.VISIBLE);
				sendto_bg.setVisibility(View.VISIBLE);
				right_btn.setVisibility(View.VISIBLE);
				address_id = item.getId();// 收货地址id
				default_name.setText(item.getName());
				default_phone.setText(item.getPhone());
				default_address.setText("收货地址：" + item.getArea() + item.getAddress());
				address.setText(item.getArea() + item.getAddress());
			}

			@Override
			public void onErrorResponse(String error) {
				dialog.dismiss();
				defaultaddress_btn.setVisibility(View.INVISIBLE);
				add_bg_btn.setVisibility(View.VISIBLE);
				sendto_bg.setVisibility(View.GONE);
				right_btn.setVisibility(View.GONE);
			}
		});
	}

	/**
	 * 立即下订单
	 */
	private void BuyGood() {
		final LoadingDialog loading = new LoadingDialog(this);
		Api.getBuyGood(address_id, remark.getText().toString().trim(), mGoodsBean.get(0).getProduct_id(),
				mGoodsBean.get(0).getStock(), new OnResponse<BuyBean>() {
					@Override
					public void onResponse(BuyBean item) {
						loading.dismiss();
						Intent mIntent = new Intent(BuyOrderActivity.this, PayBuyActivity.class);
						mIntent.putExtra("buydata", item);
						startActivity(mIntent);
						DetailsActivity.instance.finish();
						finish();
					}

					@Override
					public void onErrorResponse(String error) {
						loading.dismiss();
						ToastUtils.show(error);
					}
				});
	}

	/**
	 * 购物车下单
	 */
	private void BuyCityGood() {
		final LoadingDialog loading = new LoadingDialog(this);
		Api.getBuyCityGood(address_id, remark.getText().toString().trim(), mGoodsBean,
				new OnResponse<BuyBean>() {
					@Override
					public void onResponse(BuyBean item) {
						loading.dismiss();
						Intent mIntent = new Intent(BuyOrderActivity.this, PayBuyActivity.class);
						mIntent.putExtra("buydata", item);
						startActivity(mIntent);
						finish();
					}

					@Override
					public void onErrorResponse(String error) {
						loading.dismiss();
						ToastUtils.show(error);
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 0:
			getDefaultAddress();
			break;
		default:
			break;
		}
	}
}
