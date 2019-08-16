package com.abner.c1n.beans.vo.blacklist;

import java.util.Date;

import com.abner.c1n.beans.po.BlacklistEntity;
import com.abner.c1n.beans.po.BlacklistEntity.BlacklistStatus;
import com.alibaba.fastjson.JSON;

/**
 * 黑名单
 * @author 01383518
 * @date:   2019年8月6日 下午4:04:14
 */
public class BlacklistVo {
	
	/**
	 * id
	 * 
	 */
	private Long id;
	
	/**
	 * 域名
	 */
    private String domain;

    /**
     * 状态
     */
    private BlacklistStatus status;
    
    /**
     * 创建时间
     */
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public BlacklistStatus getStatus() {
		return status;
	}

	public void setStatus(BlacklistStatus status) {
		this.status = status;
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

	public BlacklistVo(BlacklistEntity blacklistEntity) {
		super();
		this.id = blacklistEntity.getId();
		this.domain = blacklistEntity.getDomain();
		this.status = blacklistEntity.getStatus();
		this.createTime = blacklistEntity.getCreateTime();
	}

	public BlacklistVo() {
		super();
	}
	
	

}
