package com.cxw.cxwproject.bean;

import java.io.Serializable;

public class OrderDetailsBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private AddressBean address;// ":
	private String remark;// ":"备注信息"

	public AddressBean getAddress() {
		return address;
	}

	public void setAddress(AddressBean address) {
		this.address = address;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public class AddressBean implements Serializable {
		private static final long serialVersionUID = 1L;
		private String name;// ":"张三的老哥",
		private String phone;// ":"13166383205",
		private String gender;// ":" |Y |string |F/M 性别 |",
		private String address;// ":"金钟路968号凌空SOHO6号楼502室附加打法的沙发斯蒂芬阿凡达"
		private String area;// 城市市辖区

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getGender() {
			return gender;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}

		public String getAddress() {
			return  address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getArea() {
			return area;
		}

		public void setArea(String area) {
			this.area = area;
		}

	}
}
