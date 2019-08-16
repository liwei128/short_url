package com.abner.c1n.utils;

import javax.servlet.http.HttpServletRequest;

import eu.bitwalker.useragentutils.UserAgent;

/**
 * UserAgent识别工具
 * @author 01383518
 * @date:   2019年6月6日 下午6:40:21
 */
public class UserAgentUtils {
	
	public static String getBrowser(HttpServletRequest request){
		String userAgent = request.getHeader("User-Agent");
		UserAgent agent = UserAgent.parseUserAgentString(userAgent);
		return agent.getBrowser().getGroup().getName();
	}
	
	public static String getDeviceType(HttpServletRequest request){
		String userAgent = request.getHeader("User-Agent");
		UserAgent agent = UserAgent.parseUserAgentString(userAgent);
		return agent.getOperatingSystem().getGroup().getName();
		
	}
	
	

}

