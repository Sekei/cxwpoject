package com.cxw.cxwproject.http;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.cxw.cxwproject.AppConfig;
import com.cxw.cxwproject.bean.GoodsBean;
import com.cxw.cxwproject.bean.UserBean;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.tool.CxwMD5;
import com.google.gson.Gson;

import android.text.TextUtils;

public class Api {
	public static String URL = AppConfig.BASE_URL;
	public static int PAGE_SIZE = 20;// 每页的数据，如果分页数据默认与此数不同在使用自动分页（MVCListView时需要调用setPageSize()方法，请知晓，不然会导致分页的判断错误）
	/**
	 * 密码登录接口
	 * 
	 * @param mobile
	 * @param password
	 * @param l
	 */
	public static <T> void login(String mobile, String password, OnResponse<T> l) {
		if (TextUtils.isEmpty(mobile)) {
			l.onErrorResponse("手机号不能为空");
			return;
		} else if (TextUtils.isEmpty(password)) {
			l.onErrorResponse("密码不能为空");
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20001);
		map.put("account", mobile);
		map.put("type", 0);// 0表示密码登录、1表示验证码登录
		// 密码使用MD5简单加密处理
		try {
			map.put("password", CxwMD5.getMD5(password));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 手机验证码快速登录
	 * 
	 * @param mobile
	 * @param code
	 * @param l
	 */
	public static <T> void CodeLogin(String mobile, String code, OnResponse<T> l) {
		if (TextUtils.isEmpty(mobile)) {
			l.onErrorResponse("手机号不能为空");
			return;
		} else if (TextUtils.isEmpty(code)) {
			l.onErrorResponse("验证码不能为空");
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20001);
		map.put("account", mobile);
		map.put("type", 1);// 0表示密码登录、1表示验证码登录
		map.put("vercode", code);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 注册
	 * 
	 * @param mobile
	 * @param code
	 * @param password
	 * @param l
	 */
	public static <T> void regist(String mobile, String code, String password, OnResponse<T> l) {
		if (TextUtils.isEmpty(mobile)) {
			l.onErrorResponse("手机号不能为空");
			return;
		} else if (TextUtils.isEmpty(code)) {
			l.onErrorResponse("验证码不能为空");
			return;
		} else if (TextUtils.isEmpty(password)) {
			l.onErrorResponse("密码不能为空");
			return;
		} else if (password.length() < 6) {
			l.onErrorResponse("密码不能小于6位");
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20000);
		map.put("account", mobile);
		map.put("vercode", code);
		// 密码使用MD5简单加密处理
		try {
			map.put("password", CxwMD5.getMD5(password));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 密码重置
	 * 
	 * @param mobile
	 * @param code
	 * @param password
	 * @param l
	 */
	public static <T> void modify(String mobile, String code, String password, OnResponse<T> l) {
		if (TextUtils.isEmpty(mobile)) {
			l.onErrorResponse("手机号不能为空");
			return;
		} else if (TextUtils.isEmpty(code)) {
			l.onErrorResponse("验证码不能为空");
			return;
		} else if (TextUtils.isEmpty(password)) {
			l.onErrorResponse("密码不能为空");
			return;
		} else if (password.length() < 6) {
			l.onErrorResponse("密码不能小于6位");
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20004);
		map.put("account", mobile);
		map.put("vercode", code);
		// 密码使用MD5简单加密处理
		try {
			map.put("password", CxwMD5.getMD5(password));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 获取验证码接口
	 * 
	 * @param mobile
	 * @param module
	 * @param l
	 */
	public static <T> void code(String mobile, int module, OnResponse<T> l) {
		if (TextUtils.isEmpty(mobile)) {
			l.onErrorResponse("手机号不能为空");
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 10000);
		map.put("account", mobile);
		map.put("module", module);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 会员详情
	 * 
	 * @param l
	 */
	public static <T> void getShopDetail(OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20002);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 会员信息修改
	 * 
	 * @param avatarFile
	 * @param mName
	 * @param gender
	 * @param l
	 */
	public static <T> void getNickName(String sign, String avatarFile, String mName, String gender, OnResponse<T> l) {
		// -----比较特殊单独判断空---其它揭不判断---
		if (TextUtils.isEmpty(mName) && !TextUtils.isEmpty(sign)) {
			l.onErrorResponse("昵称不能为空");
			return;
		} else if (mName.equals(UserBean.defaultShop().getNickname()) && !TextUtils.isEmpty(sign)) {
			l.onErrorResponse("新的昵称不能和之前相同");
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20003);
		if (!TextUtils.isEmpty(avatarFile)) {
			map.put("avatarFile", avatarFile);
		}
		if (!TextUtils.isEmpty(mName)) {
			map.put("nickname", mName);
		}
		if (!TextUtils.isEmpty(gender)) {
			map.put("gender", gender);
		}
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 关注--取消关注同一接口，后台取反
	 * 
	 * @param l
	 */
	public static <T> void getFollow(String nationId, OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20005);
		map.put("nationId", nationId);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 民族列表
	 * 
	 * @param nationId
	 * @param l
	 */
	public static <T> void getMoreEthnic(OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20101);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 民族屋内容
	 * 
	 * @param nationId
	 * @param l
	 */
	public static <T> void getNational(String nationId, OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20102);
		map.put("nationId", nationId);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 积分界面数据请求
	 * 
	 * @param l
	 */
	public static <T> void getSign(OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20401);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 签到
	 * 
	 * @param l
	 */
	public static <T> void getSignBtn(OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20400);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 领取关注积分
	 * 
	 * @param type
	 * @param command
	 * @param l
	 */
	public static <T> void getGeneralized(int type, String command, OnResponse<T> l) {
		if (TextUtils.isEmpty(command)) {
			l.onErrorResponse("口令不能为空");
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20403);
		map.put("type", type);
		map.put("command", command);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 连续签到奖励
	 * 
	 * @param l
	 */
	public static <T> void getReward(OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20402);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 每日分享回调接口
	 * 
	 * @param l
	 */
	public static <T> void getDailyShare(OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20007);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 首页信息获取
	 * 
	 * @param l
	 */
	public static <T> void getHome(OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20200);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 新增收货地址
	 * 
	 * @param consignee
	 * @param gender
	 * @param mobile
	 * @param region_code
	 * @param address
	 * @param is_default
	 * @param l
	 */
	public static <T> void AddressAdd(String consignee, String gender, String mobile, String region_code, String area,
			String address, int is_default, OnResponse<T> l) {
		if (TextUtils.isEmpty(consignee)) {
			l.onErrorResponse("收货人不能为空");
			return;
		} else if (TextUtils.isEmpty(gender)) {
			l.onErrorResponse("请选择先生或女士");
			return;
		} else if (TextUtils.isEmpty(mobile)) {
			l.onErrorResponse("手机号不能为空");
			return;
		} else if (TextUtils.isEmpty(region_code)) {
			l.onErrorResponse("所在地区不能为空");
			return;
		} else if (TextUtils.isEmpty(address)) {
			l.onErrorResponse("详细地址不能为空");
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20901);
		map.put("name", consignee);
		map.put("gender", gender);
		map.put("phone", mobile);
		map.put("code", region_code);
		map.put("area", area);
		map.put("address", address);
		map.put("is_default", is_default);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 修改收货地址
	 * 
	 * @param id
	 * @param consignee
	 * @param gender
	 * @param mobile
	 * @param region_code
	 * @param area
	 * @param address
	 * @param is_default
	 * @param l
	 */
	public static <T> void Modify(String id, String consignee, String gender, String mobile, String region_code,
			String area, String address, int is_default, OnResponse<T> l) {
		if (TextUtils.isEmpty(consignee)) {
			l.onErrorResponse("收货人不能为空");
			return;
		} else if (TextUtils.isEmpty(gender)) {
			l.onErrorResponse("请选择先生或女士");
			return;
		} else if (TextUtils.isEmpty(mobile)) {
			l.onErrorResponse("手机号不能为空");
			return;
		} else if (TextUtils.isEmpty(region_code)) {
			l.onErrorResponse("所在地区不能为空");
			return;
		} else if (TextUtils.isEmpty(address)) {
			l.onErrorResponse("详细地址不能为空");
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20902);
		map.put("id", id);
		map.put("name", consignee);
		map.put("gender", gender);
		map.put("phone", mobile);
		map.put("code", region_code);
		map.put("area", area);
		map.put("address", address);
		map.put("is_default", is_default);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 收货地址列表
	 * 
	 * @param l
	 */
	public static <T> void AddressList(OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", "20900");
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 默认收货地址
	 * 
	 * @param l
	 */
	public static <T> void getDefaultAddress(OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", "20905");
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 删除收货地址
	 * 
	 * @param id
	 * @param l
	 */
	public static <T> void remove(String id, OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", "20903");
		map.put("id", id);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 设为默认地址
	 * 
	 * @param id
	 * @param l
	 */
	public static <T> void Default(String id, OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", "20904");
		map.put("id", id);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 店铺关注
	 * 
	 * @param nationId
	 * @param status关注:
	 *            0=取消关注,1=关注
	 * @param l
	 */
	public static <T> void Follow(String nationId, String status, OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20005);
		map.put("nationId", nationId);
		map.put("status", status);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 信息列表界面(已完成)
	 * 
	 * @param pageNo
	 * @param l
	 */
	public static <T> void NoticeList(int pageNo, OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20600);
		map.put("pageNo", pageNo);
		map.put("pageSize", PAGE_SIZE);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 已读
	 * 
	 * @param l
	 */
	public static <T> void getRead(String message_id, OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20602);
		map.put("message_id", message_id);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 民族特产商品列表
	 * 
	 * @param page
	 * @param nationId
	 * @param l
	 */
	public static <T> void getCommodity(String nationId, OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", "20103");
		map.put("nationId", nationId);
		// map.put("pageNo", page);
		// map.put("pageSize", PAGE_SIZE);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 热卖商品
	 * 
	 * @param l
	 */
	public static <T> void getBestSellers(int page, OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", "20701");
		map.put("pageNo", page);
		map.put("pageSize", PAGE_SIZE);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 民族咨询
	 * 
	 * @param nationId
	 * @param l
	 */
	public static <T> void getNews(String nationId, OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", "20104");
		map.put("nationId", nationId);
		// map.put("pageNo", page);
		// map.put("pageSize", PAGE_SIZE);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 商品详情
	 * 
	 * @param product_id
	 * @param l
	 */
	public static <T> void getDetails(String product_id, OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", "20700");
		map.put("product_id", product_id);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 下订单
	 * 
	 * @param address_id
	 * @param remark
	 * @param product
	 * @param l
	 * 
	 *            =============1.0.0版本下订单，目前废弃方法 public static <T> void
	 *            getBuyGood(String address_id, String remark, Map<String,
	 *            Object> product, OnResponse<T> l) { Gson gson = new Gson();
	 *            Map<String, Object> map = new HashMap<String, Object>();
	 *            map.put("sid", "20800"); map.put("address_id", address_id);
	 *            map.put("remark", remark); map.put("type", 0);// 0=立即购买,
	 *            1=购物车购买 map.put("product", "[" + gson.toJson(product) +
	 *            "]");//map转json格式 HttpRequest.post(URL, map, l); }
	 */
	/**
	 * 立即购买
	 * 
	 * @param address_id
	 * @param remark
	 * @param product_id
	 * @param number
	 * @param l
	 */
	public static <T> void getBuyGood(String address_id, String remark, String product_id, String number,
			OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20800);
		map.put("address_id", address_id);
		map.put("remark", remark);
		map.put("product_id", product_id);
		map.put("number", number);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 购物车购买
	 * 
	 * @param address_id
	 * @param remark
	 * @param mGoodsBean
	 * @param l
	 */
	public static <T> void getBuyCityGood(String address_id, String remark, List<GoodsBean> mGoodsBean,
			OnResponse<T> l) {
		StringBuffer sb = new StringBuffer();
		for (GoodsBean bean : mGoodsBean) {
			sb.append(bean.getProduct_id());
			sb.append(",");
		}
		if (sb.toString().equals("") || sb.toString() == null) {
		} else {
			sb.deleteCharAt(sb.length() - 1);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20809);
		map.put("address_id", address_id);
		map.put("remark", remark);
		map.put("chart_ids", sb.toString());
		System.out.println("===============购物车>>" + map);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 订单支付
	 * 
	 * @param order_id
	 * @param pay_type
	 * @param l
	 */
	public static <T> void getPayment(String order_id, int pay_type, OnResponse<T> l) {
		if (pay_type == -1) {
			l.onErrorResponse("请选择支付方式");
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20814);
		map.put("order_id", order_id);
		map.put("channel", pay_type == 0 ? "alipay" : "wx");
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 订单获取
	 * 
	 * @param l
	 */
	public static <T> void getOrderAll(int page, String state, OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20801);
		map.put("state", state);
		map.put("pageNo", page);
		map.put("pageSize", PAGE_SIZE);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 取消订单
	 * 
	 * @param order_id
	 * @param l
	 */
	public static <T> void getOrderCancel(String order_id, OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20802);
		map.put("order_id", order_id);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 订单删除
	 * 
	 * @param order_id
	 * @param l
	 */
	public static <T> void getOrderDelete(String order_id, OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20808);
		map.put("order_id", order_id);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 提醒发货
	 * 
	 * @param order_id
	 * @param l
	 */
	public static <T> void getOrderRemind(String order_id, OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20807);
		map.put("order_id", order_id);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 提醒接单
	 * 
	 * @param order_id
	 * @param l
	 */
	public static <T> void getReminderConnection(String order_id, OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20806);
		map.put("order_id", order_id);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 确认收货
	 * 
	 * @param order_id
	 * @param l
	 */
	public static <T> void getConfirmReceipt(String order_id, OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20803);
		map.put("order_id", order_id);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 订单详情
	 * 
	 * @param order_id
	 * @param l
	 */
	public static <T> void getOrderDetails(String order_id, OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20805);
		map.put("order_id", order_id);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 我的收藏
	 * 
	 * @param page
	 * @param l
	 */
	public static <T> void getCollection(int page, OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20006);
		map.put("pageNo", page);
		map.put("pageSize", PAGE_SIZE);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 取消收藏
	 * 
	 * @param nationId
	 * @param l
	 */
	public static <T> void getCancelCollection(String nationId, OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20005);
		map.put("nationId", nationId);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 检查版本更新
	 * 
	 * @param l
	 */
	public static <T> void VersionUpdate(OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 30000);
		map.put("type", "android");
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 购物车列表
	 * 
	 * @param l
	 */
	public static <T> void getShoppingCart(OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 21000);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 加入购物车
	 * 
	 * @param product_id
	 * @param l
	 */
	public static <T> void getJoinAShoppingCart(String product_id, OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 21001);
		map.put("product_id", product_id);
		map.put("number", 1);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 修改购物车
	 * 
	 * @param chart_id
	 * @param number
	 * @param l
	 */
	public static <T> void getModifyCart(String chart_id, int number, OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 21003);
		map.put("chart_id", chart_id);
		map.put("number", number);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 删除购物车
	 * 
	 * @param chart_ids
	 * @param l
	 */
	public static <T> void getDelete(String chart_ids, OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 21002);
		if (chart_ids.equals("")) {
			l.onErrorResponse("请选择要删除的商品");
			return;
		}
		map.put("chart_ids", chart_ids);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 评价
	 * 
	 * @param order_id
	 * @param product_id
	 * @param stars
	 * @param content
	 * @param l
	 */
	public static <T> void getEvaluate(String order_id, String product_id, String stars, String content,
			OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20810);
		if (stars.equals("0.0")) {
			l.onErrorResponse("请选择评星数量");
			return;
		}
		map.put("order_id", order_id);
		map.put("product_id", product_id);
		map.put("stars", stars);
		map.put("content", content);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 商品评价列表
	 * 
	 * @param page
	 * @param product_id
	 * @param state
	 * @param l
	 */
	public static <T> void getEvaluationList(int page, String product_id, int state, OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20811);
		map.put("product_id", product_id);
		map.put("state", state);
		map.put("pageNo", page);
		map.put("pageSize", PAGE_SIZE);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 广告请求
	 * 
	 * @param l
	 */
	public static <T> void getDeviceImg(OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 10100);
		map.put("device_type", "3");
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 意见反馈
	 * 
	 * @param l
	 */
	public static <T> void getOpinion(String content, OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 20603);
		if (content.equals("")) {
			l.onErrorResponse("请先填写意见内容");
			return;
		}
		map.put("content", content);
		HttpRequest.post(URL, map, l);
	}

	/**
	 * 帮助
	 * 
	 * @param l
	 */
	public static <T> void getHelp(OnResponse<T> l) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 31000);
		HttpRequest.post(URL, map, l);
	}
}
