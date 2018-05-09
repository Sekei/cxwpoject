package com.cxw.cxwproject.activity;

import java.util.Arrays;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.UserBean;
import com.cxw.cxwproject.imagedisplay.ImageDisplay;
import com.cxw.cxwproject.imagedisplay.ImageLoader;
import com.cxw.cxwproject.util.RoundedImageView;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdminActivity extends ActionBarActivity implements OnClickListener, OnItemClickListener {
	TextView username, user_integral;
	RoundedImageView member_logo;
	static AdminActivity instance = null;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_admin;
	}

	@Override
	public int getActionBarType() {
		return ACTIONBAR_NOT_SHOW;
	}

	@Override
	protected void initView() {
		instance = this;
		// 个人资料-头像
		member_logo = (RoundedImageView) findViewById(R.id.member_logo);
		ImageLoader.displayImage(UserBean.defaultShop().getAvatar(), member_logo);
		member_logo.setOnClickListener(this);
		// 签到跳转
		LinearLayout sign_btn = (LinearLayout) findViewById(R.id.sign_btn);
		sign_btn.setOnClickListener(this);
		// 我的收藏
		TextView collection_btn = (TextView) findViewById(R.id.collection_btn);
		collection_btn.setOnClickListener(this);
		// 聊天
		TextView chat = (TextView) findViewById(R.id.chat);
		chat.setOnClickListener(this);
		// 收货地址
		TextView admin_address = (TextView) findViewById(R.id.admin_address);
		admin_address.setOnClickListener(this);
		// 设置
		TextView admin_setup = (TextView) findViewById(R.id.admin_setup);
		admin_setup.setOnClickListener(this);
		// 评分
		TextView score_btn = (TextView) findViewById(R.id.score_btn);
		score_btn.setOnClickListener(this);
		// title名称
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("个人中心");
		// 会员名称-------名称为空的情况下获取手机绑定显示
		username = (TextView) findViewById(R.id.username);
		username.setText(UserBean.defaultShop().getNickname());
		// 会员积分
		user_integral = (TextView) findViewById(R.id.user_integral);
		user_integral.setText(UserBean.defaultShop().getIntegral().getIntegral());
		// 返回
		ImageView back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		// 订单管理
		GridView order_grapegrid = (GridView) findViewById(R.id.order_grapegrid);
		order_grapegrid.setOnItemClickListener(this);
		order_grapegrid.setAdapter(
				new QuickAdapter<String>(this, R.layout.order_grapegrid_item, Arrays.asList("待付款", "待收货", "全部订单")) {
					int res[] = { R.drawable.account_payment_normal_white, R.drawable.account_logistics_normal_white,
							R.drawable.account_order_normal_white };

					@Override
					protected void convert(BaseAdapterHelper helper, String item) {
						helper.setText(R.id.order_text, item);
						helper.setVisible(R.id.order_text, true);
						helper.setImageResource(R.id.order_imgview, res[helper.getPosition()]);
					}
				});

	}

	/**
	 * GridView点击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(AdminActivity.this, OrderManagementActivity.class);
		intent.putExtra("id", position == 0 ? 1 : (position == 1 ? 2 : 0));
		startActivity(intent);
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.member_logo:// 头像
			startActivityForResult(MemberDataActivity.class, 1);
			break;
		case R.id.sign_btn:
			startActivityForResult(SignActivity.class, 0);
			break;
		case R.id.collection_btn:// 我的收藏
			startActivity(CollectionActivity.class);
			break;
		case R.id.chat:// 客服帮助
			startActivity(MechatActivity.class);
			break;
		case R.id.admin_address:// 收货地址
			startActivity(AddressActivity.class);
			break;
		case R.id.admin_setup:// 设置
			startActivity(SetupActivity.class);
			break;
		case R.id.score_btn:// 评分
			Score();
			break;
		case R.id.back:// 返回
			finish();
			break;
		default:
			break;
		}

	}

	/**
	 * 评分
	 */
	private void Score() {
		try {
			Uri uri = Uri.parse("market://details?id=" + getPackageName());
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			e.getMessage();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		username.setText(UserBean.defaultShop().getNickname());
		ImageDisplay.getSingleton().ImageLoader(member_logo, UserBean.defaultShop().getAvatar(), false);
	}
}
