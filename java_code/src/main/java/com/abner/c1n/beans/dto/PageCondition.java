package com.abner.c1n.beans.dto;

import com.alibaba.fastjson.JSON;

/**
 * 分页条件
 * @author LW
 * @time 2019年6月7日 下午6:42:08
 */
public class PageCondition extends UserCondition{
	
	private int currentPage = 0;
	
	private int pageSize = 30;
	
	private int offset = 0;


	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		this.offset = this.currentPage*this.pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		this.offset = this.currentPage*this.pageSize;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	

}
