package com.abner.c1n.service;

import com.abner.c1n.beans.bo.IpInfo;

/**
 * ip查询服务
 * @author 01383518
 * @date:   2019年7月15日 下午6:58:21
 */
public interface IpService {

	/**
	 * 查询ip详情
	 * @param ip
	 * @return
	 */
	IpInfo queryIpInfo(String ip);

}
