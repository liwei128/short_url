package com.abner.c1n.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 管理员权限注解
 * @author 01383518
 * @date:   2019年6月13日 下午12:19:07
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Admin {	
    
}
