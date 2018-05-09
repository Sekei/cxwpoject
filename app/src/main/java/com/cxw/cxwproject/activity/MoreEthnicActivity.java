package com.cxw.cxwproject.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.adapter.SideBarAdapter;
import com.cxw.cxwproject.bean.ContactsModel;
import com.cxw.cxwproject.bean.MoreEthnicBean;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.util.SideBar;
import com.cxw.cxwproject.util.SideBar.OnCurrentLetterListener;
import com.cxw.cxwproject.util.mvc.MVCHelper;
import com.cxw.cxwproject.util.mvc.MVCHelper.RequestListener;
import com.cxw.cxwproject.util.mvc.MVCViewHelper;
import com.cxw.cxwproject.widget.EmptyLayout;
import com.cxw.cxwproject.widget.MySectionIndexer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import opensource.jpinyin.PinyinHelper;

public class MoreEthnicActivity extends ActionBarActivity
		implements OnClickListener, RequestListener, OnCurrentLetterListener, OnItemClickListener {
	private ListView list_data;
	private SideBar sideBar;
	private SideBarAdapter myAdapter;
	// 用于进行字母表分组
	private MySectionIndexer<ContactsModel> indexer;
	private List<String> getLetter = new ArrayList<String>();
	private TextView txtShowCurrentLetter;

	private MVCViewHelper<List<MoreEthnicBean>> mvcViewHelper;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_moreethnic;
	}

	@Override
	public int getActionBarType() {
		return ACTIONBAR_NOT_SHOW;
	}

	@Override
	protected void initView() {
		super.initView();
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("民族分类");
		ImageView back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		list_data = (ListView) findViewById(R.id.list_data);
		sideBar = (SideBar) findViewById(R.id.side_bar);
		sideBar.setOnCurrentLetterListener(this);// 回调接口
		txtShowCurrentLetter = (TextView) findViewById(R.id.txt_show_current_letter);
		List<ContactsModel> list = initData();
		chineseToPinyin(list);
		// 将联系人列表的标题字母排序
		Collections.sort(list, new Comparator<ContactsModel>() {
			@Override
			public int compare(ContactsModel lhs, ContactsModel rhs) {
				return lhs.getFirstLetter().compareTo(rhs.getFirstLetter());
			}
		});
		// 将联系人列表的标题字母放到List<String>列表中，准备数据去重
		for (int i = 0; i < list.size(); i++) {
			getLetter.add(list.get(i).getFirstLetter());
		}
		// 数据去重
		getLetter = removeDuplicate(getLetter);
		// 将联系人列表的字母标题排序
		Collections.sort(getLetter, new Comparator<String>() {
			@Override
			public int compare(String lhs, String rhs) {
				return lhs.compareTo(rhs);
			}
		});
		// 设置已排序好的标题
		sideBar.setLetter(getLetter);
		indexer = new MySectionIndexer<ContactsModel>(list, getLetter);
		myAdapter = new SideBarAdapter(this, list, R.layout.adapter_side_bar);
		list_data.setAdapter(myAdapter);

		// 网络请求加载显示页面
		EmptyLayout emptyLayout = (EmptyLayout) findViewById(R.id.empty_view);
		mvcViewHelper = MVCHelper.createViewHelper(null, emptyLayout);
		mvcViewHelper.setRequestListener(this);
		mvcViewHelper.refresh();
	}

	/**
	 * 将中文转化为拼音
	 */
	@SuppressLint("DefaultLocale")
	public void chineseToPinyin(List<ContactsModel> list) {
		for (int i = 0; i < list.size(); i++) {
			ContactsModel contactsModel1 = list.get(i);
			// 将汉字转换为拼音
			String pinyinString = PinyinHelper.getShortPinyin(list.get(i).getName());
			// 将拼音字符串转换为大写拼音
			String upperCasePinyinString = String.valueOf(pinyinString.charAt(0)).toUpperCase();
			// 获取大写拼音字符串的第一个字符
			char tempChar = upperCasePinyinString.charAt(0);
			if (tempChar < 'A' || tempChar > 'Z') {
				contactsModel1.setFirstLetter("#");
			} else {
				contactsModel1.setFirstLetter(String.valueOf(tempChar));
			}
		}
	}

	/**
	 * 初始化数据
	 */
	public List<ContactsModel> initData() {
		List<ContactsModel> list = new ArrayList<ContactsModel>();
		ContactsModel contactsModel;
		String[] nameStrings = { "汉族", "蒙古族", "回族", "藏族", "维吾尔族", "苗族", "彝族", "壮族", "布依族", "朝鲜族", "满族", "侗族", "瑶族",
				"白族", "土家族", "哈尼族", "哈萨克族", "傣族", "黎族", "僳僳族", "佤族", "畲族", "高山族", "拉祜族", "水族", "东乡族", "纳西族", "景颇族",
				"柯尔克孜族", "土族", "达斡尔族", "仫佬族", "羌族", "布朗族", "撒拉族", "毛南族", "仡佬族", "锡伯族", "阿昌族", "普米族", "塔吉克族", "怒族",
				"乌孜别克族", "俄罗斯族", "鄂温克族", "德昂族", "保安族", "裕固族", "京族", "塔塔尔族", "独龙族", "鄂伦春族", "赫哲族", "门巴族", "珞巴族", "基诺族" };
		for (int i = 0; i < nameStrings.length; i++) {
			contactsModel = new ContactsModel();
			contactsModel.setName(nameStrings[i]);
			list.add(contactsModel);
		}
		return list;
	}

	/**
	 * 去重数据
	 */
	public <T> List<T> removeDuplicate(List<T> list) {
		Set<T> h = new HashSet<T>(list);
		list.clear();
		list.addAll(h);
		return list;

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		startActivity(new Intent(MoreEthnicActivity.this, DetailsActivity.class));
	}

	@Override
	public void showCurrentLetter(String currentLetter) {
		txtShowCurrentLetter.setVisibility(View.VISIBLE);
		txtShowCurrentLetter.setText(currentLetter);
		int position = myAdapter.getCurrentLetterPosition(currentLetter);
		if (position != -1) {
			list_data.setSelection(position);// listview头部嵌套布局，顾此处+1
		}
	}

	@Override
	public void hideCurrentLetter() {
		txtShowCurrentLetter.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}

	}

	@Override
	public void requestData(int page) {
		Api.getMoreEthnic(new OnResponse<List<MoreEthnicBean>>() {
			@Override
			public void onResponse(List<MoreEthnicBean> o) {
				System.out.println("==========>>>"+o.get(0).getSortName());
				mvcViewHelper.executeOnLoadDataSuccess(o);
			}

			@Override
			public void onErrorResponse(String error) {
				mvcViewHelper.executeOnLoadDataFail(error);
			}
		});
	}
}
