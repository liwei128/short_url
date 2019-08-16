package com.abner.c1n.beans.po;

import com.abner.c1n.beans.constant.UserConstant;
import com.abner.c1n.beans.constant.UserThreadLocal;
import com.abner.c1n.beans.dto.shorturl.LinkDto;
import com.abner.c1n.config.SpringApplicationUtils;
import com.abner.c1n.config.SystemConfig;
/**
 * 短网址实体
 * @author 01383518
 * @date:   2019年6月6日 下午6:36:21
 */
public class UrlEntity extends BaseEntity{
	
	private Long userId;

    private String key;

    private Type type;
    
    private String shortUrl;

    private String url;
    
    /**
     * 状态
     */
    private Status status;
    
    public enum Status{
    	/**
    	 * 启用
    	 */
    	NORMAL,
    	
    	/**
    	 * 禁用
    	 */
    	DISABLED
    }
    
    public enum Type{
		/**
		 * 指定网址
		 */
		URI,
		
		/**
		 * 指定域名
		 */
		DOMAIN
	}

    public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public UrlEntity(LinkDto linkDto) {
		super();
		this.type = linkDto.getType();
    	updateKey(linkDto.getKey());
		this.url = linkDto.getUrl();
		this.status = Status.NORMAL;
		if(UserThreadLocal.isLogin()){
			this.userId = UserThreadLocal.getCurrentUser().getId();
		}else{
			this.userId = UserConstant.ADMIN_ID;
		}
		
	}
	
    public void updateKey(String key) {
        this.key = key;
        this.shortUrl = SpringApplicationUtils.getBean(SystemConfig.class).getUrlByKeyAndType(type, key);
    }

	public UrlEntity() {
		super();
	}
	
    
}