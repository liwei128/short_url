package com.abner.c1n.beans.bo;

import com.abner.c1n.beans.po.UserEntity.UserStatus;
import com.alibaba.fastjson.JSON;

/**
 * 用户信息
 * @author 01383518
 * @date:   2019年6月12日 上午10:48:47
 */
public class UserInfoBo {
	
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

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
    
    

}
