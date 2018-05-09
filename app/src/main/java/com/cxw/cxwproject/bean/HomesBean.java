package com.cxw.cxwproject.bean;

import java.io.Serializable;
import java.util.List;

public class HomesBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String type;// 类型banner
	private HomeContent content;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public HomeContent getContent() {
		return content;
	}

	public void setContent(HomeContent content) {
		this.content = content;
	}

	/**
	 * 第二层内容展示
	 * 
	 * @author Administrator
	 *
	 */
	public class HomeContent implements Serializable {
		private static final long serialVersionUID = 1L;
		private List<Content> content;// ":[

		public List<Content> getContent() {
			return content;
		}

		public void setContent(List<Content> content) {
			this.content = content;
		}

		/**
		 * 第三层内容接收
		 */
		public class Content implements Serializable {
			private static final long serialVersionUID = 1L;
			private String tpl;// ": "80003",
			private List<HomeData> data;// ": [

			public String getTpl() {
				return tpl;
			}

			public void setTpl(String tpl) {
				this.tpl = tpl;
			}

			public List<HomeData> getData() {
				return data;
			}

			public void setData(List<HomeData> data) {
				this.data = data;
			}

			/**
			 * 第四层内容获取
			 */
			public class HomeData implements Serializable {
				private static final long serialVersionUID = 1L;
				private String title;// title
				private String img_url;
				private String slogan;// 头条关键字
				private Jump jump;
				private Params params;

				public String getTitle() {
					return title;
				}

				public void setTitle(String title) {
					this.title = title;
				}

				public Jump getJump() {
					return jump;
				}

				public String getSlogan() {
					return slogan;
				}

				public void setSlogan(String slogan) {
					this.slogan = slogan;
				}

				public String getImg_url() {
					return img_url;
				}

				public void setImg_url(String img_url) {
					this.img_url = img_url;
				}

				public void setJump(Jump jump) {
					this.jump = jump;
				}

				public Params getParams() {
					return params;
				}

				public void setParams(Params params) {
					this.params = params;
				}

				/**
				 * 第五层
				 * 
				 * @author Administrator
				 *
				 */
				public class Jump implements Serializable {
					private static final long serialVersionUID = 1L;
					private String needLogin;// ": 0,
					private String url;// ":
					private String activity_id;// ": "10100"

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

					public String getActivity_id() {
						return activity_id;
					}

					public void setActivity_id(String activity_id) {
						this.activity_id = activity_id;
					}

				}

				/**
				 * 第二个第五层
				 */
				public class Params implements Serializable {
					private static final long serialVersionUID = 1L;
					private String nation_id;// 民族id
					private String has_product;//是否有商品
					private String product_id;//商品id
					
					public String getNation_id() {
						return nation_id;
					}

					public void setNation_id(String nation_id) {
						this.nation_id = nation_id;
					}

					public String getHas_product() {
						return has_product;
					}

					public void setHas_product(String has_product) {
						this.has_product = has_product;
					}

					public String getProduct_id() {
						return product_id;
					}

					public void setProduct_id(String product_id) {
						this.product_id = product_id;
					}
					
				}
			}
		}
	}
}
