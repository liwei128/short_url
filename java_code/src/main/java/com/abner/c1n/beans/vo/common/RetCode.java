package com.abner.c1n.beans.vo.common;

import com.abner.c1n.beans.enums.BaseRetCode;

/**
 * 响应状态码
 * @author liwei
 * @date: 2018年8月16日 下午3:26:02
 *
 */
public interface RetCode {
	
	/**
	 * 状态码
	 * @return
	 */
	public int getCode();
	
	/**
	 * 消息
	 * @return
	 */
	public String getMsg();
	
	/**
	 * 是否成功
	 * @return
	 */
	default boolean success(){
		return getCode() == BaseRetCode.SUCCESS.getCode();
	}

}
