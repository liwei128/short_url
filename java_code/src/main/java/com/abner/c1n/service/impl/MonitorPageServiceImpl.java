package com.abner.c1n.service.impl;

import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.abner.c1n.beans.constant.ShortLinkConstant;
import com.abner.c1n.beans.dto.monitorpage.MonitorPageCondition;
import com.abner.c1n.beans.enums.BaseRetCode;
import com.abner.c1n.beans.po.MonitorPageEntity;
import com.abner.c1n.beans.po.MonitorPageEntity.PageType;
import com.abner.c1n.beans.vo.common.PagingData;
import com.abner.c1n.beans.vo.common.ResultData;
import com.abner.c1n.dao.mapper.MonitorPageEntityMapper;
import com.abner.c1n.service.MonitorPageService;

/**
 * 监控页面
 * @author 01383518
 * @date:   2019年6月13日 下午1:57:41
 */
@Service
public class MonitorPageServiceImpl implements MonitorPageService{
	
	@Resource
	private MonitorPageEntityMapper  monitorPageEntityMapper;

	@Override
	public ResultData<Void> add(PageType type, String url) {
		if(type==null||StringUtils.isEmpty(url)){
			return ResultData.getInstance(BaseRetCode.PARAM_MISSING);
		}
		if(type==PageType.URL&&!Pattern.matches(ShortLinkConstant.URL_MATCH, url)){
			return ResultData.getInstance(BaseRetCode.FORMAT_ERROR);
		}
		if(type==PageType.DOMAIN&&!Pattern.matches(ShortLinkConstant.DOMAIN_MATCH, url)){
			return ResultData.getInstance(BaseRetCode.FORMAT_ERROR);
		}
		MonitorPageEntity pageEntity = new MonitorPageEntity(type,url);
		int insert = monitorPageEntityMapper.insert(pageEntity);
		return insert==1?ResultData.getInstance():ResultData.getInstance(BaseRetCode.FAIL);
	}

	@Override
	public ResultData<Void> delete(Long id) {
		int deleteById = monitorPageEntityMapper.deleteById(id);
		return deleteById==1?ResultData.getInstance():ResultData.getInstance(BaseRetCode.FAIL);
	}

	@Override
	public ResultData<PagingData<MonitorPageEntity>> list(MonitorPageCondition condition) {
		int count  = monitorPageEntityMapper.queryCountByCondition(condition);
		List<MonitorPageEntity> monitorPageEntitys = monitorPageEntityMapper.queryByCondition(condition);
		PagingData<MonitorPageEntity> pagingData = PagingData.getInstance(condition.getPageSize(),count,monitorPageEntitys);
		return ResultData.getInstance(pagingData);
	}

}
