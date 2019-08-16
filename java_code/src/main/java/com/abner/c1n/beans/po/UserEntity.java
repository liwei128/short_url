package com.abner.c1n.beans.po;

import org.apache.commons.lang3.RandomStringUtils;

import com.abner.c1n.beans.bo.UserInfoBo;
import com.abner.c1n.beans.constant.UserConstant;
import com.abner.c1n.utils.EncryptionUtils;

/**
 * 用户信息实体
 * @author 01383518
 * @date:   2019年6月12日 上午10:07:24
 */
public class UserEntity extends BaseEntity{

	/**
	 * 用户名
	 */
    private String name;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * token
     */
    private String token;

    /**
     * 状态
     */
    private UserStatus status;
    
    public enum UserStatus{
    	/**
    	 * 初始状态
    	 */
    	INIT,
    	/**
    	 * 启用
    	 */
    	NORMAL,
    	/**
    	 * 禁用
    	 */
    	DISABLED;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

	public UserEntity(UserInfoBo userInfo) {
		super();
		this.name = userInfo.getName();
		this.pwd = userInfo.getPwd();
		this.mail = userInfo.getMail();
		this.token = userInfo.getToken();
		this.status = userInfo.getStatus();
	}
	
	public UserEntity init(){
		this.token = RandomStringUtils.randomAlphanumeric(20);
		this.status = UserStatus.INIT;
		return this;
	}
	
	public String generateToken(){
		return EncryptionUtils.md5(UserConstant.TOKEN+this.name);
	}

	public UserEntity() {
		super();
	}
    
}