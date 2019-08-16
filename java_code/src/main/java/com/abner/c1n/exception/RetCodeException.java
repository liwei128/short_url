package com.abner.c1n.exception;

import com.abner.c1n.beans.enums.BaseRetCode;
import com.abner.c1n.beans.vo.common.ResultData;
import com.abner.c1n.beans.vo.common.RetCode;

/**
 * 附带RetCode的异常
 * @author 01383518
 * @date:   2019年1月10日 上午9:27:47
 */
public class RetCodeException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2010420346426677267L;
	
	private ResultData<Void> retCode;
	

	public ResultData<Void> getRetCode() {
		return retCode;
	}

	public RetCodeException(String msg) {
		super(msg);
		this.retCode = ResultData.getInstance(BaseRetCode.EXCEPTION_FORMAT,msg);
	}

	public RetCodeException(RetCode retCode) {
		super(retCode.getMsg());
		this.retCode = ResultData.getInstance(retCode);
	}
	
	public RetCodeException(RetCode retCode,Throwable cause) {
		super(cause);
		this.retCode = ResultData.getInstance(retCode);
	}
	
	public RetCodeException(RetCode retCode,Object... msgArgs) {
		super(String.format(retCode.getMsg(),msgArgs));
		this.retCode = ResultData.getInstance(retCode,msgArgs);
	}
	

}
