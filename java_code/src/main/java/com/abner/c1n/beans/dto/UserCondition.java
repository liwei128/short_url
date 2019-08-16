package com.abner.c1n.beans.dto;

import com.abner.c1n.beans.constant.UserThreadLocal;

/**
 * 根据登录用户，隔离用户数据
 * @author LW
 * @time 2019年6月12日 下午9:48:07
 */
public class UserCondition implements Cloneable{
	
	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public UserCondition() {
		super();
		if(UserThreadLocal.isLogin()&&!UserThreadLocal.isAdmin()){
			this.userId = UserThreadLocal.getCurrentUser().getId();
		}
	}
	public boolean compareUserId(Long userId){
		return this.userId == null||this.userId.equals(userId);
	}	
	

}
