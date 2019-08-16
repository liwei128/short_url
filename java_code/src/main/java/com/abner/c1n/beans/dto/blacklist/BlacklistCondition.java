package com.abner.c1n.beans.dto.blacklist;

import com.abner.c1n.beans.dto.PageCondition;
import com.abner.c1n.beans.po.BlacklistEntity.BlacklistStatus;

/**
 * 黑名单查询条件
 * @author 01383518
 * @date:   2019年8月6日 下午4:01:10
 */
public class BlacklistCondition extends PageCondition{
	
    private String domain;

    private BlacklistStatus status;

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
	
    
	
}
