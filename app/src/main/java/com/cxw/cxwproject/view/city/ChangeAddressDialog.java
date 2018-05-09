package com.cxw.cxwproject.view.city;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cxw.cxwproject.R;
import com.cxw.cxwproject.dialog.base.BaseDialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/*
 * 城市区域选择对话框
 */
public class ChangeAddressDialog extends BaseDialog implements android.view.View.OnClickListener {

	private CartWheelView wvProvince;
	private CartWheelView wvCitys;
	private CartWheelView wvArea;
	private TextView btnSure;
	private TextView btnCancel;

	private Context context;
	private String[] mProvinceDatas;
	private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();// 选择市
	private Map<String, String[]> mTiaiDatasMap = new HashMap<String, String[]>();// 选择区
	private Map<String, String[]> marrCityids = new HashMap<String, String[]>();// 选择id
	private ArrayList<String> arrProvinces = new ArrayList<String>();
	private ArrayList<String> arrCitys = new ArrayList<String>();
	private ArrayList<String> arrAreas = new ArrayList<String>();
	private ArrayList<String> arrAreaids = new ArrayList<String>();
	private AddressTextAdapter provinceAdapter;
	private AddressTextAdapter cityAdapter;
	private AddressTextAdapter areaAdapter;

	private String strProvince = "上海";
	private String strCity = "上海市";
	private String strArea = "浦东新区";
	private String cityid;
	private OnAddressCListener onAddressCListener;

	private int maxsize = 15;
	private int minsize = 13;

	public ChangeAddressDialog(Context context) {
		super(context);
		this.context = context;
		initDialogView();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.dialog_changeaddress;
	}

