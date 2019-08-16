package com.abner.c1n.config.filter;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

/**
 * 公共过滤器
 * @author LW
 * @time 2019年6月9日 下午9:18:10
 */
public interface BaseFilter extends Filter{

	/**
	 * 初始化
	 * @param filterConfig
	 * @throws ServletException
	 * @return
	 */
	@Override
	default void init(FilterConfig filterConfig) throws ServletException {
	}

	/**
	 * 销毁方法
	 */
	@Override
	default void destroy() {
	}

}
