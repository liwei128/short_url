package com.abner.c1n.beans.dto.shorturl;

import com.abner.c1n.beans.po.UrlEntity;
import com.abner.c1n.beans.po.UrlEntity.Type;
import com.alibaba.fastjson.JSON;

/**
 * 短网址请求
 * @author 01383518
 * @date:   2019年6月6日 上午11:29:16
 */
public class LinkDto {
	
	/**
	 * 链接
	 */
	private String url;
	
	/**
	 * 类型
	 */
	private Type type;
	
	/**
	 * 指定key
	 */
	private String key;
	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	public LinkDto() {
		super();
	}

	public LinkDto(UrlEntity urlEntity) {
		super();
		this.url = urlEntity.getUrl();
		this.type = urlEntity.getType();
		this.key = urlEntity.getKey();
	}
	
	

}
