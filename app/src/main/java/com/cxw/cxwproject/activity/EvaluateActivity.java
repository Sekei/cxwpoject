package com.cxw.cxwproject.activity;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.OrderAllBean;
import com.cxw.cxwproject.dialog.LoadingDialog;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.imagedisplay.ImageDisplay;
import com.cxw.cxwproject.widget.ToastUtils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * 商品评价
 * 
 * @author Administrator
 *
 */
public class EvaluateActivity extends ActionBarActivity implements OnClickListener {
	private EditText evaluatecontext;
	private TextView number;
	private OrderAllBean mOrderData;
	private RatingBar mRatingBar;
	private String product_id;
	private String order_id;
	
	@Override
	protected int getLayoutId() {
		return R.layout.activity_evaluate;
	}

	@Override
	public int getActionBarType() {
		return ACTIONBAR_NOT_SHOW;
	}

	@Override
	protected void initView() {
		mOrderData =getIntentObject(OrderAllBean.class);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("商品评价");
		ImageView back = (ImageView) findViewById(R.id.back); // 返回
		back.setOnClickListener(this);
		ImageView commodity_goodsimg = (ImageView) findViewById(R.id.img);
		// 商品信息相关
		order_id= mOrderData.getOrder_id();
		if (mOrderData.getProducts().size() != 0) {
			ImageDisplay.getSingleton().ImageLoader(commodity_goodsimg, mOrderData.getProducts().get(0).getImage(),
					false);
			TextView commodity_name = (TextView) findViewById(R.id.commodity_name);
			commodity_name.setText(mOrderData.getProducts().get(0).getName());
			TextView commodity_money = (TextView) findViewById(R.id.commodity_money);
			commodity_money.setText(mOrderData.getProducts().get(0).getDiscount());
			TextView commodity_number = (TextView) findViewById(R.id.commodity_number);
			commodity_number.setText("X"+mOrderData.getProducts().get(0).getNumber());
			product_id = mOrderData.getProducts().get(0).getProduct_id();
		}
		// 留言
		evaluatecontext = (EditText) findViewById(R.id.evaluatecontext);
		evaluatecontext.addTextChangedListener(mTextWatcher);
		// 字数限制
		number = (TextView) findViewById(R.id.number);
		// 评价
		TextView evaluate_btn = (TextView) findViewById(R.id.evaluate_btn);
		evaluate_btn.setOnClickListener(this);
		mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
	}

	TextWatcher mTextWatcher = new TextWatcher() {
		private CharSequence temp;

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			temp = s;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			number.setText(temp.length() + "/100字");
		}
	};

	/**
	 * 订单评论
	 */
	private void getComment() {
		final LoadingDialog loading = new LoadingDialog(this);
		String stars = mRatingBar.getRating() + "";
		String content = evaluatecontext.getText().toString();
		Api.getEvaluate(order_id, product_id, stars, content, new OnResponse<String>() {
			@Override
			public void onResponse(final String item) {
				loading.dismiss();
				ToastUtils.show(item);
				setResult(104, getIntent());
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.evaluate_btn:
			getComment();
			break;
		default:
			break;
		}
	}
}
