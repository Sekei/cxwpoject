package com.cxw.cxwproject.data;

import java.util.ArrayList;
import java.util.List;

import com.cxw.cxwproject.adapter.CartAdapter;
import com.cxw.cxwproject.bean.DetailsBean;
import com.cxw.cxwproject.bean.GoodsBean;
import com.cxw.cxwproject.bean.ShoppingCartBean;
import com.cxw.cxwproject.bean.ShoppingCartBean.ProductBean;
import com.cxw.cxwproject.tool.CartTool;

public class BuyOrderData {

	/**
	 * 购物车购买Activity传值赋值
	 * 
	 * @param adapter
	 * @param listItems
	 * @return
	 */
	public static List<GoodsBean> GoodsData(CartAdapter adapter, List<ShoppingCartBean> listItems) {
		List<GoodsBean> mListGoods = new ArrayList<GoodsBean>();
		String[] deleteStr = CartTool.getInstance().BuyOrCheckOutShop(listItems, adapter);
		for (int i = 0; i < deleteStr.length; i++) {
			GoodsBean goods = new GoodsBean();
			ProductBean mpro = listItems.get(Integer.parseInt(deleteStr[i])).getProduct();
			goods.setImages(mpro.getImage());
			goods.setIntroduction(mpro.getAttribute());
			goods.setAttribute(mpro.getAttribute());
			goods.setName(mpro.getName());
			goods.setPrice(String.valueOf(mpro.getPrice()));
			goods.setProduct_id(listItems.get(Integer.parseInt(deleteStr[i])).getChart_id());
			goods.setStock(String.valueOf(listItems.get(Integer.parseInt(deleteStr[i])).getNumber()));
			goods.setSource("1");
			mListGoods.add(goods);
		}
		return mListGoods;
	}

	/**
	 * 商品详情直接购买Activity传值赋值
	 * 
	 * @param mdata
	 * @param Number
	 * @return
	 */
	public static List<GoodsBean> GoodsData(DetailsBean mdata, int Number) {
		List<GoodsBean> mListGoods = new ArrayList<GoodsBean>();
		GoodsBean goods = new GoodsBean();
		goods.setImages(mdata.getImages()[0]);
		goods.setIntroduction(mdata.getIntroduction());
		goods.setProduct_id(mdata.getProduct_id());
		goods.setName(mdata.getName());
		goods.setAttribute(mdata.getAttribute());
		goods.setPrice(String.valueOf(mdata.getPrice()));
		goods.setStock(String.valueOf(Number));
		goods.setSource("0");
		mListGoods.add(goods);
		return mListGoods;
	}

}
