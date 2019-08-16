package com.abner.c1n.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import com.abner.c1n.beans.po.UrlEntity.Type;

/**
 * 系统配置
 * @author LW
 * @time 2019年6月9日 下午6:52:16
 */
@Component
@ConfigurationProperties(prefix="system")
public class SystemConfig {
	
	private static final String DISABLED_PAGE = "http://www.%s/disabled.html";
	private static final String ABSENT_PAGE = "http://www.%s/404.html";
	private static final String HOME_PAGE = "http://www.%s";
	private static final String URL_DOMIN = "http://%s.%s";
	private static final String URL_URI = "http://%s/%s";
	private static final String ACTIVATE_URL = "http://%s/manage/user/activate?key=%s";
	
	private static final String DISABLED_URL = "http://%s/link/disabled";
	private static final String BLACKLIST_URL = "http://%s/link/addBlacklist";
	
	/**
	 * 需要验证登录的uri
	 */
	private String needLogin;
	
	/**
	 * 不需要登录的uri
	 */
	private String[] ignoreLoginUri;
	
	/**
	 * 提醒邮箱
	 */
	private String[] reminderMail;
	
	/**
	 * 短网址后台域名
	 */
	private String domain;
	


	public String getDomain() {
		return domain;
	}


	public void setDomain(String domain) {
		this.domain = domain;
	}


	public String getNeedLogin() {
		return needLogin;
	}

	public void setNeedLogin(String needLogin) {
		this.needLogin = needLogin;
	}

	
	public String[] getIgnoreLoginUri() {
		return ignoreLoginUri;
	}

	public void setIgnoreLoginUri(String[] ignoreLoginUri) {
		this.ignoreLoginUri = ignoreLoginUri;
	}

	public String[] getReminderMail() {
		return reminderMail;
	}

	public void setReminderMail(String[] reminderMail) {
		this.reminderMail = reminderMail;
	}
	
	public String getDisabledUrl() {
		return String.format(DISABLED_URL, domain);
	}


	public String getBlacklistUrl() {
		return String.format(BLACKLIST_URL, domain);
	}

	public String getDisabledPage() {
		return String.format(DISABLED_PAGE, domain);
	}


	public String getAbsentPage() {
		return String.format(ABSENT_PAGE, domain);
	}


	public String getHomePage() {
		return String.format(HOME_PAGE, domain);
	}

	/**
	 * 根据key和类型来生成短网址
	 * @param type
	 * @param key
	 * @return
	 */
	public String getUrlByKeyAndType(Type type, String key) {
		if (type == Type.DOMAIN) {
			return String.format(URL_DOMIN, key, domain);
		}
		return String.format(URL_URI, domain, key);
	}
	
	public String getActivateUrl(String key) {
		return String.format(ACTIVATE_URL, domain, key);
	}
	
}
