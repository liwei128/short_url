package com.abner.c1n.config.thread;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

/**
 * 线程池配置
 * @author liwei
 * @date: 2018年11月7日 下午3:58:22
 *
 */
@Component
public class ThreadPoolConfig {
	
	@Value("${threadPool.corePoolSize}")
	private int corePoolSize;

	@Value("${threadPool.maxPoolSize}")
	private int maxPoolSize;

	@Value("${threadPool.keepAliveSeconds}")
	private int keepAliveSeconds;

	@Value("${threadPool.queueCapacity}")
	private int queueCapacity;
	
	@Value("${threadPool.schedulerSize}")
	private int schedulerSize;

	public int getCorePoolSize() {
		return corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getKeepAliveSeconds() {
		return keepAliveSeconds;
	}

	public void setKeepAliveSeconds(int keepAliveSeconds) {
		this.keepAliveSeconds = keepAliveSeconds;
	}

	public int getQueueCapacity() {
		return queueCapacity;
	}

	public void setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;
	}
	

	public int getSchedulerSize() {
		return schedulerSize;
	}

	public void setSchedulerSize(int schedulerSize) {
		this.schedulerSize = schedulerSize;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	
	

}
