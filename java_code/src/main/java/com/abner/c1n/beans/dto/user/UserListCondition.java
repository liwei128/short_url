package com.abner.c1n.beans.dto.user;

import com.abner.c1n.beans.dto.PageCondition;
import com.abner.c1n.beans.po.UserEntity.UserStatus;

/**
 * 用户列表查询条件
 * @author 01383518
 * @date:   2019年6月13日 上午10:59:44
 */
public class UserListCondition extends PageCondition{
	
	/**
	 * 用户名
	 */
    private String name;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 状态
     */
    private UserStatus status;
    
	/**
	 * 开始日期，格式yyyy-MM-dd
	 */
	public String startDate;
	
	/**
	 * 结束日期，格式yyyy-MM-dd
	 */
	public String endDate;
	

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}
    
    

}
