package com.abner.c1n.beans.po;

import com.abner.c1n.beans.bo.IpInfo;
import com.abner.c1n.beans.bo.RequestInfo;

/**
 * 日志实体
 * @author 01383518
 * @date:   2019年6月6日 下午6:36:06
 */
public class LogEntity extends BaseEntity{
	

	/**
	 * 链接
	 */
    private String url;

    /**
     * ip地址
     */
    private String ip;
    
    /**
     * 域名分类标签
     */
    private String domainTag;
    
    /**
     * 用户cookies
     */
    private String cookies;

    /**
     * 设备
     */
    private String device;

    /**
     * 浏览器
     */
    private String browser;

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

    /**
     * 来源
     */
    private String source;

    /**
     * 状态
     */
    private LogStatus status;
    
    public enum LogStatus{
    	/**
    	 * 成功
    	 */
    	SUCCESS,
    	
    	/**
    	 * 失败
    	 */
    	FAIL
    }


    public LogEntity(RequestInfo requestInfo) {
    	this.url = requestInfo.getUrl();
    	this.domainTag = requestInfo.getDomain();
    	this.cookies = requestInfo.getCookies();
    	this.browser = requestInfo.getBrowser();
    	this.device = requestInfo.getDevice();
    	this.source = requestInfo.getSource();
    	this.status= requestInfo.getStatus();
    	this.ip = requestInfo.getIp();
	}
    

	public void builderIpInfo(IpInfo ipInfo) {
		this.country = ipInfo.getCountry();
		this.province = ipInfo.getProvince();
		this.city = ipInfo.getCity();
		this.networkOperator = ipInfo.getNetworkOperator();
	}
    

	public LogEntity() {
		super();
	}


	public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

	public LogStatus getStatus() {
		return status;
	}

	public void setStatus(LogStatus status) {
		this.status = status;
	}

	public String getDomainTag() {
		return domainTag;
	}

	public void setDomainTag(String domainTag) {
		this.domainTag = domainTag;
	}

	public String getCookies() {
		return cookies;
	}

	public void setCookies(String cookies) {
		this.cookies = cookies;
	}
	


}