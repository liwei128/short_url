package com.abner.c1n.config.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.abner.c1n.beans.constant.UserThreadLocal;
import com.abner.c1n.beans.enums.BaseRetCode;
import com.abner.c1n.beans.vo.common.ResultData;

/**
 * 
 * @author 01383518
 * @date:   2019年6月13日 下午12:17:25
 */
@Component
@Aspect
public class AdminAop {
	
	/**
	 * 管理员权限拦截
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(com.abner.c1n.annotation.Admin)")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable{
		if(!UserThreadLocal.isAdmin()){
			return ResultData.getInstance(BaseRetCode.NOT_PERMISSION);
		}
		return pjp.proceed();
	}

}
