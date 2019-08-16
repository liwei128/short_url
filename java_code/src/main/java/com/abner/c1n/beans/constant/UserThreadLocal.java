package com.abner.c1n.beans.constant;

import com.abner.c1n.beans.bo.RequestInfo;
import com.abner.c1n.beans.po.UserEntity;

/**
 * 保存登录的用户信息，当前线程可获取
 * @author 01383518
 * @date:   2019年1月25日 下午2:36:24
 */
public class UserThreadLocal {
	
	public static ThreadLocal<UserEntity> userData = new ThreadLocal<>();
	
	public static ThreadLocal<RequestInfo> requestInfos = new ThreadLocal<>();
	
	/**
	 * 获取当前的用户信息
	 * @return
	 */
	public static UserEntity getCurrentUser() {
		return userData.get();
	}
	
	/**
	 * 保存用户信息到ThreadLocal，全局都可以获取使用
	 * @param user
	 */
	public static void putUser(UserEntity user) {
		userData.set(user);
	}
	/**
	 * 清除用户信息
	 */
	public static void removeUser() {
		userData.remove();
	}
	
	/**
	 * 判断用户是否已经登录
	 * @return
	 */
	public static boolean isLogin() {
		return getCurrentUser()!=null;
	}
	
	/**
	 * 判断用户是否为管理员
	 * @return
	 */
	public static boolean isAdmin() {
		return getCurrentUser()!=null&&getCurrentUser().getId().equals(UserConstant.ADMIN_ID);
	}

	/**
	 * 保存用户请求信到当前线程
	 * @param requestInfo
	 */
	public static void putRequestInfo(RequestInfo requestInfo) {
		requestInfos.set(requestInfo);
	}
	
	/**
	 * 获取当前用户访问信息
	 * @param requestInfo
	 * @return
	 */
	public static RequestInfo getRequestInfo() {
		return requestInfos.get();
	}

	public static void removeRequestInfo() {
		requestInfos.remove();
	}
	
}
