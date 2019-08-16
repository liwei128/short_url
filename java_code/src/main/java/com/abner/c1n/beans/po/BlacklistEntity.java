package com.abner.c1n.beans.po;


/**
 * 黑名单
 * @author 01383518
 * @date:   2019年8月6日 下午3:22:01
 */
public class BlacklistEntity extends BaseEntity{

    private String domain;

    private BlacklistStatus status;
    
    public enum BlacklistStatus{
    	/**
    	 * 启用
    	 */
    	NORMAL,
    	
    	/**
    	 * 禁用
    	 */
    	DISABLED
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

	public BlacklistEntity(String domain, BlacklistStatus status) {
		super();
		this.domain = domain;
		this.status = status;
	}

	public BlacklistEntity() {
		super();
	}
    
    

}