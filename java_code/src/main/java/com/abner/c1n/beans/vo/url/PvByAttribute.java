package com.abner.c1n.beans.vo.url;

import com.alibaba.fastjson.JSON;

/**
 * 属性对应的访问量
 * @author LW
 * @time 2019年6月8日 下午3:09:47
 */
public class PvByAttribute {
	
	private String name;
	
	private int pv;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPv() {
		return pv;
	}

	public void setPv(int pv) {
		this.pv = pv;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	

}
