package com.abner.c1n.beans.vo.url;

import org.apache.commons.lang3.StringUtils;

import com.abner.c1n.beans.dto.logs.LogCondition;
import com.alibaba.fastjson.JSON;

/**
 * 访问量统计
 * @author LW
 * @time 2019年6月8日 下午2:58:13
 */
public class Pv {
	
	/**
	 * 访问量
	 */
	private int pv;
	
	/**
	 * ip数量
	 */
	private int ipCount;
	
	/**
	 * 用户数量
	 */
	private int userCount;
	
	/**
	 * 网址
	 */
	private String url;
	
	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getPv() {
		return pv;
	}

	public void setPv(int pv) {
		this.pv = pv;
	}

	public int getIpCount() {
		return ipCount;
	}

	public void setIpCount(int ipCount) {
		this.ipCount = ipCount;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public void initUrl(LogCondition condition) {
		if(StringUtils.isNotEmpty(condition.getDomain())){
			this.url = condition.getDomain();
		}
		if(StringUtils.isNotEmpty(condition.getUrl())){
			this.url = condition.getUrl();
		}
		
	}
	
	

}
