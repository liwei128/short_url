package com.abner.c1n.beans.dto.monitorpage;

import com.abner.c1n.beans.dto.PageCondition;

/**
 * 监控页面查询条件
 * @author 01383518
 * @date:   2019年6月13日 下午2:04:06
 */
public class MonitorPageCondition extends PageCondition{
	
    /**
     * 页面地址
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
