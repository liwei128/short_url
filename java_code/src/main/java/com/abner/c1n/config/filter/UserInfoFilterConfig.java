package com.abner.c1n.config.filter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.abner.c1n.beans.bo.RequestInfo;
import com.abner.c1n.beans.constant.UserConstant;
import com.abner.c1n.beans.constant.UserThreadLocal;
import com.abner.c1n.beans.po.UserEntity;
import com.abner.c1n.dao.RedisDao;
import com.abner.c1n.dao.mapper.UserEntityMapper;
import com.abner.c1n.utils.ControllerUtil;
import com.alibaba.fastjson.JSON;

/**
 * 记录用户访问信息
 * @author 01383518
 * @date:   2019年8月9日 下午1:02:14
 */
@Configuration
public class UserInfoFilterConfig {
	
	
	private static Logger logger = LoggerFactory.getLogger(UserInfoFilterConfig.class);
	
	@Autowired
	private RedisDao redisDao;
	
	@Autowired
	private UserEntityMapper userEntityMapper;
	
    
    /**
     * 记录用户的请求
     * @return
     */
    @Bean
    public FilterRegistrationBean<Filter> userInfoFilter() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new BaseFilter(){
			@Override
			public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
					throws IOException, ServletException {
				//记录请求日志信息到当前线程
				UserThreadLocal.putRequestInfo(ControllerUtil.builderRequestInfo());
				//记录用户登录信息
				recordUserInfo();
				
				chain.doFilter(request, response);
				//打印请求日志
				logRecord();
				//清除请求日志信息
				UserThreadLocal.removeRequestInfo();
				//清除登录用户信息
				UserThreadLocal.removeUser();
			}
        	
        });
        registration.addUrlPatterns("/*");
        registration.setName("userInfoFilter");
        registration.setOrder(3);
        return registration;
    }

	protected void recordUserInfo() {
		UserEntity userEntity = null;
		// cookies方式登录
		String tokenKey = ControllerUtil.readCookies().get(UserConstant.TOKEN);
		if (StringUtils.isNotEmpty(tokenKey)) {
			String json = redisDao.get(tokenKey);
			if (StringUtils.isNotEmpty(json)) {
				userEntity = JSON.parseObject(json, UserEntity.class);
				// 刷新失效时间，保持登录
				redisDao.expire(tokenKey, 30, TimeUnit.MINUTES);
			}
		}
		// 请求头token方式登录
		String token = ControllerUtil.getRequest().getHeader(UserConstant.TOKEN);
		if (StringUtils.isNotEmpty(token)) {
			userEntity = userEntityMapper.queryByToken(token);
		}

		if (userEntity != null) {
			UserThreadLocal.putUser(userEntity);
		}

	}

	/**
     * 日志记录
     */
    protected void logRecord() {
    	RequestInfo info = UserThreadLocal.getRequestInfo();
    	String user = UserThreadLocal.isLogin()?UserThreadLocal.getCurrentUser().getName():"游客";
    	logger.info("[{}]url:{},ip:{},cookies:{},browser:{},status:{}",user,info.getUrl(),info.getIp(),info.getCookies(),info.getBrowser(),info.getStatus());
	}

    

}
