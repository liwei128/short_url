package com.abner.c1n.utils;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import com.abner.c1n.beans.bo.HttpProxyRequest;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
/**
 * 新版 Http工具类
 * @author liwei
 * @date: 2018年8月31日 下午4:53:00
 *
 */
public class HttpClientUtils {
	
	private static  Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
	
	private static final List<String> EXCLUDE_COOKIES = Lists.newArrayList("domain","path","expires","HttpOnly","secure","Max-Age");
		
	
	/**
	 * get请求
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 */
	public static String get(String url, Map<String,String> headers,Map<String, String> cookies){
		CloseableHttpClient httpClient = createCookiesHttpClient();
    	CloseableHttpResponse response=null;
		try{
    		HttpGet httpGet = new HttpGet(url);
    		//头部
			setHeaders(httpGet,headers);
			//cookies
			setCookies(httpGet,cookies);
            response = httpClient.execute(httpGet);
            return toString(response);
            
		}catch(Exception e){
			logger.error("链接:{}异常",url,e);
			return null;
    	}finally{
    		closeStream(response,httpClient);
    	}
	}
	

	/**
	 * get请求
	 * @param url
	 * @return
	 */
	public static String get(String url){
		return get(url,Maps.newHashMap(),Maps.newHashMap());
	}
	
	/**
	 * get请求
	 * @param url
	 * @param headers
	 * @return
	 */
	public static String get(String url,Map<String,String> headers){
		return get(url,headers,Maps.newHashMap());
	}

	/**
	 * post提交json或者xml、文本信息
	 * @param url
	 * @param data
	 * @param headers
	 * @return
	 */
	public static String postData(String url,String data, Map<String,String> headers,Map<String, String> cookies){
		CloseableHttpClient httpClient = createCookiesHttpClient();
    	CloseableHttpResponse response=null;
		try{
    		HttpPost httpPost = new HttpPost(url);
			//头部
			setHeaders(httpPost,headers);
			//cookies
			setCookies(httpPost,cookies);
			
    		httpPost.setEntity(new StringEntity(data, CharEncoding.UTF_8));
            response = httpClient.execute(httpPost);
            return toString(response);
            
		}catch(Exception e){
			logger.error("链接:{}异常",url,e);
			return null;
    	}finally{
    		closeStream(response, httpClient);
    	}
	}
	/**
	 * post提交json或者xml、文本信息
	 * @param url
	 * @param data
	 * @return
	 */
	public static String postData(String url,String data){
		return postData(url, data, Maps.newHashMap(),Maps.newHashMap());
	}
	
	/**
	 * 提交表单数据
	 * @param url
	 * @param from
	 * @param headers
	 * @return
	 */
	public static String postForms(String url, Map<String, String> from, Map<String,String> headers,Map<String, String> cookies) {
		CloseableHttpClient httpClient = createCookiesHttpClient();
		CloseableHttpResponse response = null;
		try {
			HttpPost httpPost = new HttpPost(url);
			//头部
			setHeaders(httpPost,headers);
			//cookies
			setCookies(httpPost,cookies);
			//表单
			setFormEntity(httpPost,from);
			
			response = httpClient.execute(httpPost);
			return toString(response);
		} catch (Exception e) {
			logger.error("链接:{}异常",url,e);
			return null;
		} finally {
			closeStream(response,httpClient);
		}

	}
	
	/**
	 * 设置表单格式数据
	 * @param httpPost
	 * @param from
	 * @throws Exception
	 */
	private static void setFormEntity(HttpPost httpPost,Map<String, String> from) throws Exception {
		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		for (Map.Entry<String, String> entry : from.entrySet()) {
			pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(pairs, CharEncoding.UTF_8));
		
	}
	/**
	 * 设置请求头
	 * @param httpRequest
	 * @param headers
	 */
	private static void setHeaders(HttpRequestBase httpRequest, Map<String, String> headers) {
		if (headers != null && headers.size() > 0) {
			headers.forEach((key, value) -> {
				httpRequest.addHeader(key, value);
			});
		}
		httpRequest.addHeader("Connection", "close"); 
	}
	/**
	 * 提交表单数据
	 * @param url
	 * @param from
	 * @return
	 */
	public static String postForms(String url, Map<String, String> from) {
		return postForms(url, from, Maps.newHashMap(),Maps.newHashMap());
	}
	
