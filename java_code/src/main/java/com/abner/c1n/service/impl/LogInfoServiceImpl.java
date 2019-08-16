package com.abner.c1n.service.impl;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abner.c1n.beans.bo.IpInfo;
import com.abner.c1n.beans.bo.RequestInfo;
import com.abner.c1n.beans.constant.ShortLinkConstant;
import com.abner.c1n.beans.constant.SymbolConstant;
import com.abner.c1n.beans.constant.UserThreadLocal;
import com.abner.c1n.beans.dto.logs.AttributeLogCondition;
import com.abner.c1n.beans.dto.logs.LogCondition;
import com.abner.c1n.beans.enums.BaseRetCode;
import com.abner.c1n.beans.po.LogEntity;
import com.abner.c1n.beans.po.MonitorPageEntity;
import com.abner.c1n.beans.po.UrlEntity;
import com.abner.c1n.beans.po.MonitorPageEntity.PageType;
import com.abner.c1n.beans.vo.common.PagingData;
import com.abner.c1n.beans.vo.common.ResultData;
import com.abner.c1n.beans.vo.common.RetCode;
import com.abner.c1n.beans.vo.url.Pv;
import com.abner.c1n.beans.vo.url.PvByAttribute;
import com.abner.c1n.beans.vo.url.PvByDate;
import com.abner.c1n.config.thread.AsyncThreadPool;
import com.abner.c1n.dao.mapper.LogEntityMapper;
import com.abner.c1n.dao.mapper.MonitorPageEntityMapper;
import com.abner.c1n.dao.mapper.UrlEntityMapper;
import com.abner.c1n.service.IpService;
import com.abner.c1n.service.LogInfoService;
import com.abner.c1n.utils.CalendarUtils;
import com.abner.c1n.utils.ControllerUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 日志记录
 * @author 01383518
 * @date:   2019年6月6日 上午11:24:04
 */
@Service
public class LogInfoServiceImpl implements LogInfoService{

	@Autowired
	private LogEntityMapper logEntityMapper;
	
	@Autowired
	private AsyncThreadPool asyncThreadPool;
	
	@Autowired
	private IpService ipService;
	
	@Resource
	private UrlEntityMapper  urlEntityMapper;
	
	@Resource
	private MonitorPageEntityMapper  monitorPageEntityMapper;
	
	@Override
	public ResultData<Void> addLog(String url) {
		if(!Pattern.matches(ShortLinkConstant.URL_MATCH, url)){
			return ResultData.getInstance(BaseRetCode.PARAM_MISSING);
		}
		RequestInfo requestInfo = UserThreadLocal.getRequestInfo();
		requestInfo.setUrl(ControllerUtil.parseUrl(url));
		requestInfo.setDomain(ControllerUtil.parseDomain(url));
		return addLog(requestInfo);
	}


	
	@Override
	public ResultData<Void> addLog(RequestInfo requestInfo) {
		asyncThreadPool.execute(()->{
			LogEntity logEntity = new LogEntity(requestInfo);
			IpInfo ipInfo =  ipService.queryIpInfo(requestInfo.getIp());
			logEntity.builderIpInfo(ipInfo);
			logEntityMapper.insert(logEntity);
		});
		return ResultData.getInstance();
	}
	@Override
	public ResultData<Pv> totalVisits(LogCondition condition) {
		RetCode initUrl = initUrl(condition);
		if(!initUrl.success()){
			return ResultData.getInstance(initUrl);
		}
		Pv visits = logEntityMapper.queryVisits(condition);
		visits.initUrl(condition);
		return ResultData.getInstance(visits);
	}
	/**
	 * 通过链接id解析出监控url，以及判断是否属于当前用户
	 * @param condition
	 * @return
	 */
	private RetCode initUrl(LogCondition condition) {
		if(StringUtils.isEmpty(condition.getUrlId())){
			return BaseRetCode.PARAM_MISSING;
		}
		condition.setDomain(null);
		condition.setUrl(null);
		if(SymbolConstant.ALL.equals(condition.getUrlId())){
			return UserThreadLocal.isAdmin()?BaseRetCode.SUCCESS:BaseRetCode.NOT_PERMISSION;
		}

		String[] split = condition.getUrlId().split("-");
		Long id = Long.parseLong(split[1]);
		String type = split[0];
		if(SymbolConstant.A.equals(type)){
			UrlEntity urlEntity = urlEntityMapper.selectById(id);
			if(urlEntity == null || !condition.compareUserId(urlEntity.getUserId())){
				return BaseRetCode.NOT_PERMISSION;
			}
			condition.setUrl(urlEntity.getShortUrl());
			return BaseRetCode.SUCCESS;
		}
		if(SymbolConstant.B.equals(type)){
			MonitorPageEntity monitorPageEntity = monitorPageEntityMapper.selectById(id);
			if(monitorPageEntity == null ||!condition.compareUserId(monitorPageEntity.getUserId())){
				return BaseRetCode.NOT_PERMISSION;
			}
			if(monitorPageEntity.getType()==PageType.DOMAIN){
				condition.setDomain(monitorPageEntity.getUrl());
			}else{
				condition.setUrl(monitorPageEntity.getUrl());
			}
			return BaseRetCode.SUCCESS;
		}
		return BaseRetCode.FORMAT_ERROR;
	}



