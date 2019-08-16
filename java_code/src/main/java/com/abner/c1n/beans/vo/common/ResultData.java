package com.abner.c1n.beans.vo.common;


import java.io.Serializable;

import com.abner.c1n.beans.enums.BaseRetCode;
import com.alibaba.fastjson.JSON;
/**
 * 返回数据
 * @author liwei
 * @date: 2018年8月16日 下午3:26:19
 *
 */
public class ResultData<T> implements RetCode,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3398323943665457721L;

	private int code;
	
	private String msg;
	
	private T data;
	
	@Override
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	@Override
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public ResultData(RetCode retCode) {
		this.code = retCode.getCode();
		this.msg = retCode.getMsg();
	}

	public ResultData(T data) {
		this();
		this.data = data;
	}
	
	public ResultData() {
		this.code = BaseRetCode.SUCCESS.getCode();
		this.msg = BaseRetCode.SUCCESS.getMsg();
	}

	public static <T> ResultData<T> getInstance(RetCode retCode){
		return new ResultData<T>(retCode);
	}
	
	public static <T> ResultData<T> getInstance(T data){
		return new ResultData<T>(data);
	}
	
	public static ResultData<Void> getInstance(){
		return new ResultData<Void>();
	}
	
	public static <T> ResultData<T> getInstance(RetCode retCode,Object... msgArgs){
		ResultData<T> resultData = new ResultData<T>(retCode);
		resultData.setMsg(String.format(resultData.getMsg(),msgArgs));
		return resultData;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	
	

}
