package com.abner.c1n.beans.po;

import com.abner.c1n.beans.constant.UserConstant;
import com.abner.c1n.beans.constant.UserThreadLocal;

/**
 * 
 * @author 01383518
 * @date:   2019年6月13日 下午3:10:14
 */
public class MonitorPageEntity extends BaseEntity{

	/**
	 * 用户id
	 */
    private Long userId;

    /**
     * 类型
     */
    private PageType type;

    /**
     * 页面地址
     */
    private String url;
    
    public enum PageType{
		/**
		 * 指定网址
		 */
		URL,
		
		/**
		 * 指定域名
		 */
		DOMAIN
	}


    public MonitorPageEntity(PageType type, String url) {
		this.type = type;
		this.url = url;
		if(UserThreadLocal.isLogin()){
			this.userId = UserThreadLocal.getCurrentUser().getId();
		}else{
			this.userId = UserConstant.ADMIN_ID;
		}
	}
    

	public MonitorPageEntity() {
		super();
	}


	public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public PageType getType() {
        return type;
    }

    public void setType(PageType type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

	public void setUrl(String url) {
		this.url = url;
	}
    

}