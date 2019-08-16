package com.abner.c1n.beans.constant;


/**
 * 短链接常量
 * @author liwei
 * @date: 2018年8月23日 上午10:22:14
 *
 */
public class ShortLinkConstant {
	
	/**
	 * 原网址格式校验
	 */
	public final static String URL_MATCH = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
	
	/**
	 * 自定义域名的校验
	 */
	public final static String KEY_LOW_MATCH = "^[0-9a-z]+$";
	
	/**
	 * 自定义uri的校验
	 */
	public final static String KEY_MATCH = "^[0-9a-zA-Z]+$";
	
	/**
	 * 域名格式校验
	 */
	public final static String DOMAIN_MATCH = "[a-z0-9][-a-z0-9]{0,62}(\\.[a-z0-9][-a-z0-9]{0,62})+\\.?";
	
	
	/**
	 *保留www不允许生成
	 */
	public final static String WWW = "www";
	
	/**
	 * 用户记录用户uv数
	 */
	public final static String COOKIES_NAME = "liwei";
	
	

}
