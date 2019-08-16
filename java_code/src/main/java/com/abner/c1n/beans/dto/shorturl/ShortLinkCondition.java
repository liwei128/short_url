package com.abner.c1n.beans.dto.shorturl;

import com.abner.c1n.beans.dto.PageCondition;
import com.abner.c1n.beans.po.UrlEntity.Status;

/**
 * 短网址查询条件
 * @author LW
 * @time 2019年6月7日 下午6:38:58
 */
public class ShortLinkCondition extends PageCondition{
	
	private String keyUrl;
	
    /**
     * 状态
     */
    private Status status;
	/**
	 * 开始日期，格式yyyy-MM-dd
	 */
	public String startDate;
	
	/**
	 * 结束日期，格式yyyy-MM-dd
	 */
	public String endDate;
	
    

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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getKeyUrl() {
		return keyUrl;
	}

	public void setKeyUrl(String keyUrl) {
		this.keyUrl = keyUrl;
	}
	
}
