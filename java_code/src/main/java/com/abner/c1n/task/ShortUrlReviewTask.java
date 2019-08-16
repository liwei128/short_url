package com.abner.c1n.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.abner.c1n.beans.constant.UserConstant;
import com.abner.c1n.beans.po.UrlEntity;
import com.abner.c1n.config.SystemConfig;
import com.abner.c1n.config.thread.AsyncThreadPool;
import com.abner.c1n.dao.RedisDao;
import com.abner.c1n.service.MailSendService;
import com.abner.c1n.utils.EncryptionUtils;
import com.google.common.collect.Lists;


/**
 * 短网址定时审核任务
 * @author 01383518
 * @date:   2019年8月7日 上午10:17:56
 */
@Component
public class ShortUrlReviewTask {
	
	@Autowired
	private AsyncThreadPool asyncThreadPool;
	
	@Autowired
	private MailSendService mailSendService;
	
	@Autowired
	private SystemConfig systemConfig;
	
	@Resource
	private RedisDao redisDao;
	
	private final static int MAX_URL_COUNT = 16;
	
	private final static String DISABLED = "<a href = '%s?id=%s' >禁用</a>";
	
	private final static String BLACKLIST = "<a href = '%s?id=%s' >一键封禁</a>";
	
	private LinkedBlockingQueue<UrlEntity> urlQueue = new LinkedBlockingQueue<>();
	

	
	/**
	 * 添加短网址到队列，并发送邮件进行审核
	 * @param url
	 */
	public void urlReview(UrlEntity url) {
		url.setCreateTime(new Date());
		urlQueue.offer(url);
		if (urlQueue.size() >= MAX_URL_COUNT) {
			asyncThreadPool.execute(() -> {
				mailReminder();
			});
		}
	}
	
	/**
	 * 邮件提醒，2分钟一次
	 */
	@Scheduled(cron = "0 0/2 * * * ?")
	public void mailReminder() {
		List<UrlEntity> urls = getUrlList();
		if(urls.size()==0){
			return;
		}
		String body = builderMailBody(urls);
		mailSendService.sendMail(systemConfig.getReminderMail(), "网址审核", body);
	}


	/**
	 * 构建邮件内容
	 * @param urls
	 * @return
	 */
	private String builderMailBody(List<UrlEntity> urls) {
		StringBuilder builder = new StringBuilder("<table border='1' cellspacing='0' cellpadding='0'>");
		builder.append("<tr><th>时间</th><th>短网址</th><th>原网址</th><th>用户ID</th><th>操作</th></tr>");
		urls.forEach(url->{
			String id = generateSafeId(url.getId());
			String disabledBody  = String.format(DISABLED, systemConfig.getDisabledUrl(),id);
			String blacklistBody  = String.format(BLACKLIST, systemConfig.getBlacklistUrl(),id);
			builder.append("<tr>")
			.append("<td>").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(url.getCreateTime())).append("</td>")
			.append("<td>").append(url.getShortUrl()).append("</td>")
			.append("<td>").append(url.getUrl()).append("</td>")
			.append("<td>").append(url.getUserId()).append("</td>")
			.append("<td>").append(disabledBody+"|"+blacklistBody).append("</td>")
			.append("</tr>");
		});
		builder.append("</table>");
		return builder.toString();
	}

	/**
	 * 生成安全ID
	 * @param id
	 * @return
	 */
	private String generateSafeId(Long id) {
		String safeId = UserConstant.SAFE_ID+id+EncryptionUtils.random(6);
		redisDao.set(safeId, String.valueOf(id), 12, TimeUnit.HOURS);
		return safeId;
	}
	/**
	 * 获取队列中的短网址
	 * @return
	 */
	private List<UrlEntity> getUrlList() {
		List<UrlEntity> urls = Lists.newArrayList();
		UrlEntity url = null;
		while ((url = urlQueue.poll()) != null) {
			urls.add(url);
		}
		return urls;
	}

}
