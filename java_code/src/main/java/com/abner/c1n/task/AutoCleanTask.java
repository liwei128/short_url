package com.abner.c1n.task;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.abner.c1n.beans.constant.UserConstant;
import com.abner.c1n.beans.po.UrlEntity;
import com.abner.c1n.dao.mapper.LogEntityMapper;
import com.abner.c1n.dao.mapper.UrlEntityMapper;
import com.abner.c1n.dao.mapper.UserEntityMapper;

/**
 * 自动清理任务
 * @author 01383518
 * @date:   2019年8月9日 下午4:40:17
 */
@Component
public class AutoCleanTask {
	
	private static final int DAY = 1;
	
	private static final int COUNT = 10;
	
	private static final int MINUTE = 30;
	
	@Resource
	private UrlEntityMapper urlEntityMapper;
	
	@Autowired
	private LogEntityMapper logEntityMapper;
	
	@Autowired
	private UserEntityMapper userEntityMapper;
	
	/**
	 * 清理未激活用户
	 */
	@Scheduled(cron = "0 0/5 * * * ?")
	public void cleanNotActiveUser(){
		userEntityMapper.deleteInitUserByTime(MINUTE);
	}
	
	/**
	 * 清理访问量低的短网址,（满一天访问量不足10次）
	 */
	@Scheduled(cron = "0 0 * * * ?")
	public void cleanAccessLowUrl() {
		List<UrlEntity> list = urlEntityMapper.queryUrlByUpdateTime(DAY);
		list.forEach(u -> {
			int count = logEntityMapper.queryCountByUrl(u.getShortUrl());
			if (count < COUNT && u.getUserId() == UserConstant.ADMIN_ID) {
				urlEntityMapper.deleteById(u.getId());
				logEntityMapper.deleteByUrl(u.getShortUrl());
			}
		});
	}
}
