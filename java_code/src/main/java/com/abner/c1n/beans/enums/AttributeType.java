package com.abner.c1n.beans.enums;
/**
 * 
 * @author 01383518
 * @date:   2019年6月13日 下午3:10:01
 */
public enum AttributeType{
	/**
	 * 根据设备查询
	 */
	DEVICE("device",null),
	/**
	 * 根据浏览器查询
	 */
	BROWSER("browser",null),
	/**
	 * 根据国家查询
	 */
	COUNTRY("country",null),
	/**
	 * 根据国内省份查询
	 */
	PROVINCE("province","country = '中国'"),
	
	/**
	 * 根据市查询
	 */
	CITY("city","province = '%s'"),
	/**
	 * 根据网络运营商查询
	 */
	NETWORK("network_operator",null);
	/**
	 * 属性名
	 */
	private String name;
	
	/**
	 * 条件
	 */
	private String condition;

	public String getName() {
		return name;
	}

	public String getCondition() {
		return condition;
	}

	private AttributeType(String name, String condition) {
		this.name = name;
		this.condition = condition;
	}
	
}
