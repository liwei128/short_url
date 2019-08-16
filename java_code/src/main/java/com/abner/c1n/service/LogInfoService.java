package com.abner.c1n.service;


import java.util.List;
import java.util.Map;

import com.abner.c1n.beans.bo.RequestInfo;
import com.abner.c1n.beans.dto.logs.AttributeLogCondition;
import com.abner.c1n.beans.dto.logs.LogCondition;
import com.abner.c1n.beans.po.LogEntity;
import com.abner.c1n.beans.vo.common.PagingData;
import com.abner.c1n.beans.vo.common.ResultData;
import com.abner.c1n.beans.vo.url.Pv;
import com.abner.c1n.beans.vo.url.PvByAttribute;
import com.abner.c1n.beans.vo.url.PvByDate;

/**
 * 日志记录
 * @author 01383518
 * @date:   2019年6月6日 上午11:24:25
 */
public interface LogInfoService {
	
	/**
	 * 异步记录日志
	 * @param requestInfo
	 * @return
	 */
	public ResultData<Void> addLog(RequestInfo requestInfo);
	
	/**
	 * 日志记录
	 * @param url
	 * @return
	 */
	public ResultData<Void> addLog(String url);


	/**
	 * 查询总访问量
	 * @param condition
	 * @return
	 */
	public ResultData<Pv> totalVisits(LogCondition condition);

	/**
	 * 查询每天访问量
	 * @param condition
	 * @return
	 */
	public ResultData<Map<String,Object>> everydayPv(LogCondition condition);

	/**
	 * 24小时内访问统计
	 * @param condition
	 * @return
	 */
	public ResultData<List<PvByDate>> everyhourPv(LogCondition condition);

	/**
	 * 根据属性查询访问量
	 * @param condition
	 * @return
	 */
	public ResultData<List<PvByAttribute>> queryPvByAttribute(AttributeLogCondition condition);

	/**
	 * 访问记录明细
	 * @param condition
	 * @return
	 */
	public ResultData<PagingData<LogEntity>> record(LogCondition condition);
	
	/**
	 * 高频访问IP名单
	 * @param condition
	 * @return
	 */
	public ResultData<List<Map<String, String>>> oftenIp(LogCondition condition);





}
