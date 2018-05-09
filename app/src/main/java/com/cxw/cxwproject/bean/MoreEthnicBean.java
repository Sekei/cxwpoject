package com.cxw.cxwproject.bean;

import java.io.Serializable;
import java.util.List;

public class MoreEthnicBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String sortName;// ":"A",
	private List<ChildBean> child;

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public List<ChildBean> getChild() {
		return child;
	}

	public void setChild(List<ChildBean> child) {
		this.child = child;
	}

	public class ChildBean implements Serializable {
		private static final long serialVersionUID = 1L;
		private String nation_id;// ":"",
		private String name;// ":"",
		private String spell;// ":""

		public String getNation_id() {
			return nation_id;
		}

		public void setNation_id(String nation_id) {
			this.nation_id = nation_id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSpell() {
			return spell;
		}

		public void setSpell(String spell) {
			this.spell = spell;
		}
	}
}