	@Override
	public ResultData<Map<String,Object>> everydayPv(LogCondition condition) {
		RetCode initUrl = initUrl(condition);
		if(!initUrl.success()){
			return ResultData.getInstance(initUrl);
		}
		//查询今天 、昨天、 7天、 30天的访问总量
		LogCondition todayCondition = condition.today();
		LogCondition yesterdayCondition = condition.yesterday();
		LogCondition weekCondition = condition.week();
		LogCondition monthCondition = condition.month();

		Map<String,Object> result = Maps.newHashMap();
		result.put(todayCondition.getStartDate(), logEntityMapper.queryVisits(todayCondition));
		result.put(yesterdayCondition.getStartDate(), logEntityMapper.queryVisits(yesterdayCondition));
		result.put("week", logEntityMapper.queryVisits(weekCondition));
		result.put("month", logEntityMapper.queryVisits(monthCondition));
		//查询30天每天访问量
		List<PvByDate> list = logEntityMapper.queryEverydayPv(monthCondition);
		List<PvByDate> pvByDates = Lists.newArrayList();
		//补齐空缺数据
		for(int i=0;i<=SymbolConstant.MONTH;i++){
			String day = CalendarUtils.formatTimeByDay(monthCondition.getStartDate(),i);
			pvByDates.add(getPvData(list,day));
		}
		result.put("everyday", pvByDates);
		return ResultData.getInstance(result);
	}
	@Override
	public ResultData<List<PvByDate>> everyhourPv(LogCondition condition) {
		RetCode initUrl = initUrl(condition);
		if(!initUrl.success()){
			return ResultData.getInstance(initUrl);
		}
		String endDate = StringUtils.isNotEmpty(condition.getEndDate())?condition.getEndDate()+" 23":CalendarUtils.currentTimeByHour();
		condition.setEndDate(endDate);
		condition.setStartDate(CalendarUtils.formatTimeByHour(endDate,-23));
		List<PvByDate> list = logEntityMapper.everyhourPv(condition);
		List<PvByDate> pvByDates = Lists.newArrayList();
		//补齐空缺数据
		for(int i=0;i<=SymbolConstant.DAY;i++){
			String day = CalendarUtils.formatTimeByHour(condition.getStartDate(),i);
			pvByDates.add(getPvData(list,day));
		}
		return ResultData.getInstance(pvByDates);
	}
	private PvByDate getPvData(List<PvByDate> list, String day) {
		Optional<PvByDate> findFirst = list.stream().filter(v->{
			return v.getLogDate().equals(day);
		}).findFirst();
		if(findFirst.isPresent()){
			return findFirst.get();
		}
		return new PvByDate(day);
	}
	@Override
	public ResultData<List<PvByAttribute>> queryPvByAttribute(AttributeLogCondition condition) {
		RetCode initUrl = initUrl(condition);
		if(!initUrl.success()){
			return ResultData.getInstance(initUrl);
		}
		if(condition.getAttribute()==null){
			return ResultData.getInstance(BaseRetCode.PARAM_MISSING);
		}
		List<PvByAttribute> pv = logEntityMapper.queryPvByAttribute(condition);
		return ResultData.getInstance(pv);
	}
	
	@Override
	public ResultData<PagingData<LogEntity>> record(LogCondition condition) {
		RetCode initUrl = initUrl(condition);
		if(!initUrl.success()){
			return ResultData.getInstance(initUrl);
		}
		int count  = logEntityMapper.queryCountByCondition(condition);
		List<LogEntity> logEntitys = logEntityMapper.queryByCondition(condition);
		PagingData<LogEntity> pagingData = PagingData.getInstance(condition.getPageSize(),count,logEntitys);
		return ResultData.getInstance(pagingData);
	}
	@Override
	public ResultData<List<Map<String, String>>> oftenIp(LogCondition condition) {
		RetCode initUrl = initUrl(condition);
		if(!initUrl.success()){
			return ResultData.getInstance(initUrl);
		}
		List<Map<String, String>> visits = logEntityMapper.oftenIp(condition);
		return ResultData.getInstance(visits);
	}



}
