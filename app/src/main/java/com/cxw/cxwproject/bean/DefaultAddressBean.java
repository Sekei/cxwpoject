package com.cxw.cxwproject.bean;

import java.io.Serializable;

public class DefaultAddressBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;// ":"5555555",
	private String name;// ":"张三的老哥",
	private String phone;// ":"13166383205",
	private String gender;// ":" |Y |string |F/M 性别 |",
	private String area;// ":"上海市长宁区",
	private String code;// ":"",
	private String address;// ":"金钟路968号凌空SOHO6号楼502室附加打法的沙发斯蒂芬阿凡达",
	private String is_default;// ":1

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIs_default() {
		return is_default;
	}

	public void setIs_default(String is_default) {
		this.is_default = is_default;
	}

}
