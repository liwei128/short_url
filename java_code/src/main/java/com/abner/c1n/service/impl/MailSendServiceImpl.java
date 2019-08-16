package com.abner.c1n.service.impl;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.abner.c1n.service.MailSendService;

/**
 * 邮件服务
 * @author 01383518
 * @date:   2019年6月12日 下午12:30:42
 */
@Service
public class MailSendServiceImpl implements MailSendService{
	
	private static  Logger logger = LoggerFactory.getLogger(MailSendServiceImpl.class);
	
	/**
	 * 邮件内容后缀
	 */
	private static final String SUFFIX= "<br/>&nbsp;&nbsp;&nbsp;&nbsp;C1N短网址服务平台，随心所欲定制域名，定制网址。c1n.cn为您提供:短网址服务,数据实时统计,使你更加了解你的用户,准备好提升你的品牌,发掘新的受众了吗？";
	
	/**
	 * 邮件发送者昵称
	 */
	public final static String MAIL_FROM = "C1N短网址<%s>";
	
	/**
	 * 邮件主题
	 */
	private static final String SUBJECT = "C1N短网址-%s";
	
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String sender;
    
	@Override
	@Async
	public void sendVerificationCode(String to, String userName, String code) {
		StringBuffer body = new StringBuffer("亲爱的会员 ").append(userName).append("：您好！");
		body.append("<br/>&nbsp;&nbsp;&nbsp;&nbsp;您的验证码为：").append(code).append("，10分钟内有效。");
		sendMail(new String[]{to},"验证码",body.toString());
	}

	@Override
	@Async
	public void sendRegisterMail(String to, String userName,String activateUrl) {
		StringBuffer body = new StringBuffer("亲爱的会员 ").append(userName).append("：您好！");
		body.append("<br/>&nbsp;&nbsp;&nbsp;&nbsp;欢迎注册C1N短网址服务平台，<a href = '").append(activateUrl).append("'>请点击激活您的账号</a>，链接30分钟内有效。");
		sendMail(new String[]{to},"账号注册",body.toString());
	}
	
	@Override
	public void sendMail(String[] to,String subject,String body){
		try{
			MimeMessage message=mailSender.createMimeMessage();
	        MimeMessageHelper helper=new MimeMessageHelper(message,true);
	        
	        helper.setFrom(String.format(MAIL_FROM, sender));
	        helper.setTo(to);
	        helper.setSubject(String.format(SUBJECT, subject));
	        helper.setText(body+SUFFIX,true);
	        mailSender.send(message);
        }catch(Exception e){
        	logger.error("sendRegisterMail fail,to:{}",to,e);
        }
	}



}
