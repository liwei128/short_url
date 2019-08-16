package com.abner.c1n;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



/**
 * 
 * @author 01383518
 * @date:   2019年6月6日 下午6:40:59
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.abner.c1n.dao.mapper"})
public class FrameworkApplication {
    
	public static void main(String[] args){
		SpringApplication.run(FrameworkApplication.class, args);
		
	}

}
