package com.cxw.cxwproject.tool;

import java.util.HashMap;
import java.util.List;

import com.cxw.cxwproject.adapter.CartAdapter;
import com.cxw.cxwproject.bean.ShoppingCartBean;

import android.os.Handler;

/**
 * 购物车计算相关
 * 
 * @author Administrator
 *
 */
public class CartTool {
	/* 持有私有静态实例，防止被引用，此处赋值为null，目的是实现延迟加载 */
	private static CartTool instance = null;

	/* 私有构造方法，防止被实例化 */
	private CartTool() {
	}

	/* 1:懒汉式，静态工程方法，创建实例 */
	public static CartTool getInstance() {
		if (instance == null) {
			instance = new CartTool();
		}
		return instance;
	}

	/**
	 * 全选
	 */
	public void AllCheck(List<ShoppingCartBean> listItems, CartAdapter adapter, boolean ischecked, Handler handler) {
		for (int i = 0; i < listItems.size(); i++) {
			adapter.getIsSelected().put(i, ischecked);
			ShoppingCartBean bean = listItems.get(i);
			bean.setChoosed(ischecked);
		}
		handler.sendMessage(handler.obtainMessage(10, getTotalPrice(listItems)));
		handler.sendMessage(handler.obtainMessage(13, getShopNumber(listItems)));
		adapter.notifyDataSetChanged();
	}

	/**
	 * 多少数量商品
	 */
	public int getShopNumber(List<ShoppingCartBean> listItems) {
		ShoppingCartBean bean = null;
		int totalPrice = 0;
		for (int i = 0; i < listItems.size(); i++) {
			bean = listItems.get(i);
			if (bean.isChoosed()) {
				totalPrice += bean.getNumber();
			}
		}
		return totalPrice;
	}

	/**
	 * 计算选中商品的金额
	 * 
	 * @return 返回需要付费的总金额
	 */
	public float getTotalPrice(List<ShoppingCartBean> listItems) {
		ShoppingCartBean bean = null;
		float totalPrice = 0;
		for (int i = 0; i < listItems.size(); i++) {
			bean = listItems.get(i);
			if (bean.isChoosed()) {
				totalPrice += bean.getNumber() * bean.getProduct().getPrice();
			}
		}
		return (float) (Math.round(totalPrice * 100)) / 100;
	}

	/**
	 * 判断是否购物车中所有的商品全部被选中
	 * 
	 * @return true所有条目全部被选中 false还有条目没有被选中
	 */
	public boolean isAllSelected(List<ShoppingCartBean> listItems, HashMap<Integer, Boolean> getIsSelected) {
		boolean flag = true;
		for (int i = 0; i < listItems.size(); i++) {
			if (!getIsSelected.get(i)) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	/**
	 * 购买或删除商品
	 * 
	 * @return
	 */
	public String deleteOrCheckOutShop(List<ShoppingCartBean> listItems, CartAdapter adapter) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < listItems.size(); i++) {
			if (adapter.getIsSelected().get(i)) {
				sb.append(listItems.get(i).getChart_id());// 购物车id
				sb.append(",");
			}
		}
		if (sb.toString().equals("") || sb.toString() == null) {
		} else {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 购买传值
	 * 
	 * @param listItems
	 * @param adapter
	 * @return
	 */
	public String[] BuyOrCheckOutShop(List<ShoppingCartBean> listItems, CartAdapter adapter) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < listItems.size(); i++) {
			if (adapter.getIsSelected().get(i)) {
				sb.append(i);
				sb.append(",");
			}
		}
		if (sb.toString().equals("") || sb.toString() == null) {
		} else {
			sb.deleteCharAt(sb.length() - 1);
		}

		return sb.toString().split(",");
	}

}
