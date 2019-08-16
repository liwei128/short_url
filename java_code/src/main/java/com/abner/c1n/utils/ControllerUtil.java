package com.abner.c1n.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.abner.c1n.beans.bo.HttpProxyRequest;
import com.abner.c1n.beans.bo.RequestInfo;
import com.abner.c1n.beans.constant.ShortLinkConstant;
import com.abner.c1n.beans.constant.SymbolConstant;
import com.abner.c1n.beans.po.LogEntity.LogStatus;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

/**
 * Controller工具
 * @author liwei
 * @date: 2018年10月22日 下午3:11:06
 *
 */
public class ControllerUtil {
		
	public static String getDomainName() {
		return getRequest().getServerName();
	}
	
	public static HttpServletRequest getRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
			return ((ServletRequestAttributes) requestAttributes).getRequest();
		}
		return null;
	}
	
	public static HttpServletResponse getResponse() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
			return ((ServletRequestAttributes) requestAttributes).getResponse();
		}
		return null;
	}
	
	public static Map<String,String> readCookies() {
		Cookie[] cookies = getRequest().getCookies();
		Map<String,String> cookiesMap = Maps.newHashMap();
		if(cookies!=null){
			for(Cookie cookie: cookies){
				cookiesMap.put(cookie.getName(), cookie.getValue());
			}
		}
		return cookiesMap;
	}
	
	public static void setCookie(Map<String,String> cookiesMap,int maxAge) {
		HttpServletResponse response = getResponse();
		cookiesMap.forEach((name,value)->{
			Cookie cookie = new Cookie(name, value);
			cookie.setDomain(parseOneDomain(getRequest().getRequestURL().toString()));
	        cookie.setPath("/");
	        cookie.setMaxAge(maxAge);
	        response.addCookie(cookie); 
		});
    }

	
	public static Map<String, String> readForm() {
		
		Map<String, String[]> parameterMap = getRequest().getParameterMap();
		Map<String, String> parameters = Maps.newHashMap();
		parameterMap.forEach((key,value)->{
			if(value!=null&&value.length>0){
				parameters.put(key, value[0]);
			}
		});
		return parameters;
	}
	
	
	public static String readJson() {
		try{
			BufferedReader streamReader = getRequest().getReader();
			StringBuilder body = new StringBuilder(); 
			String inputStr;  
		    while ((inputStr = streamReader.readLine()) != null){  
		        body.append(inputStr);
		    }
			return body.toString();
		}catch(Exception e){
			return "";
		}
	}
	
	
	public static HttpProxyRequest generateProxyRequest(){
		HttpProxyRequest proxyRequest = new HttpProxyRequest();
		proxyRequest.setUrl(getRequest().getHeader("url"));
		proxyRequest.setHttpMethod(getMethod());
		proxyRequest.setFormData(readForm());
		proxyRequest.setJsonData(readJson());
		proxyRequest.setCookies(readCookies());
		return proxyRequest;
		
	}

	private static HttpMethod getMethod() {
		String method = getRequest().getMethod();
		return HttpMethod.valueOf(method);
	}
	
	/**
	 * 从url中解析域名
	 * @param url
	 * @return
	 */
	public static String parseDomain(String url) {
		if(StringUtils.isEmpty(url)){
			return null;
		}
		String[] split = url.split("/");
		if(split.length>=SymbolConstant.THREE){
			return split[2];
		}
		return null;
	}
	
	/**
	 * 从url中解析一级域名
	 * @param url
	 * @return
	 */
	public static String parseOneDomain(String url){
		String domain = parseDomain(url);
		if(StringUtils.isEmpty(domain)){
			return null;
		}
		String[] domains = domain.split("\\.");
		if(domains.length>1){
			return domains[domains.length-2]+"."+domains[domains.length-1];
		}
		return domains[0];
	}
	
	/**
	 * 转换url
	 * @param url
	 * @return
	 */
	public static String parseUrl(String url) {
		if(url.contains(SymbolConstant.QUESTION)){
			url = url.split(SymbolConstant.QUESTION_ESCAPING)[0];
		}
		if(url.contains(SymbolConstant.C)){
			url = url.split(SymbolConstant.C)[0];
		}
		if(url.endsWith("/")){
			url = url.substring(0, url.length()-1);
		}
		return url;
	}
	/**
	 * 获取ip
	 * @param request
	 * @return
	 */
	public static String getIp() { 
		HttpServletRequest request = getRequest();
		String agencyIp = request.getHeader("X-Forwarded-For");
		String ip = request.getHeader("X-Real-IP"); 
		if(StringUtils.isNotEmpty(agencyIp) && !SymbolConstant.UNKNOWN.equalsIgnoreCase(agencyIp)){             
			int index = agencyIp.indexOf(",");              
			if(index != -1){                   
				return agencyIp.substring(0,index);     
			}else{                  
				return agencyIp;            
			}         
		}       
		
		if(StringUtils.isNotEmpty(ip) && !SymbolConstant.UNKNOWN.equalsIgnoreCase(ip)){      
			return ip;           
		}
		return request.getRemoteAddr();     
	}

	/**
	 * 构建用户访问记录
	 * @return
	 */
	public static RequestInfo builderRequestInfo() {
    	HttpServletRequest request = getRequest();
    	String url = request.getRequestURL().toString();
    	RequestInfo requestInfo = new RequestInfo();
    	requestInfo.setCookies(recordCookies());
    	requestInfo.setIp(getIp());
    	requestInfo.setBrowser(UserAgentUtils.getBrowser(request));
    	requestInfo.setDevice(UserAgentUtils.getDeviceType(request));
    	requestInfo.setSource(request.getHeader("Referer"));
    	requestInfo.setUrl(parseUrl(url));
    	requestInfo.setDomain(parseOneDomain(url));
    	requestInfo.setStatus(LogStatus.SUCCESS);
    	return requestInfo;
	}
	
	/**
	 * 记录用户Cookies，不存在则创建
	 * @return
	 */
	public static String recordCookies(){
		String cookiesStr = "";
		Cookie[] cookies = getRequest().getCookies();
		if(cookies!=null){
			for(Cookie cookie: cookies){
				if(ShortLinkConstant.COOKIES_NAME.equals(cookie.getName())){
					cookiesStr = cookie.getValue();
				}
			}
		}
		if(StringUtils.isEmpty(cookiesStr)){
			cookiesStr = RandomStringUtils.randomAlphanumeric(10)+System.currentTimeMillis();
			Map<String, String> cookiesMap = Maps.newHashMap();
			cookiesMap.put(ShortLinkConstant.COOKIES_NAME, cookiesStr);
			ControllerUtil.setCookie(cookiesMap, Integer.MAX_VALUE);
		}
		return cookiesStr;
	}
	
	public static void setResponseData(Object data,int status) throws IOException{
		HttpServletResponse response = getResponse();
		response.setStatus(status);
		response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.append(JSON.toJSONString(data));
        
	}
	
	public static void setResponseData(Object data) throws IOException{
		setResponseData(data, HttpStatus.SC_OK);
	}


}
