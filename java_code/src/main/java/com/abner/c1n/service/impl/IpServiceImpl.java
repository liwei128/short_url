package com.abner.c1n.service.impl;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.abner.c1n.beans.bo.IpInfo;
import com.abner.c1n.beans.constant.SymbolConstant;
import com.abner.c1n.service.IpService;
import com.abner.c1n.utils.HttpClientUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

/**
 * ip查询服务
 * 官网http://user.ip138.com/
 * 价格
 * 20次/积分,100积分/元
 * @author 01383518
 * @date:   2019年7月15日 下午6:59:46
 */
@Service
public class IpServiceImpl implements IpService{
	
	@Value("${ip138.token}")
	private String token;
	
	@Value("${ip138.url}")
	private String ip138;
	
	@Cacheable("ip")
	@Override
	public IpInfo queryIpInfo(String  ip){
		Map<String, String> headers = Maps.newHashMap();
		headers.put("token", token);
		StringBuilder builder = new StringBuilder("?");
		String params = builder.append("ip=").append(ip)
		.append("&datatype=jsonp").toString();
		String ret = HttpClientUtils.get(ip138+params, headers);
		return parseIpInfo(ret);
	}

	private static IpInfo parseIpInfo(String ret) {
		IpInfo ipInfo = new IpInfo();
		JSONObject jsonObject = JSON.parseObject(ret);
		if(!SymbolConstant.OK.equals(jsonObject.getString(SymbolConstant.RET))){
			return  ipInfo;
		}
		ipInfo.setIp(jsonObject.getString("ip"));
		JSONArray info = jsonObject.getJSONArray("data");
		ipInfo.setCountry(info.getString(0));
		ipInfo.setProvince(info.getString(1));
		ipInfo.setCity(info.getString(2));
		ipInfo.setNetworkOperator(info.getString(3));
		return ipInfo;
	}

}
