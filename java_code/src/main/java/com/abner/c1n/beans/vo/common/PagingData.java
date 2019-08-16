package com.abner.c1n.beans.vo.common;

import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * 分页结果
 * @author LW
 * @time 2019年6月7日 上午10:32:15
 */
public class PagingData<T> {
	
	/**
	 * 总页数
	 */
	private int pageTotal;
	
	/**
	 * 总记录数
	 */
	private int total;
	
	/**
	 * 数据列表
	 */
	private List<T> list;

	public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public static <T> PagingData<T> getInstance(int pageSize, int total, List<T> list) {
		PagingData<T> pagingData = new PagingData<>();
		pagingData.setList(list);
		pagingData.setTotal(total);
		int end = total%pageSize>0?1:0;
		pagingData.setPageTotal(total/pageSize+end);
		return pagingData;
	}
	
	

}
