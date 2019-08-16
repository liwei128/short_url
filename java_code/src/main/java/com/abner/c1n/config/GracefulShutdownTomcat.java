package com.abner.c1n.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import com.abner.c1n.task.ShortUrlReviewTask;

/**
 * Tomcat优雅停机
 * @author LW
 * @time 2019年7月13日 上午12:05:47
 */
@Component
public class GracefulShutdownTomcat implements ApplicationListener<ContextClosedEvent> {
    private final Logger log = LoggerFactory.getLogger(GracefulShutdownTomcat.class);
    private final int waitTime = 30;
    private static volatile Connector connector;
    
	@Resource
	private	ShortUrlReviewTask shortUrlReviewTask;
	
    
    /**
     * 用于获取tomcat连接器
     * @return
     */
    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addConnectorCustomizers(connect->{
        	connector = connect;
        });
        return tomcat;
    }

	/**
     * 监听 停止操作kill -15
     */
    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
    	long startTime = System.currentTimeMillis();
    	//tomcat暂停对外服务
    	connector.pause();
        //获取tomcat线程池
        Executor executor = connector.getProtocolHandler().getExecutor();
        if (executor instanceof ThreadPoolExecutor) {
            try {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                //线程池优雅停止(不接收新的请求，等待任务运行完成后关闭线程池)
                threadPoolExecutor.shutdown();
                //堵塞等待一定时间，指定时间内关闭成功则返回true，解除堵塞；否则fasle
                if (threadPoolExecutor.awaitTermination(waitTime, TimeUnit.SECONDS)) {
                    log.info("Tomcat thread pool closed,time:{}ms",System.currentTimeMillis()-startTime);
                }
                //处理队列中挤压的数据
                shortUrlReviewTask.mailReminder();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
}