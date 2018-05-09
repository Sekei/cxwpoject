package com.cxw.cxwproject.activity;

import java.util.List;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.AddressListBean;
import com.cxw.cxwproject.dialog.CustomDialog;
import com.cxw.cxwproject.dialog.CustomDialog.Builder;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.util.mvc.MVCHelper.RequestListener;
import com.cxw.cxwproject.util.mvc.MVCListViewHelper;
import com.cxw.cxwproject.widget.EmptyLayout;
import com.cxw.cxwproject.widget.ToastUtils;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AddressActivity extends ActionBarActivity implements OnItemClickListener,RequestListener, OnClickListener {
	private ListView address_list;
	private MVCListViewHelper<AddressListBean> mvcHelper;
	private QuickAdapter<AddressListBean> adapter;

	private static final int NEWLYADDED = 0;// 新增地址
	private static final int EDIT = 1;// 编辑地址

	// 数据保存
	private List<AddressListBean> ALLAddress;
	private AddressListBean mData;// 数据保存传值

	@Override
	protected int getLayoutId() {
		return R.layout.activity_address;
	}

	@Override
	public int getActionBarType() {
		return ACTIONBAR_NOT_SHOW;
	}

	@Override
	protected void initView() {
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("收货地址");
		TextView right_btn = (TextView) findViewById(R.id.right_btn);
		right_btn.setVisibility(View.VISIBLE);
		right_btn.setText("新增地址");
		right_btn.setOnClickListener(this);
		ImageView back = (ImageView) findViewById(R.id.back); // 返回
		back.setOnClickListener(this);
		address_list = (ListView) findViewById(R.id.address_list);
		address_list.setOnItemClickListener(this);
		adapter = new QuickAdapter<AddressListBean>(this, R.layout.address_item) {
			@SuppressWarnings("deprecation")
			@Override
			protected void convert(final BaseAdapterHelper helper, final AddressListBean List) {
				helper.setText(R.id.name, List.getName());
				helper.setText(R.id.phone, List.getPhone());
				helper.setText(R.id.address, List.getArea() + List.getAddress());
				if (List.getIs_default() == 0) {// 0.否 1.是
					helper.setText(R.id.default_title, "选择默认");
					helper.setTextColor(R.id.default_title, getResources().getColor(R.color.uli_797979));
					helper.setImageResource(R.id.default_icon, R.drawable.checkbox_cart_empty);
				} else {
					helper.setText(R.id.default_title, "默认地址");
					helper.setTextColor(R.id.default_title, getResources().getColor(R.color.uli_bg));
					helper.setImageResource(R.id.default_icon, R.drawable.checkbox);
				}
				// 地址----编辑
				helper.setOnClickListener(R.id.modify, new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 先绑定数据
						mData = new AddressListBean();
						mData = ALLAddress.get(helper.getPosition());
						// 跳转传值
						StartForResult(EDIT);
					}
				});
				// 地址----删除
				helper.setOnClickListener(R.id.delete, new OnClickListener() {
					@Override
					public void onClick(View v) {
						AddressRemove(helper.getPosition());
					}
				});
			}
		};
		EmptyLayout emptyLayout = (EmptyLayout) findViewById(R.id.empty_view);
		// 网络请求
		mvcHelper = MVCListViewHelper.createListHelper(null, address_list, emptyLayout);
		mvcHelper.setAdapter(adapter);
		mvcHelper.setRequestListener(this);
		mvcHelper.refresh();
	}

	/**
	 * 删除地址提示框
	 */
	private void AddressRemove(final int position) {
		// 提示是否删除地址
		CustomDialog.Builder builder = new Builder(AddressActivity.this);
		builder.setTitle("温馨提示");
		builder.setMessage("此操作将删除当前收货地址？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
				remove(ALLAddress.get(position).getId(), position);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_btn:
			StartForResult(NEWLYADDED);
			break;
		case R.id.back:
			finish();
			break;
		}
	}

	/**
	 * 跳转相关
	 */
	private void StartForResult(int WhoInt) {
		Intent intent = new Intent(AddressActivity.this, AddressAddActivity.class);
		intent.putExtra("TAG", WhoInt);
		if (WhoInt == EDIT) {// 编辑传值
			intent.putExtra("DATA", mData);
		}
		startActivityForResult(intent, WhoInt);
	}

	/**
	 * 回调地址
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case NEWLYADDED:// 新增---编辑都要刷新数据
			mvcHelper.refresh();// 刷新数据
			break;
		}
	}

	/**
	 * 网络请求
	 */
	@Override
	public void requestData(int page) {
		Api.AddressList(new OnResponse<List<AddressListBean>>() {
			@Override
			public void onResponse(List<AddressListBean> o) {
				ALLAddress = o;// 数据保存
				mvcHelper.executeOnLoadDataSuccess(o);
			}

			@Override
			public void onErrorResponse(String error) {
				mvcHelper.executeOnLoadDataFail(error);
			}
		});
	}

	/**
	 * 删除收货地址
	 */
	private void remove(String addressId, final int position) {
		Api.remove(addressId, new OnResponse<String>() {
			@Override
			public void onResponse(String o) {
				mvcHelper.refresh();// 刷新数据
			}
			@Override
			public void onErrorResponse(String error) {
				ToastUtils.show(error);
			}
		});
	}

	/**
	 * 设置默认地址
	 * 
	 * @param addressId
	 */
	private void Default(String addressId) {
		Api.Default(addressId, new OnResponse<String>() {
			@Override
			public void onResponse(String o) {
				ToastUtils.show("默认地址设置成功");
				mvcHelper.refresh();// 刷新数据
			}
			@Override
			public void onErrorResponse(String error) {
				ToastUtils.show(error);
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Default(ALLAddress.get(position).getId());
	}

}
