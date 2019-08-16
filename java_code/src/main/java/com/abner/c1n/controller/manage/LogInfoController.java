package com.abner.c1n.controller.manage;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abner.c1n.beans.dto.logs.AttributeLogCondition;
import com.abner.c1n.beans.dto.logs.LogCondition;
import com.abner.c1n.beans.po.LogEntity;
import com.abner.c1n.beans.vo.common.PagingData;
import com.abner.c1n.beans.vo.common.ResultData;
import com.abner.c1n.beans.vo.url.Pv;
import com.abner.c1n.beans.vo.url.PvByAttribute;
import com.abner.c1n.beans.vo.url.PvByDate;
import com.abner.c1n.service.LogInfoService;


/**
 * 日志记录
 * @author 01383518
 * @date:   2019年3月8日 下午4:11:19
 */
@RestController
@RequestMapping("/manage/logs")
public class LogInfoController {
	
	@Resource
	private LogInfoService logInfoService;
	
	/**
	 * 查询总访问量
	 * @param condition
	 * @return
	 */
	@RequestMapping("/totalVisits")
	public ResultData<Pv> totalVisits(LogCondition condition){
		return logInfoService.totalVisits(condition);
	}
	
	/**
	 * 30天内的访问统计
	 * @param condition
	 * @return
	 */
	@RequestMapping("/everydayPv")
	public ResultData<Map<String,Object>> everydayPv(LogCondition condition){
		return logInfoService.everydayPv(condition);
	}
	
	/**
	 * 24小时内访问统计
	 * @param condition
	 * @return
	 */
	@RequestMapping("/everyhourPv")
	public ResultData<List<PvByDate>> everyhourPv(LogCondition condition){
		return logInfoService.everyhourPv(condition);
	}
	
	/**
	 * 根据属性查询访问量
	 * @param condition
	 * @return
	 */
	@RequestMapping("/queryPvByAttribute")
	public ResultData<List<PvByAttribute>> queryPvByAttribute(AttributeLogCondition condition){
		return logInfoService.queryPvByAttribute(condition.init());
	}
	
	
	
	/**
	 * 访问记录明细
	 * @param condition
	 * @return
	 */
	@RequestMapping("/recordInfo")
	public ResultData<PagingData<LogEntity>> record(LogCondition condition){
		return logInfoService.record(condition);
	}
	/**
	 * 高频访问IP名单
	 * @param condition
	 * @return
	 */
	@RequestMapping("/oftenIp")
	public ResultData<List<Map<String, String>>> oftenIp(LogCondition condition){
		return logInfoService.oftenIp(condition);
	}

}
