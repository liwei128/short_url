package com.abner.c1n.config.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.abner.c1n.beans.constant.UserThreadLocal;
import com.abner.c1n.beans.enums.UserEnum;
import com.abner.c1n.beans.vo.common.ResultData;
import com.abner.c1n.config.SystemConfig;
import com.abner.c1n.utils.ControllerUtil;

/**
 * 全局过滤器
 * @author LW
 * @time 2019年6月9日 上午11:33:34
 */
@Configuration
public class LoginFilterConfig {
	
	@Autowired
	private SystemConfig systemConfig;

	/**
     * 登录过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean<Filter> loginFilter() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new BaseFilter(){
			@Override
			public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
					throws IOException, ServletException {
				HttpServletRequest req = (HttpServletRequest)request;
				//判断是否登录
				if(isIgnoreLogin(req.getRequestURI())||UserThreadLocal.isLogin()){
					chain.doFilter(request, response);
				}else{
					ControllerUtil.setResponseData(ResultData.getInstance(UserEnum.NOT_LOGIN));
				}
			}
        	
        });
        registration.addUrlPatterns(systemConfig.getNeedLogin());
        registration.setName("loginFilter");
        registration.setOrder(4);
        return registration;
    }
    
    /**
     * 是否忽略登录
     * @param requestUri
     * @return
     */
    public boolean isIgnoreLogin(String requestUri) {
		return ArrayUtils.contains(systemConfig.getIgnoreLoginUri(), requestUri);
	}
    
}
