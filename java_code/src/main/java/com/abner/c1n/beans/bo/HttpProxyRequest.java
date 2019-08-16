package com.abner.c1n.beans.bo;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.HttpMethod;

import com.alibaba.fastjson.JSON;

/**
 * http，支持跳转和cookies的请求
 * @author 01383518
 * @date:   2019年1月19日 下午5:29:29
 */
public class HttpProxyRequest {
	
	private static final int COUNT_LIMIT = 5;
	
	/**
	 * 重定向次数
	 */
	private AtomicInteger redirectCount = new AtomicInteger(0);
	
	/**
	 * url
	 */
	private String url;
	
	/**
	 * cookies
	 */
	private Map<String,String> cookies;
	
	/**
	 * 表单数据
	 */
	private Map<String, String> formData;
	
	/**
	 * JSON数据
	 */
	private String jsonData;
	
	/**
	 * httpMethod
	 */
	private HttpMethod httpMethod;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getCookies() {
		return cookies;
	}

	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}

	public Map<String, String> getFormData() {
		return formData;
	}

	public void setFormData(Map<String, String> formData) {
		this.formData = formData;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}
	
	
	public AtomicInteger getRedirectCount() {
		return redirectCount;
	}

	public void setRedirectCount(AtomicInteger redirectCount) {
		this.redirectCount = redirectCount;
	}
	

	public void checkRedirectCount(){
		if(redirectCount.incrementAndGet() > COUNT_LIMIT){
			throw new RuntimeException("重定向次数过多");
		}
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	
}
