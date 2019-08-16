package com.abner.c1n.beans.dto.logs;

import org.apache.commons.lang3.StringUtils;

import com.abner.c1n.beans.dto.PageCondition;
import com.abner.c1n.utils.CalendarUtils;
import com.alibaba.fastjson.JSON;

/**
 * 日志查询基本条件
 * @author LW
 * @time 2019年6月7日 上午10:18:22
 */
public class LogCondition extends PageCondition{
	
	/**
	 * 链接id
	 */
	private String urlId;
	
	/**
	 * 域名
	 */
	private String domain;
	
	/**
	 * url
	 */
	private String url;
	
	/**
	 * 开始日期，格式yyyy-MM-dd
	 */
	public String startDate;
	
	/**
	 * 结束日期，格式yyyy-MM-dd
	 */
	public String endDate;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	
	public String getUrlId() {
		return urlId;
	}

	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}

	private LogCondition copyAndInit(){
		try{
			LogCondition clone = (LogCondition)this.clone();
			if(StringUtils.isEmpty(clone.getEndDate())){
				clone.endDate = CalendarUtils.currentTimeByDay();
			}
			return clone;
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 今天
	 */
	public LogCondition today(){
		LogCondition clone = copyAndInit();
		clone.startDate = clone.endDate;
		return clone;
	}

	/**
	 * 昨天
	 */
	public LogCondition yesterday(){
		LogCondition clone = copyAndInit();
		clone.startDate = CalendarUtils.formatTimeByDay(clone.endDate,-1);
		clone.endDate = clone.startDate;
		return clone;
	}
	
	/**
	 * 一周内
	 */
	public LogCondition week(){
		LogCondition clone = copyAndInit();
		clone.startDate = CalendarUtils.formatTimeByDay(clone.endDate,-6);
		return clone;
	}
	
	/**
	 * 一个月内
	 */
	public LogCondition month(){
		LogCondition clone = copyAndInit();
		clone.startDate = CalendarUtils.formatTimeByDay(clone.endDate,-29);
		return clone;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}	
	

}
