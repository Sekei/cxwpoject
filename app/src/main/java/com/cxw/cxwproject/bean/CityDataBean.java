package com.cxw.cxwproject.bean;

import java.io.Serializable;
import java.util.List;

public class CityDataBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String code;// ": "110000",
	private String name;// ": "北京市",
	private List<TwoChild> child;// ":

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TwoChild> getChild() {
		return child;
	}

	public void setChild(List<TwoChild> child) {
		this.child = child;
	}

	public class TwoChild implements Serializable {
		private static final long serialVersionUID = 1L;
		private String code;// ": "110100",
		private String name;// ": "市辖区",
		private List<ThreeChild> child;// ": [

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<ThreeChild> getChild() {
			return child;
		}

		public void setChild(List<ThreeChild> child) {
			this.child = child;
		}

		public class ThreeChild implements Serializable {
			private static final long serialVersionUID = 1L;
			private String code;// ": "110101",
			private String name;// ": "东城区"
			public String getCode() {
				return code;
			}
			public void setCode(String code) {
				this.code = code;
			}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
		}
	}
}