	// 获取assets下文件json内容
	private String getFromAssets(String fileName) {
		String result = null;
		try {
			InputStream in = context.getResources().getAssets().open(fileName);
			// 获取文件的字节数
			int lenght = in.available();
			// 创建byte数组
			byte[] buffer = new byte[lenght];
			// 将文件中的数据读到byte数组中
			in.read(buffer);
			result = EncodingUtils.getString(buffer, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 实例化控件并绑定数据
	private void initDialogView() {
		wvProvince = (CartWheelView) findViewById(R.id.wv_address_province);// 省
		wvCitys = (CartWheelView) findViewById(R.id.wv_address_city);// 市
		wvArea = (CartWheelView) findViewById(R.id.wv_address_area);// 区
		btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
		btnCancel = (TextView) findViewById(R.id.btn_myinfo_cancel);
		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		initDatas();// 解析本地城市json
		initProvinces();

		provinceAdapter = new AddressTextAdapter(context, arrProvinces, getProvinceItem(strProvince), maxsize, minsize);
		wvProvince.setVisibleItems(5);
		wvProvince.setViewAdapter(provinceAdapter);
		wvProvince.setCurrentItem(getProvinceItem(strProvince));

		initCitys(mCitisDatasMap.get(strProvince));
		cityAdapter = new AddressTextAdapter(context, arrCitys, getCityItem(strCity), maxsize, minsize);
		wvCitys.setVisibleItems(5);
		wvCitys.setViewAdapter(cityAdapter);
		wvCitys.setCurrentItem(getCityItem(strCity));

		initAreas(mTiaiDatasMap.get(strCity), marrCityids.get(strCity));
		areaAdapter = new AddressTextAdapter(context, arrAreas, getAreaItem(strArea), maxsize, minsize);
		wvArea.setVisibleItems(5);
		wvArea.setViewAdapter(areaAdapter);
		wvArea.setCurrentItem(getAreaItem(strArea));

		wvProvince.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(CartWheelView wheel, int oldValue, int newValue) {
				String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
				strProvince = currentText;
				setTextviewSize(currentText, provinceAdapter);
				String[] citys = mCitisDatasMap.get(currentText);
				initCitys(citys);
				cityAdapter = new AddressTextAdapter(context, arrCitys, 0, maxsize, minsize);
				wvCitys.setVisibleItems(5);
				wvCitys.setViewAdapter(cityAdapter);
				wvCitys.setCurrentItem(0);

				String[] areas = mTiaiDatasMap.get(strCity);
				String[] areaids = marrCityids.get(strCity);
				initAreas(areas, areaids);
				areaAdapter = new AddressTextAdapter(context, arrAreas, 0, maxsize, minsize);
				wvArea.setVisibleItems(5);
				wvArea.setViewAdapter(areaAdapter);
				wvArea.setCurrentItem(0);
			}
		});

		wvProvince.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(CartWheelView wheel) {

			}

			@Override
			public void onScrollingFinished(CartWheelView wheel) {
				String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, provinceAdapter);
			}
		});

		wvCitys.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(CartWheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
				strCity = currentText;
				setTextviewSize(currentText, cityAdapter);
				String[] areas = mTiaiDatasMap.get(currentText);
				String[] areaids = marrCityids.get(currentText);
				initAreas(areas, areaids);
				areaAdapter = new AddressTextAdapter(context, arrAreas, 0, maxsize, minsize);
				wvArea.setVisibleItems(5);
				wvArea.setViewAdapter(areaAdapter);
				wvArea.setCurrentItem(0);
			}
		});

		wvCitys.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(CartWheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(CartWheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, cityAdapter);
			}
		});

		wvArea.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(CartWheelView wheel, int oldValue, int newValue) {
				String currentText = (String) areaAdapter.getItemText(wheel.getCurrentItem());
				strArea = currentText;
				cityid = arrAreaids.get(wheel.getCurrentItem()).toString();// 获得区域id
				setTextviewSize(currentText, areaAdapter);
			}
		});

		wvArea.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(CartWheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(CartWheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) areaAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, areaAdapter);
			}
		});
	}

	private class AddressTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected AddressTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize,
				int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
			this.list = list;
			setItemTextResource(R.id.tempValue);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index) + "";
		}
	}

	/**
	 * 设置字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	@SuppressWarnings("deprecation")
	public void setTextviewSize(String curriteItemText, AddressTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(maxsize);
				textvew.setTextColor(context.getResources().getColor(R.color.uli_bg));
			} else {
				textvew.setTextSize(minsize);
				textvew.setTextColor(context.getResources().getColor(R.color.uli_797979));
			}
		}
	}

	public void setAddresskListener(OnAddressCListener onAddressCListener) {
		this.onAddressCListener = onAddressCListener;
	}

	@Override
	public void onClick(View v) {
		if (v == btnSure) {
			if (onAddressCListener != null) {
				onAddressCListener.onClick(strProvince, strCity, strArea, cityid);
			}
		} else if (v == btnCancel) {

		} else {
			dismiss();
		}
		dismiss();
	}

	/**
	 * 回调接口
	 * 
	 * @author Administrator
	 * 
	 */
	public interface OnAddressCListener {
		public void onClick(String province, String city, String area, String cityid);
	}

	/**
	 * 解析数据
	 */
	private void initDatas() {
		try {
			JSONArray jsonArray = new JSONArray(getFromAssets("citydata.txt"));
			mProvinceDatas = new String[jsonArray.length()];
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonP = jsonArray.getJSONObject(i);
				String province = jsonP.getString("name");// 省
				mProvinceDatas[i] = province;
				JSONArray jsonCs = null;
				try {
					jsonCs = jsonP.getJSONArray("child");
				} catch (Exception e1) {
					continue;
				}
				String[] mCitiesDatas = new String[jsonCs.length()];
				for (int j = 0; j < jsonCs.length(); j++) {
					JSONObject jsonCity = jsonCs.getJSONObject(j);
					String city = jsonCity.getString("name");// 市
					mCitiesDatas[j] = city;
					JSONArray jsonAreas = null;
					try {
						jsonAreas = jsonCity.getJSONArray("child");
					} catch (Exception e) {
						continue;
					}
					String[] mAreasDatas = new String[jsonAreas.length()];
					String[] arrCityids = new String[jsonAreas.length()];
					for (int k = 0; k < jsonAreas.length(); k++) {
						String area = jsonAreas.getJSONObject(k).getString("name");// 区
						mAreasDatas[k] = area;
						arrCityids[k] = jsonAreas.getJSONObject(k).getString("code");// 区对应id
					}
					marrCityids.put(city, arrCityids);
					mTiaiDatasMap.put(city, mAreasDatas);
				}
				mCitisDatasMap.put(province, mCitiesDatas);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化省会
	 */
	public void initProvinces() {
		int length = mProvinceDatas.length;
		for (int i = 0; i < length; i++) {
			arrProvinces.add(mProvinceDatas[i]);
		}
	}

	/**
	 * 根据省会，生成该省会的所有城市
	 * 
	 * @param citys
	 */
	public void initCitys(String[] citys) {
		if (citys != null) {
			arrCitys.clear();
			int length = citys.length;
			for (int i = 0; i < length; i++) {
				arrCitys.add(citys[i]);
			}
		} else {
			String[] city = mCitisDatasMap.get("上海");
			arrCitys.clear();
			int length = city.length;
			for (int i = 0; i < length; i++) {
				arrCitys.add(city[i]);
			}
		}
		if (arrCitys != null && arrCitys.size() > 0 && !arrCitys.contains(strCity)) {
			strCity = arrCitys.get(0);
		}
	}

	/**
	 * 根据城市，生成该城市的所有区域
	 * 
	 * @param citys
	 */
	public void initAreas(String[] areas, String[] areaids) {
		if (areas != null) {
			arrAreaids.clear();
			arrAreas.clear();
			int length = areas.length;
			for (int i = 0; i < length; i++) {
				arrAreas.add(areas[i]);
				arrAreaids.add(areaids[i]);
			}
		} else {
			String[] city = mTiaiDatasMap.get("上海市");
			arrAreas.clear();
			arrAreaids.clear();
			int length = city.length;
			for (int i = 0; i < length; i++) {
				arrAreas.add(city[i]);
				arrAreaids.add(areaids[i]);
			}
		}
		if (arrAreas != null && arrAreas.size() > 0 && !arrAreas.contains(strArea)) {
			cityid = arrAreaids.get(0);// 获得区域id
			strArea = arrAreas.get(0);
			arrAreaids.add(areaids[0]);
		}
	}

	/**
	 * 初始化地点
	 * 
	 * @param province
	 * @param city
	 */
	public void setAddress(String province, String city, String area) {
		if (province != null && province.length() > 0) {
			this.strProvince = province;
		}
		if (city != null && city.length() > 0) {
			this.strCity = city;
		}
		if (area != null && area.length() > 0) {
			this.strArea = area;
		}
	}

	/**
	 * 返回省会索引，没有就返回默认“四川”
	 * 
	 * @param province
	 * @return
	 */
	public int getProvinceItem(String province) {
		int size = arrProvinces.size();
		int provinceIndex = 0;
		boolean noprovince = true;
		for (int i = 0; i < size; i++) {
			if (province.equals(arrProvinces.get(i))) {
				noprovince = false;
				return provinceIndex;
			} else {
				provinceIndex++;
			}
		}
		if (noprovince) {
			strProvince = "上海";
			return 22;
		}
		return provinceIndex;
	}

	/**
	 * 得到城市索引，没有返回默认“成都”
	 * 
	 * @param city
	 * @return
	 */
	public int getCityItem(String city) {
		int size = arrCitys.size();
		int cityIndex = 0;
		boolean nocity = true;
		for (int i = 0; i < size; i++) {
			if (city.equals(arrCitys.get(i))) {
				nocity = false;
				return cityIndex;
			} else {
				cityIndex++;
			}
		}
		if (nocity) {
			strCity = "上海市";
			return 0;
		}
		return cityIndex;
	}

	/**
	 * 得到区域索引，没有返回默认“南充”
	 * 
	 * @param city
	 * @return
	 */
	public int getAreaItem(String area) {
		int size = arrAreas.size();
		int cityIndex = 0;
		boolean nocity = true;
		for (int i = 0; i < size; i++) {
			if (area.equals(arrAreas.get(i))) {
				nocity = false;
				cityid = arrAreaids.get(i).toString();// 获得区域id
				return cityIndex;
			} else {
				cityIndex++;
			}
		}
		if (nocity) {
			strArea = "长宁区";
			return 0;
		}
		return cityIndex;
	}
}