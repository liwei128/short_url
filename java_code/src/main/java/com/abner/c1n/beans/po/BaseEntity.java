package com.abner.c1n.beans.po;

import java.util.Date;

import com.alibaba.fastjson.JSON;

/**
 * 表基础信息
 * @author liwei
 * @date: 2018年9月28日 下午1:17:52
 *
 */
public class BaseEntity {
	
	private Long id;
	
	private Date updateTime;
	
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	

}
