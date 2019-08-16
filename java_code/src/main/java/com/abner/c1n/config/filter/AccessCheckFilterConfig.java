package com.abner.c1n.config.filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.abner.c1n.beans.constant.SymbolConstant;
import com.abner.c1n.beans.enums.BaseRetCode;
import com.abner.c1n.beans.vo.common.ResultData;
import com.abner.c1n.utils.ControllerUtil;

/**
 * 请求校验过滤器，拦截非法请求、限流
 * @author 01383518
 * @date:   2019年8月8日 下午2:59:50
 */
@Configuration
public class AccessCheckFilterConfig {
	
	
	private static Logger logger = LoggerFactory.getLogger(AccessCheckFilterConfig.class);
	
	private static final AtomicInteger COUNT = new AtomicInteger(0);
	
	private static final int COUNT_LIMIT = 100;
	
	@Autowired
	private RequestMappingHandlerMapping requestMappingHandlerMapping;
	
    
    /**
     * 记录请求日志信息、校验、限流
     * @return
     */
    @Bean
    public FilterRegistrationBean<Filter> accessCheckFilter() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new BaseFilter(){
			@Override
			public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
					throws IOException, ServletException {
				try{
					String url = ControllerUtil.getRequest().getRequestURL().toString();
					//访问频率限制
					if(COUNT.incrementAndGet()>COUNT_LIMIT){
						ControllerUtil.setResponseData(ResultData.getInstance(BaseRetCode.ACCESS_LIMIT));
						logger.warn("访问频率过高:{}",url);
						return;
					}
					//域名限制以及非法请求
					if(!checkDomain(url)){
						logger.warn("非法请求:{}",url);
						ControllerUtil.setResponseData(ResultData.getInstance(BaseRetCode.ERROR_ACCESS),HttpStatus.SC_NOT_FOUND);
						return;
					}
					chain.doFilter(request, response);
				}finally{
					COUNT.decrementAndGet();
				}
				
			}
        	
        });
        registration.addUrlPatterns("/*");
        registration.setName("accessCheckFilter");
        registration.setOrder(2);
        return registration;
    }

	protected boolean checkDomain(String url) {
		//限制域名（一级和二级）
		int length = ControllerUtil.parseDomain(url).split("\\.").length;
		if(!(length==SymbolConstant.TWO||length==SymbolConstant.THREE)){
			return false;
		}
		//限制uri
		String uri = ControllerUtil.getRequest().getRequestURI();
		if(StringUtils.isNotEmpty(uri)&&uri.contains(SymbolConstant.DOT)){
			return false;
		}
		return isMatching(uri);
	}
	
	private boolean isMatching(String uri){
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
		for(RequestMappingInfo key : handlerMethods.keySet()){
			List<String> matchingPatterns = key.getPatternsCondition().getMatchingPatterns(uri);
			if(!CollectionUtils.isEmpty(matchingPatterns)){
				return true;
			}
		}
		return false;
	}
    

}
