package com.abner.c1n.beans.vo.user;

import java.util.Date;

import com.abner.c1n.beans.po.UserEntity;
import com.abner.c1n.beans.po.UserEntity.UserStatus;
import com.alibaba.fastjson.JSON;

/**
 * 返回到页面的用户信息
 * @author 01383518
 * @date:   2019年6月13日 上午10:47:38
 */
public class UserVo {
	
	/**
	 * id
	 * 
	 */
	private Long id;
	
	/**
	 * 用户名
	 */
    private String name;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public UserVo(UserEntity currentUser) {
		this.id = currentUser.getId();
		this.name = currentUser.getName();
		this.mail = currentUser.getMail();
		this.token = currentUser.getToken();
		this.status = currentUser.getStatus();
		this.createTime = currentUser.getCreateTime();
	}

	public UserVo() {
		super();
	}
	
	
	

}
