package com.cxw.cxwproject.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class HomeBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String type;// 类型banner
	private int sortno;// 排序
	private int bottomMargin;// 距离下方多少dp
	private HomeContent content;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSortno() {
		return sortno;
	}

	public void setSortno(int sortno) {
		this.sortno = sortno;
	}

	public int getBottomMargin() {
		return bottomMargin;
	}

	public void setBottomMargin(int bottomMargin) {
		this.bottomMargin = bottomMargin;
	}

	public HomeContent getContent() {
		return content;
	}

	public void setContent(HomeContent content) {
		this.content = content;
	}

	/**
	 * 内容展示
	 * 
	 * @author Administrator
	 *
	 */
	class HomeContent implements Serializable {
		private static final long serialVersionUID = 1L;
		private int contentNum;// ":1,
		private List<Content> content;// ":[

		public int getContentNum() {
			return contentNum;
		}

		public void setContentNum(int contentNum) {
			this.contentNum = contentNum;
		}

		public List<Content> getContent() {
			return content;
		}

		public void setContent(List<Content> content) {
			this.content = content;
		}
		/**
		 * 第三层内容接收
		 */
		class Content implements Serializable {
			private static final long serialVersionUID = 1L;
			private String tpl;// ": "80003",
			//private List<Data> data;// ": [

			public String getTpl() {
				return tpl;
			}

			public void setTpl(String tpl) {
				this.tpl = tpl;
			}

//			public List<Data> getData() {
//				return data;
//			}

//			public void setData(List<Data> data) {
//				this.data = data;
//			}
		}

	}

	/**
	 * 第三层内容接收
	 */
	class Content implements Serializable {
		private static final long serialVersionUID = 1L;
		private String tpl;// ": "80003",
		//private List<Data> data;// ": [

		public String getTpl() {
			return tpl;
		}

		public void setTpl(String tpl) {
			this.tpl = tpl;
		}

//		public List<Data> getData() {
//			return data;
//		}

//		public void setData(List<Data> data) {
//			this.data = data;
//		}
	}

	/**
	 * 第四层内容获取
	 */
	class Data implements Serializable {
//		private static final long serialVersionUID = 1L;
//		private String type;// ": 0,
//		private String title;// u8fc7\u5269\u4ea7\u80fd",
//		private String image;// /web\/data\/setting\/20170411\/b187c95ad00437f73eed41414591a083.jpg",
//		private Jump jump;// ": {
//
//		public String getType() {
//			return type;
//		}
//
//		public void setType(String type) {
//			this.type = type;
//		}
//
//		public String getTitle() {
//			return title;
//		}
//
//		public void setTitle(String title) {
//			this.title = title;
//		}
//
//		public String getImage() {
//			return image;
//		}
//
//		public void setImage(String image) {
//			this.image = image;
//		}
//
//		public Jump getJump() {
//			return jump;
//		}
//
//		public void setJump(Jump jump) {
//			this.jump = jump;
//		}
	}
	/**
	 * 第五层
	 * @author Administrator
	 *
	 */
	class Jump implements Serializable {
		private static final long serialVersionUID = 1L;
		private String needLogin;// ": 0,
		private String url;// ":
		public String getNeedLogin() {
			return needLogin;
		}
		public void setNeedLogin(String needLogin) {
			this.needLogin = needLogin;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
	}

	private List<BannerBean> banner;// 广告
	private List<HotnationBean> hotnation;// 热推
	private List<NationalsBean> nations;// 名族列表

	public List<BannerBean> getBanner() {
		return banner;
	}

	public void setBanner(List<BannerBean> banner) {
		this.banner = banner;
	}

	public List<HotnationBean> getHotnation() {
		return hotnation;
	}

	public void setHotnation(List<HotnationBean> hotnation) {
		this.hotnation = hotnation;
	}

	public List<NationalsBean> getNations() {
		return nations;
	}

	public void setNations(List<NationalsBean> nations) {
		this.nations = nations;
	}

	public class BannerBean implements Serializable {
		private static final long serialVersionUID = 1L;
		private String type;// ":0,
		private String image;// ":"http://xxx.xxx",
		private String url;// ":"http://xxx.xx"

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

	}

	public class HotnationBean implements Serializable {
		private static final long serialVersionUID = 1L;
		private String id;// ":"",
		private String img_url;// ":"",
		private String name;// ":""
		private boolean has_product;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getImg_url() {
			return img_url;
		}

		public void setImg_url(String img_url) {
			this.img_url = img_url;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isHas_product() {
			return has_product;
		}

		public void setHas_product(boolean has_product) {
			this.has_product = has_product;
		}

	}

	public class NationalsBean implements Serializable {
		private static final long serialVersionUID = 1L;
		private String nation_id;// ":"",
		private String img_url;// ":""
		private String name;// ":""
		private boolean has_product;

		public String getNation_id() {
			return nation_id;
		}

		public void setNation_id(String nation_id) {
			this.nation_id = nation_id;
		}

		public String getImg_url() {
			return img_url;
		}

		public void setImg_url(String img_url) {
			this.img_url = img_url;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isHas_product() {
			return has_product;
		}

		public void setHas_product(boolean has_product) {
			this.has_product = has_product;
		}

	}

}
