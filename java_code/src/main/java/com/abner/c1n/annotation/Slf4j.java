package com.abner.c1n.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 日志记录
 * @author 01383518
 * @date:   2019年8月13日 上午10:28:34
 */

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Slf4j {
	/**
	 * 日志内容
	 * @return
	 */
	String value();
	
	/**
	 * 是否记录响应时间
	 * @return
	 */
	boolean logTime() default true;
	
	/**
	 * 是否记录异常栈信息
	 * @return
	 */
	boolean printStackTrace() default false;
    
}
