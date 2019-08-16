package com.abner.c1n.config.thread;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * 异步线程池
 * 可配合注解使用@Async("asyncThreadPool")
 * @author liwei
 * @date: 2018年11月7日 下午1:42:53
 *
 */
@Component("asyncThreadPool")
public class AsyncThreadPool extends ThreadPoolTaskExecutor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2305730842681731330L;
	
	@Autowired
	private ThreadPoolConfig threadPoolConfig;

	@Override
	public void initialize() {
		setCorePoolSize(threadPoolConfig.getCorePoolSize());
		setMaxPoolSize(threadPoolConfig.getMaxPoolSize());
		setQueueCapacity(threadPoolConfig.getQueueCapacity());
		setKeepAliveSeconds(threadPoolConfig.getKeepAliveSeconds());
		setThreadNamePrefix("AsyncThreadPool-");
		setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		super.initialize();
	}


}