	/**
	 * 创建HttpClient
	 * 
	 * @return
	 */
	private static CloseableHttpClient createCookiesHttpClient() {

		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000)  
                       .setConnectionRequestTimeout(10000).build();
        // 设置默认跳转以及存储cookie  
        CloseableHttpClient httpClient = HttpClientBuilder.create()  
                     .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())  
                     .setRedirectStrategy(new DefaultRedirectStrategy()).setDefaultRequestConfig(requestConfig)  
                     .setDefaultCookieStore(new BasicCookieStore()).build(); 
		return httpClient;
	}
	
	
	
	/**
	 * 关闭资源
	 * @param streams      
	 * void
	 */
	private static void closeStream(Closeable... streams) {
		for(Closeable stream:streams){
			if(stream!=null){
				try {
					stream.close();
				} catch (IOException e) {
					logger.error("资源关闭异常",e);
				}
			}
			
		}
	}
	/**
	 * 获取响应消息实体  
	 * @param httpResponse
	 * @return
	 */
	private static String toString(CloseableHttpResponse httpResponse){  
    	try{
        	int statusCode = httpResponse.getStatusLine().getStatusCode();
        	if(statusCode!=HttpStatus.SC_OK){
        		
	        	logger.error("状态值:{}",statusCode); 
        		return null;
        	}
    		HttpEntity entity = httpResponse.getEntity();
            return EntityUtils.toString(entity,CharEncoding.UTF_8);  
    	}catch(Exception e){
    		logger.error("http返回数据转字符出现异常");  
    	}
    	return null;
        
    }
	
	/**
	 * 解析返回的cookies
	 * @param headers
	 * @return
	 */
	private static Map<String,String> parseCookies(Header[] headers) {
		
		Map<String,String> newHashMap = Maps.newHashMap();
		if(headers==null){
			return newHashMap;
		}
		List<Header> headerList = Lists.newArrayList(headers);
		headerList.forEach(h->{
			String value = h.getValue();
			List<String> values = Lists.newArrayList(value.split(";"));
			for(String node : values){
				String[] split = node.split("=");
				String key = split[0].trim();
				boolean match = EXCLUDE_COOKIES.stream().allMatch(u->{
					return !key.equalsIgnoreCase(u);
				});
				if(match&&split.length>1){
					newHashMap.put(key, split[1]);
				}
			}
		});

		return newHashMap;
	}

	
	/**
	 * 请求头设置cookies
	 * @param httpRequest
	 * @param cookies
	 */
	private static void setCookies(HttpRequestBase httpRequest, Map<String, String> cookies) {
		StringBuilder builder = new StringBuilder();
		for(String key : cookies.keySet()){
			builder.append(key).append("=").append(cookies.get(key)).append(";");
		}
		httpRequest.addHeader("Cookie", builder.toString());
	}
	
	/**
	 * 支持跳转和cookies的请求
	 * @param httpSkipAndCookiesBo
	 * @return
	 * @throws Exception 
	 */
	public static String httpProxy(HttpProxyRequest httpProxyRequest) throws Exception {
		httpProxyRequest.checkRedirectCount();
		
		CloseableHttpClient httpClient = createCookiesHttpClient();
    	CloseableHttpResponse response=null;
		try{
			HttpRequestBase http;
			if(httpProxyRequest.getHttpMethod()==HttpMethod.POST){
				HttpPost httpPost = new HttpPost(httpProxyRequest.getUrl());
				if(httpProxyRequest.getFormData().size()>0){
					setFormEntity(httpPost,httpProxyRequest.getFormData());
				}
				String jsonData = httpProxyRequest.getJsonData();
				if(StringUtils.isNotEmpty(jsonData)){
					httpPost.setEntity(new StringEntity(jsonData, CharEncoding.UTF_8));
					httpPost.addHeader("Content-type","application/json; charset=utf-8");
				}
				http = httpPost;
			}else{
				http = new HttpGet(httpProxyRequest.getUrl());
			}
			setCookies(http,httpProxyRequest.getCookies());

            response = httpClient.execute(http);
            int statusCode = response.getStatusLine().getStatusCode();
            httpProxyRequest.getCookies().putAll(parseCookies(response.getHeaders("Set-Cookie")));
            if(statusCode==HttpStatus.SC_MOVED_PERMANENTLY||statusCode==HttpStatus.SC_MOVED_TEMPORARILY){
            	String rurl = response.getHeaders("Location")[0].getValue();
            	httpProxyRequest.setUrl(rurl);
            	return httpProxy(httpProxyRequest);
        	}
            return toString(response);
    	}finally{
    		closeStream(response,httpClient);
    	}
	}


	public static void imageCode(String url,Map<String, String> cookies) {
		
		CloseableHttpClient httpClient = createCookiesHttpClient();
    	CloseableHttpResponse response=null;
		try{
    		HttpGet httpGet = new HttpGet(url);
    		
    		setCookies(httpGet, cookies);
    		
            response = httpClient.execute(httpGet);

            HttpServletResponse httpResponse = ControllerUtil.getResponse();

            HttpEntity entity = response.getEntity();
			byte[] data = EntityUtils.toByteArray(entity);
			httpResponse.setContentType("image/jpeg;charset=UTF-8");
			ServletOutputStream outputStream = httpResponse.getOutputStream();
			outputStream.write(data);
			outputStream.flush();
		}catch(Exception e){
			logger.error("链接:{}异常",url,e);
    	}finally{
    		closeStream(response,httpClient);
    	}
	}
}