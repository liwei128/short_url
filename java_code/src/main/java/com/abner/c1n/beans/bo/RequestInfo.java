package com.abner.c1n.beans.bo;

import java.io.Serializable;

import com.abner.c1n.beans.po.LogEntity.LogStatus;
import com.alibaba.fastjson.JSON;

/**
 * 请求信息
 * @author LW
 * @time 2019年6月22日 下午4:04:16
 */
public class RequestInfo implements Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 434066294522030431L;

	/**
     * ip地址
     */
    private String ip;
    
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
     * 来源
     */
    private String source;
    
    /**
     * url
     */
    private String url;
    /**
     * 域名
     */
    private String domain;
    
    /**
     * 状态
     */
    private LogStatus status;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCookies() {
		return cookies;
	}

	public void setCookies(String cookies) {
		this.cookies = cookies;
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
    
    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public LogStatus getStatus() {
		return status;
	}

	public void setStatus(LogStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
