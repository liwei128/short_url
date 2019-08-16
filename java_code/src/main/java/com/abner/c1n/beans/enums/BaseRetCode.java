package com.abner.c1n.beans.enums;

import com.abner.c1n.beans.vo.common.RetCode;
/**
 * 基本响应状态码
 * @author liwei
 * @date: 2018年9月10日 上午10:13:47
 *
 */
public enum BaseRetCode implements RetCode{
	
	/**
	 * 成功
	 */
	SUCCESS(0,"成功"),
	/**
	 * 失败
	 */
	FAIL(1,"失败"),
	/**
	 * 异常
	 */
	EXCEPTION_FORMAT(2,"异常:%s"),
	/**
	 * 参数缺失
	 */
	PARAM_MISSING(3,"参数缺失"),
	/**
	 * 没有访问权限
	 */
	NOT_PERMISSION(4,"没有访问权限"),
	/**
	 * 参数格式不正确
	 */
	FORMAT_ERROR(5,"参数格式不正确"),
	/**
	 * 访问频率过高
	 */
	ACCESS_LIMIT(6,"访问频率过高，请稍后重试"),
	
	/**
	 * 非法请求
	 */
	ERROR_ACCESS(7,"非法请求"),
	
	/**
	 * 链接已失效
	 */
	URL_FAILURE(8,"链接已失效");
	
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

	private BaseRetCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

}
