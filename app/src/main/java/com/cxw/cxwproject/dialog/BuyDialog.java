package com.cxw.cxwproject.dialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cxw.cxwproject.R;
import com.cxw.cxwproject.activity.BuyOrderActivity;
import com.cxw.cxwproject.bean.DetailsBean;
import com.cxw.cxwproject.bean.GoodsBean;
import com.cxw.cxwproject.data.BuyOrderData;
import com.cxw.cxwproject.dialog.base.BaseDialog;
import com.cxw.cxwproject.imagedisplay.ImageDisplay;
import com.cxw.cxwproject.util.AmountView;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class BuyDialog extends BaseDialog implements OnClickListener {
	Context mContext;
	DetailsBean mdata;
	OnClickAction mOnClickAction;
	int Number = 1;

	public void setOnClickAction(OnClickAction mOnClickAction) {
		this.mOnClickAction = mOnClickAction;
	}

	public BuyDialog(Context context) {
		super(context);
	}

	public BuyDialog(Context context, DetailsBean mdata) {
		super(context);
		this.mContext = context;
		this.mdata = mdata;
		initViewData();// 赋值开始
	}

	@Override
	protected int getLayoutId() {
		return R.layout.buy_dialog;
	}

	@Override
	protected void initView() {
		super.initView();
	}

	private void initViewData() {
		// 关闭
		ImageView buy_close_btn = (ImageView) findViewById(R.id.buy_close_btn);
		buy_close_btn.setOnClickListener(this);
		// 确认
		TextView confirm_btn = (TextView) findViewById(R.id.confirm_btn);
		confirm_btn.setOnClickListener(this);
		// 赋值--价格、库存、图片
		TextView buy_price = (TextView) findViewById(R.id.buy_price);
		TextView buy_stock = (TextView) findViewById(R.id.buy_stock);
		ImageView buy_img = (ImageView) findViewById(R.id.buy_img);
		buy_price.setText("￥" + mdata.getPrice());
		buy_stock.setText("库存" + mdata.getStock() + "件");
		ImageDisplay.getSingleton().ImageLoader(buy_img, mdata.getImages()[0], true);
		// 加、减组和控件件
		AmountView mAmountView = (AmountView) findViewById(R.id.amount_view);
		// 最大库存
		mAmountView.setGoods_storage(Integer.parseInt(mdata.getStock()));
		mAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
			@Override
			public void onAmountChange(View view, int amount) {
				Number = amount;
			}
		});

	}

	public interface OnClickAction {
		void onClickAction(int amount);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buy_close_btn:
			dismiss();
			break;
		case R.id.confirm_btn:
			List<GoodsBean> mListGoods = BuyOrderData.GoodsData(mdata, Number);
			Intent mIntent = new Intent(mContext, BuyOrderActivity.class);
			mIntent.putExtra("data", (Serializable) mListGoods);
			mContext.startActivity(mIntent);
			dismiss();
			break;
		default:
			break;
		}

	}
}
