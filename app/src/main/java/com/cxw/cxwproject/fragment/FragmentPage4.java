package com.cxw.cxwproject.fragment;

import java.util.Arrays;

import com.cxw.cxwproject.BaseFragment;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.activity.AddressActivity;
import com.cxw.cxwproject.activity.CollectionActivity;
import com.cxw.cxwproject.activity.HelpActivity;
import com.cxw.cxwproject.activity.LoginActivity;
import com.cxw.cxwproject.activity.MechatActivity;
import com.cxw.cxwproject.activity.MemberDataActivity;
import com.cxw.cxwproject.activity.OpinionActivity;
import com.cxw.cxwproject.activity.OrderManagementActivity;
import com.cxw.cxwproject.activity.SetupActivity;
import com.cxw.cxwproject.activity.SignActivity;
import com.cxw.cxwproject.bean.UserBean;
import com.cxw.cxwproject.imagedisplay.ImageDisplay;
import com.cxw.cxwproject.tool.GenderTool;
import com.cxw.cxwproject.tool.SharePreferenceUtil;
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

/**
 * 个人中心
 * 
 * @author Sekei
 */
public class FragmentPage4 extends BaseFragment implements OnClickListener, OnItemClickListener {
	TextView username, user_integral;
	RoundedImageView member_logo;
	ImageView gender_icon;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_4;
	}

	@Override
	public void onResume() {
		super.onResume();
		username.setText(UserBean.defaultShop().getNickname());
		user_integral.setText(UserBean.defaultShop().getIntegral().getIntegral());
		ImageDisplay.getSingleton().ImageLoader(member_logo, UserBean.defaultShop().getAvatar(), false);
		gender_icon.setBackgroundResource(GenderTool.getInstance().GednerStr(UserBean.defaultShop().getGender()));
	}

	@Override
	protected void initView(View view) {
		super.initView(view);
		member_logo = (RoundedImageView) view.findViewById(R.id.member_logo);
		member_logo.setOnClickListener(this);
		gender_icon = (ImageView) view.findViewById(R.id.gender_icon);// 性别
		username = (TextView) view.findViewById(R.id.username);// 会员名称-------名称为空的情况下获取手机绑定显示
		user_integral = (TextView) view.findViewById(R.id.user_integral);// 会员积分
		LinearLayout sign_btn = (LinearLayout) view.findViewById(R.id.sign_btn);
		sign_btn.setOnClickListener(this);
		// 我的收藏
		TextView collection_btn = (TextView) view.findViewById(R.id.collection_btn);
		collection_btn.setOnClickListener(this);
		// 聊天
		TextView chat = (TextView) view.findViewById(R.id.chat);
		chat.setOnClickListener(this);
		// 收货地址
		TextView admin_address = (TextView) view.findViewById(R.id.admin_address);
		admin_address.setOnClickListener(this);
		//意见反馈
		TextView opinion_btn = (TextView) view.findViewById(R.id.opinion_btn);
		opinion_btn.setOnClickListener(this);
		// 设置
		TextView admin_setup = (TextView) view.findViewById(R.id.admin_setup);
		admin_setup.setOnClickListener(this);
		// 评分
		TextView score_btn = (TextView) view.findViewById(R.id.score_btn);
		score_btn.setOnClickListener(this);
		// 订单管理
		GridView order_grapegrid = (GridView) view.findViewById(R.id.order_grapegrid);
		order_grapegrid.setOnItemClickListener(this);
		order_grapegrid.setAdapter(new QuickAdapter<String>(getActivity(), R.layout.order_grapegrid_item,
				Arrays.asList("待付款", "待发货", "待收货", "待评价", "全部订单")) {
			int res[] = { R.drawable.account_payment_normal_white, R.drawable.receiptofgoods_icon,
					R.drawable.account_logistics_normal_white, R.drawable.complete_icon,
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
		if (new SharePreferenceUtil().getCookie() != null) {
			Intent intent = new Intent(getActivity(), OrderManagementActivity.class);
			intent.putExtra("id", position == 4 ? 0 : position + 1);
			startActivity(intent);
		} else {
			startActivity(LoginActivity.class);
		}
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.member_logo:// 头像
			startActivityForResult(new Intent(getActivity(), MemberDataActivity.class), 1);
			break;
		case R.id.sign_btn:
			startActivityForResult(new Intent(getActivity(), SignActivity.class), 0);
			break;
		case R.id.collection_btn:// 我的收藏
			StartAct(CollectionActivity.class);
			break;
		case R.id.chat:// 客服帮助
			startActivity(HelpActivity.class);
			break;
		case R.id.admin_address:// 收货地址
			StartAct(AddressActivity.class);
			break;
		case R.id.admin_setup:// 设置
			startActivity(SetupActivity.class);
			break;
		case R.id.score_btn:// 评分
			Score();
			break;
		case R.id.opinion_btn:
			startActivity(OpinionActivity.class);
			break;
		}
	}

	/**
	 * 跳转判断
	 * 
	 * @param cls
	 */
	private void StartAct(Class<?> cls) {
		if (new SharePreferenceUtil().getCookie() != null) {
			startActivity(cls);
		} else {
			startActivity(LoginActivity.class);
		}
	}

	/**
	 * 评分
	 */
	private void Score() {
		try {
			Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			e.getMessage();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		username.setText(UserBean.defaultShop().getNickname());
		user_integral.setText(UserBean.defaultShop().getIntegral().getIntegral());
		ImageDisplay.getSingleton().ImageLoader(member_logo, UserBean.defaultShop().getAvatar(), false);
		gender_icon.setBackgroundResource(GenderTool.getInstance().GednerStr(UserBean.defaultShop().getGender()));
	}
}