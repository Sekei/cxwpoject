package com.cxw.cxwproject.activity;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.AddressListBean;
import com.cxw.cxwproject.dialog.CustomDialog;
import com.cxw.cxwproject.dialog.CustomDialog.Builder;
import com.cxw.cxwproject.dialog.LoadingDialog;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.view.city.ChangeAddressDialog;
import com.cxw.cxwproject.view.city.ChangeAddressDialog.OnAddressCListener;
import com.cxw.cxwproject.view.loading.TriggerLoadingDialog;
import com.cxw.cxwproject.widget.ToastUtils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AddressAddActivity extends ActionBarActivity implements OnClickListener, OnAddressCListener {
	TextView address_city, delete_address;
	ImageView default_img;
	EditText et_shouhuoren, et_mobile, address;
	ChangeAddressDialog mChangeAddressDialog;
	int is_default = 0;// 是否设为默认 0.否 1.是
	String region_code, addressId;// 所在地区code--地址id（编辑使用）
	LinearLayout default_btn;

	int TAG;// 判断是新建还是编辑
	AddressListBean mData; // 获取实体类

	LoadingDialog loading;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_addressadd;
	}

	@Override
	public int getActionBarType() {
		return ACTIONBAR_NOT_SHOW;
	}

	/**
	 * 头部显示
	 */
	private String TitleStr() {
		return TAG == 0 ? "新增地址" : "编辑地址";
	}

	@Override
	protected void initView() {
		TAG = getIntent().getIntExtra("TAG", 0);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(TitleStr());
		TextView right_btn = (TextView) findViewById(R.id.right_btn);
		right_btn.setVisibility(View.VISIBLE);
		right_btn.setText("保存");
		right_btn.setOnClickListener(this);
		// 返回
		ImageView back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		// 导入电话号码和联系人
		RelativeLayout import_btn = (RelativeLayout) findViewById(R.id.import_btn);
		import_btn.setOnClickListener(this);
		// 收货人--电话号码---地址详情
		et_shouhuoren = (EditText) findViewById(R.id.et_shouhuoren);
		et_mobile = (EditText) findViewById(R.id.et_mobile);
		address = (EditText) findViewById(R.id.address);
		// 城市选择
		mChangeAddressDialog = new ChangeAddressDialog(this);
		mChangeAddressDialog.setAddresskListener(this);
		address_city = (TextView) findViewById(R.id.address_city);
		address_city.setOnClickListener(this);
		// 默认地址设置
		default_btn = (LinearLayout) findViewById(R.id.default_btn);
		default_btn.setOnClickListener(this);
		default_img = (ImageView) findViewById(R.id.default_img);
		// 删除收货地址---删除
		delete_address = (TextView) findViewById(R.id.delete_address);
		delete_address.setOnClickListener(this);
		// 编辑地址时------数据绑定
		initData();
	}

	/**
	 * 地址编辑保存
	 */
	private void initData() {
		if (TAG != 0) {
			// 控件隐藏
			default_btn.setVisibility(View.GONE);// 默认地址选择
			delete_address.setVisibility(View.VISIBLE);// 删除控件
			// 控件赋值
			mData = (AddressListBean) getIntent().getSerializableExtra("DATA");
			et_shouhuoren.setText(mData.getName());// 收货人
			et_mobile.setText(mData.getPhone());// 电话号码
			address_city.setText(mData.getArea());// 所在城市
			address.setText(mData.getAddress());// 信息地址
			region_code = mData.getCode();// 区域id
			is_default = mData.getIs_default();// 是否是默认地址
			addressId = mData.getId();
		}
	}

	/**
	 * 城市选择回调
	 */
	@Override
	public void onClick(String province, String city, String area, String cityid) {
		address_city.setText(province + city + area);
		region_code = cityid;
	}

	/**
	 * 点击事件
	 * 
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:// 返回
			finish();
			break;
		case R.id.right_btn:// 提交保存地址
			Submit();
			// 这也是一个效果，后续优化使用
			// TriggerLoadingDialog.getInstance().show3(AddressAddActivity.this);
			break;
		case R.id.address_city:// 所在地址选择
			mChangeAddressDialog.show();
			break;
		case R.id.import_btn:// 导出电话号码联系人
			startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 0);
			break;
		case R.id.default_btn:// 默认地址设置
			Default_view();
			break;
		case R.id.delete_address:// 删除地址
			AddressRemove();
			break;

		default:
			break;
		}

	}

	/**
	 * 是否设置默认收货地址
	 */
	private void Default_view() {
		if (is_default == 0) {
			is_default = 1;
			default_img.setBackgroundResource(R.drawable.checkbox);
		} else {
			is_default = 0;
			default_img.setBackgroundResource(R.drawable.checkbox_cart_empty);
		}
	}

	/**
	 * 新增地址保存
	 */
	private void Submit() {
		loading = new LoadingDialog(this);
		// 判断是编辑----新建
		if (TAG != 0) {
			Api.Modify(addressId, et_shouhuoren.getText().toString(), "F", et_mobile.getText().toString(), region_code,
					address_city.getText().toString(), address.getText().toString(), is_default,
					new OnResponse<String>() {
						@Override
						public void onResponse(String o) {
							loading.dismiss();
							setResult(0, getIntent());
							finish();
						}

						@Override
						public void onErrorResponse(String error) {
							loading.dismiss();
							ToastUtils.show(error);
						}
					});
		} else {
			Api.AddressAdd(et_shouhuoren.getText().toString(), "F", et_mobile.getText().toString(), region_code,
					address_city.getText().toString(), address.getText().toString(), is_default,
					new OnResponse<String>() {
						@Override
						public void onResponse(String o) {
							loading.dismiss();
							setResult(0, getIntent());
							finish();
						}

						@Override
						public void onErrorResponse(String error) {
							loading.dismiss();
							ToastUtils.show(error);
						}
					});
		}
	}

	/**
	 * 删除收货地址
	 */
	private void remove() {
		loading = new LoadingDialog(this);
		Api.remove(addressId, new OnResponse<String>() {
			@Override
			public void onResponse(String o) {
				loading.dismiss();
				ToastUtils.show(o);
				setResult(0, getIntent());
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
	 * 删除地址提示框
	 */
	private void AddressRemove() {
		// 提示是否删除地址
		CustomDialog.Builder builder = new Builder(AddressAddActivity.this);
		builder.setTitle("温馨提示");
		builder.setMessage("此操作将删除当前收货地址？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				// 关闭当前页面---反馈数据--
				dialog.dismiss();
				remove();
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

	/*
	 * 获取手机联系人回调(non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			// ContentProvider展示数据类似一个单个数据库表
			// ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据
			try {
				ContentResolver reContentResolverol = getContentResolver();
				// URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
				Uri contactData = data.getData();
				// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
				@SuppressWarnings("deprecation")
				Cursor cursor = managedQuery(contactData, null, null, null, null);
				cursor.moveToFirst();
				// 获得DATA表中的名字
				String username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				// 条件为联系人ID
				String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
				// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
				Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
				while (phone.moveToNext()) {
					String usernumber = phone
							.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					et_shouhuoren.setText(username);// 收货人
					et_mobile.setText(usernumber);// 手机号
				}
			} catch (Exception e) {
				CustomDialog.Builder builder = new Builder(AddressAddActivity.this);
				builder.setTitle("温馨提示");
				builder.setMessage("读取手机联系人失败，可能尘晓屋已被禁止该权限或手机号为空。请检查读取联系人权限是否关闭？");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.dismiss();
						try {
							Intent localIntent = new Intent();
							localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							if (Build.VERSION.SDK_INT >= 9) {
								localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
								localIntent.setData(Uri.fromParts("package", getPackageName(), null));
							} else if (Build.VERSION.SDK_INT <= 8) {
								localIntent.setAction(Intent.ACTION_VIEW);
								localIntent.setClassName("com.android.settings",
										"com.android.settings.InstalledAppDetails");
								localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
							}
							startActivity(localIntent);
						} catch (Exception e2) {
							e2.getMessage();
						}
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
		}
	}
}
