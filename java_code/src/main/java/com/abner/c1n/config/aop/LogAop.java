package com.abner.c1n.config.aop;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.abner.c1n.annotation.Slf4j;
import com.abner.c1n.beans.constant.UserThreadLocal;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

/**
 * 日志记录
 * @author 01383518
 * @date:   2019年1月9日 下午6:10:58
 */
@Component
@Aspect
public class LogAop {
	
	private static final Logger logger = LoggerFactory.getLogger(LogAop.class);
	
	private static final String PARSE = "#";
	
	
	/**
	 * 对于@Slf4j注解的方法，拦截进行增强
	 * 自动打印日志，记录请求和返回数据、记录响应时间
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(com.abner.c1n.annotation.Slf4j)")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable{
		if(!(pjp.getSignature() instanceof MethodSignature)){
			return pjp.proceed();
		}
		//1.获取请求参数
		String reqJson = getReqJson(pjp);
		//2.获取方法上的注解
		Slf4j logInfo = getSlf4j(pjp);
		//3.执行具体的代码逻辑
		long startTime = System.currentTimeMillis();
		Object result = execute(pjp);
	    //4.打印日志
	    printLog(logInfo,reqJson,result,startTime);
	    //5.返回方法的执行结果
	    if(result instanceof Throwable){
	    	throw (Throwable)result;
	    }
		return result;
	}
	

	/**
	 * 执行实际的方法
	 * @param pjp
	 * @return
	 */
	private Object execute(ProceedingJoinPoint pjp) {
	    try{
	    	return pjp.proceed();
	    }catch(Throwable e){
	    	return e;
	    }
	}

	/**
	 * 获取注解信息
	 * @param pjp
	 * @return
	 */
	private Slf4j getSlf4j(ProceedingJoinPoint pjp) {
		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
	    Method method = AopUtils.getMostSpecificMethod(methodSignature.getMethod(), pjp.getTarget().getClass());
	    return method.getAnnotation(Slf4j.class);
	}

	/**
	 * 获取请求从参数，序列化成字符串
	 * @param pjp
	 * @return
	 */
	private String getReqJson(ProceedingJoinPoint pjp) {
		Map<String, Object> map = Maps.newHashMap();
		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
		String[] names = methodSignature.getParameterNames();
		Object[] values = pjp.getArgs();
		for(int i = 0;i<names.length;i++){
			Object arg = values[i];
			//忽略controller层特殊的请求参数
			if(arg instanceof ServletRequest
					||arg instanceof ServletResponse
					||arg instanceof HttpSession
					||arg instanceof Model
					||arg instanceof ModelMap
					||arg instanceof ModelAndView
					||arg instanceof MultipartFile
					||arg instanceof MultipartFile[]){
				continue; 
			}
			map.put(names[i], values[i]);
		}
		if(map.size()==1){
			String key = map.keySet().stream().findFirst().get();
			return JSON.toJSONString(map.get(key));
		}
		return JSON.toJSONString(map);
	}

	/**
	 * 打印日志
	 * @param logInfo
	 * @param reqJson
	 * @param result
	 * @param startTime
	 */
	private void printLog(Slf4j logInfo, String reqJson, Object result, long startTime) {

		//日志名称
		String logName = builderLogName(reqJson,logInfo);
		//执行时间
		Long runtime = System.currentTimeMillis()-startTime;
		//日志内容
		String logBody = builderLogBody(logName,reqJson,runtime,result,logInfo);
		
		//正常日志输出
		if(!(result instanceof Throwable)){
			logger.info(logBody);
		}else{
			//错误日志输出
			if(logInfo.printStackTrace()){
				logger.error(logBody,result);
			}else{
				logger.error(logBody);
			}
		}
		
	}

	/**
	 * 解析日志名称
	 * @param reqJson
	 * @param logInfo
	 * @return
	 */
	private String builderLogName(String reqJson, Slf4j logInfo) {
		String value = logInfo.value();
		if(StringUtils.isEmpty(value)){
			return "";
		}
		if(value.indexOf(PARSE)==-1){
			return value;
		}
		String baseName = value.substring(0,value.indexOf(PARSE));
		String nameParam = value.substring(value.indexOf(PARSE)+1);
		String param  = isJsonString(reqJson)?JSON.parseObject(reqJson).getString(nameParam):reqJson;
		return baseName+param;
	}
	
	/**
	 * 
	 * 判断是否为json字符串
	 * @param reqJson
	 * @return
	 */
	private boolean isJsonString(String reqJson){
		try{
			if(StringUtils.isEmpty(reqJson)||"null".equals(reqJson)){
				return false;
			}
			JSON.parseObject(reqJson);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * 构建日志内容
	 * @param logName
	 * @param reqJson
	 * @param time
	 * @param result
	 * @param logInfo
	 * @return
	 */
	private String builderLogBody(String logName, String reqJson, long time, Object result,Slf4j logInfo) {
    	String user = UserThreadLocal.isLogin()?UserThreadLocal.getCurrentUser().getName():"游客";
		StringBuilder body = new StringBuilder();
		body.append("[").append(user).append("]").append(logName);
		if(logInfo.logTime()){
			body.append(",time:").append(time).append("ms");
		}
		
		body.append(",param:").append(reqJson);
		if(result instanceof Throwable){
			body.append(",exception;");
		}else{
			body.append(",result:").append(JSON.toJSONString(result));
		}
		return body.toString();
	}

}
