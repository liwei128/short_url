package com.abner.c1n.config.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/***
 * 跨域过滤器
 * @author 01383518
 * @date:   2019年8月8日 下午2:58:50
 */
@Configuration
public class CorsFilterConfig {
	
	
	private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); 
        corsConfiguration.addAllowedHeader("*"); 
        corsConfiguration.addAllowedMethod("*"); 
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }

    /**
     * 跨域过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
    	 FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>();
    	 
    	 UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
         source.registerCorsConfiguration("/**", buildConfig());
    	 registration.setFilter(new CorsFilter(source));
    	 registration.addUrlPatterns("/*");
         registration.setName("CorsFilter");
         registration.setOrder(1);
         return registration;
    }
}
