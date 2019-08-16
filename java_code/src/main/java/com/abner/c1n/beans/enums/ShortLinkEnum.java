package com.abner.c1n.beans.enums;

import com.abner.c1n.beans.vo.common.RetCode;

/**
 * 短链接服务相关枚举 10开头
 * @author liwei
 * @date: 2018年8月16日 下午2:24:55
 *
 */
public enum ShortLinkEnum implements RetCode{
	
	/**
	 * 链接不合法
	 */
	ILLEGAL_URL(1001,"链接格式不正确"),
	/**
	 * 该短网址已存在
	 */
	EXIST(1002,"该短网址已存在"),
	/**
	 * 自定义网址格式不正确
	 */
	FORMAT_ERROR(1003,"自定义网址格式不正确"),
	
	/**
	 * 不支持生成本站链接
	 */
	UNSUPPORTED_LINKS(1004,"不支持生成本站链接"),
	
	/**
	 * 黑名单
	 */
	BLACKLIST(1005,"该网址可能存在安全隐患，已停止访问");
	
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

	private ShortLinkEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	

}
