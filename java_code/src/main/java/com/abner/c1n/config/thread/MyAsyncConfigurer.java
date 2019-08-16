package com.abner.c1n.config.thread;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;


/**
 * 修改springBoot @Async 默认使用的线程池
 * @author liwei
 * @date: 2018年11月7日 下午2:39:32
 *
 */
@Configuration
@EnableScheduling
@EnableAsync
public class MyAsyncConfigurer implements AsyncConfigurer,SchedulingConfigurer{
	
	private static Logger logger = LoggerFactory.getLogger(MyAsyncConfigurer.class);
	
	@Autowired
	private AsyncThreadPool asyncThreadPool;
	
	@Autowired
	private ThreadPoolConfig threadPoolConfig;

	@Override
	public Executor getAsyncExecutor() {
		return asyncThreadPool;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncUncaughtExceptionHandler(){
			@Override
			public void handleUncaughtException(Throwable ex, Method method, Object... params) {
				logger.error("asyncThreadPool task error,{}.{}()",method.getDeclaringClass().getSimpleName(),method.getName(),ex);
			}
			
		};
	}

	/**
	 * 注册定时任务的线程池
	 */
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

		taskRegistrar.setScheduler(new ScheduledThreadPoolExecutor(threadPoolConfig.getSchedulerSize(), r -> {
			return new Thread(r);
		}));
	}
	
	
	
}
