package com.cxw.cxwproject.enums;

public class OderStateEnums {
	/**
	 * 订单状态
	 * 
	 * @param weekEnums
	 * @return
	 */
	public static String getWeek(int weekEnums) {
		String week = null;
		switch (weekEnums) {
		case 100000:
			week = "全部订单";
			break;
		case 100001:
			week = "待支付";
			break;
		case 100002:
			week = "待接单";
			break;
		case 100003:
			week = "待发货";
			break;
		case 100004:
			week = "待收货";
			break;
		case 100005:
			week = "等待评价";// 待评论 |
			break;
		case 100006:
			week = "已完成";
			break;
		case 100010:
			week = "已作废";
			break;
		case 100011:
			week = "已取消";
			break;
		}
		return week;
	}

	/**
	 * 按钮是否显示
	 * 
	 * @param weekEnums
	 * @return
	 */
	public static boolean getVisible(int weekEnums) {
		boolean week = true;
		switch (weekEnums) {
		case 100000:
		case 100001:
			week = true;
			break;
		case 100002:
		case 100003:
		case 100004:
		case 100005:
		case 100006:
		case 100010:
		case 100011:
			week = false;
			break;
		}
		return week;
	}

	/**
	 * 按钮名字
	 * 
	 * @param weekEnums
	 * @return
	 */
	public static String getName(int weekEnums) {
		String week = null;
		switch (weekEnums) {
		case 100000:
		case 100001:
			week = "立即支付";
			break;
		case 100002:
			week = "提醒接单";
			break;
		case 100003:
			week = "提醒发货";
			break;
		case 100004:
			week = "确认收货";
			break;
		case 100005:
			week = "等待评价";// 待评论 |
			break;
		case 100006:
		case 100010:
		case 100011:
			week = "删除订单";
			break;
		}
		return week;
	}

	/**
	 * 订单详情标签走向
	 */
	public static int getStep(int weekEnums) {
		int week = 1;
		switch (weekEnums) {
		case 100000:
		case 100001:
			week = 1;
			break;
		case 100002:
			week=2;
			break;
		case 100003:
			week=3;
			break;
		case 100004:
			week = 4;
			break;
		case 100005:
			week = 5;
			break;
		case 100006:
		case 100010:
		case 100011:
			week = 6;
			break;
		}
		return week;
	}
}
