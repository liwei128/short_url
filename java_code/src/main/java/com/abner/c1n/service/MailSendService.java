package com.abner.c1n.service;

/**
 * 邮件发送服务
 * @author 01383518
 * @date:   2019年6月12日 下午12:30:05
 */
public interface MailSendService {

	/**
	 * 发送注册邮件
	 * @param to
	 * @param userName
	 * @param activateUrl
	 */
	void sendRegisterMail(String to, String userName,String activateUrl);

	/**
	 * 发送验证码
	 * @param to
	 * @param userName
	 * @param code
	 */
	void sendVerificationCode(String to, String userName, String code);
	
	/**
	 * 发送邮件
	 * @param to
	 * @param subject
	 * @param body
	 */
	void sendMail(String[] to,String subject,String body);

}
