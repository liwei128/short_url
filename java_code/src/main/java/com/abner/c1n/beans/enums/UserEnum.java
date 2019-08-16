package com.abner.c1n.beans.enums;

import com.abner.c1n.beans.vo.common.RetCode;

/**
 * 用户相关枚举
 * @author 01383518
 * @date:   2019年6月12日 上午11:01:03
 */
public enum UserEnum implements RetCode{
	
	/**
	 * 邮箱格式不正确
	 */
	MAIL_NOT_MATCH(2001,"邮箱格式不正确"),
	/**
	 * 请登录后再访问
	 */
	NOT_LOGIN(2002,"请登录后再访问"), 
	/**
	 * 用户名已存在
	 */
	USER_EXIST(2003,"用户名已存在"), 
	/**
	 * 用户名格式不正确
	 */
	USER_NOT_MATCH(2004,"用户名格式不正确"),
	/**
	 * 登录失败，用户名或密码错误
	 */
	LOGIN_FAIL(2005,"登录失败，用户名或密码错误"),
	/**
	 * 用户名或邮箱不匹配
	 */
	RESET_PWD_FAIL(2006,"用户名或者邮箱不匹配"),
	
	/**
	 * 修改密码失败，原密码错误
	 */
	CHANGE_PWD_FAIL(2007,"修改密码失败，原密码错误"),
	
	/**
	 * 验证码错误
	 */
	CODE_FAIL(2008,"验证码错误"),
	
	/**
	 * 账号未激活
	 */
	NOT_ACTIVATED(2009,"账号未激活，请前往邮件进行验证激活"),
	
	/**
	 * 账号被禁用
	 */
	USER_DISABLED(2010,"账号被禁用");
	
	private int code;
	
	private String msg;

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	private UserEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

}
