package com.abner.c1n.beans.bo;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
/**
 * ip信息
 * @author 01383518
 * @date:   2019年6月6日 下午6:35:17
 */
public class IpInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2210510954871449368L;

	/**
     * ip地址
     */
    private String ip;

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 网络运营商
     */
    private String networkOperator;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getNetworkOperator() {
		return networkOperator;
	}

	public void setNetworkOperator(String networkOperator) {
		this.networkOperator = networkOperator;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
    
    

}
