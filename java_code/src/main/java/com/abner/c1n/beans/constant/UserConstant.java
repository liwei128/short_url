package com.abner.c1n.beans.constant;

/**
 * 用户相关常量
 * @author 01383518
 * @date:   2019年6月12日 上午10:58:39
 */
public class UserConstant {
	
	/**
	 * 邮箱格式
	 */
	public final static String MAIL_MATCH = "^[\\.a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
	
	/**
	 * 缓存激活信息
	 */
	public final static String ACTIVE_KEY = "register-%s";
	
	/**
	 * 用户名格式
	 */
	public final static String USER_NAME_MATCH = "^[0-9a-zA-Z]+$";
	
	/**
	 * 用户登录验证信息
	 */
	public final static String TOKEN = "token";
	
	/**
	 * 用户验证码信息
	 */
	public final static String CODE = "code";
	
	/**
	 * 用于生成安全ID
	 */
	public final static String SAFE_ID = "ID";
	
	/**
	 * 管理员id
	 */
	public final static Long ADMIN_ID = 1L;

}
