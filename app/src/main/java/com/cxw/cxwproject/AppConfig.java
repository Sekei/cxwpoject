package com.cxw.cxwproject;

public class AppConfig {
	//测试服务器http://v.cxwworld.com/v3
	public static final String BASE = "http://r.cxwworld.com/v4";
	public static final String BASE_URL = BASE;
	// 图片路径访问
	public static final String ICON = "http://r.cxwworld.com/";
	//积分说明
	public static final String ExplainUrl="http://r.cxwworld.com/home/integral/explain_integral";
	//分享下载链接
	public static final String ShareUrl="http://r.cxwworld.com/home/download";
	// DEBUG控制log打印输出-----true/false
	public static final boolean DEBUG = false;
	public static final boolean DEBUG_LOG = true & DEBUG;// LOG日志输出开关
	public static final boolean DEBUG_API = true & DEBUG;// 网络错误显示级别控制开关
}
