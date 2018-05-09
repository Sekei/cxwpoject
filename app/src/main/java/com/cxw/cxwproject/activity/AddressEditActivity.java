package com.cxw.cxwproject.activity;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.view.city.ChangeAddressDialog;
import com.cxw.cxwproject.view.city.ChangeAddressDialog.OnAddressCListener;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AddressEditActivity extends ActionBarActivity implements OnAddressCListener {
	private TextView title, address_city;
	private ImageView back;
	ChangeAddressDialog mChangeAddressDialog;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_addressedit;
	}

	@Override
	public int getActionBarType() {
		return ACTIONBAR_NOT_SHOW;
	}

	@Override
	protected void initView() {
		title = (TextView) findViewById(R.id.title);
		title.setText("编辑地址");
		back = (ImageView) findViewById(R.id.back); // 返回
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 城市选择
		mChangeAddressDialog = new ChangeAddressDialog(this);
		mChangeAddressDialog.setAddresskListener(this);
		address_city = (TextView) findViewById(R.id.address_city);
		address_city.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mChangeAddressDialog.show();
			}
		});
	}

	@Override
	public void onClick(String province, String city, String area, String cityid) {
		// ((TextView) view).setText(province + city + area);
		// city_id = cityid;
	}

}
