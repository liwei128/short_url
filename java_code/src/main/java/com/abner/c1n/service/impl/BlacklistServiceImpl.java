package com.abner.c1n.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.abner.c1n.beans.dto.blacklist.BlacklistCondition;
import com.abner.c1n.beans.enums.BaseRetCode;
import com.abner.c1n.beans.po.BlacklistEntity;
import com.abner.c1n.beans.po.BlacklistEntity.BlacklistStatus;
import com.abner.c1n.beans.vo.blacklist.BlacklistVo;
import com.abner.c1n.beans.vo.common.PagingData;
import com.abner.c1n.beans.vo.common.ResultData;
import com.abner.c1n.dao.mapper.BlacklistEntityMapper;
import com.abner.c1n.service.BlacklistService;
import com.abner.c1n.utils.ControllerUtil;

/**
 * 黑名单服务
 * @author 01383518
 * @date:   2019年8月6日 下午3:31:42
 */
@Service
public class BlacklistServiceImpl implements BlacklistService{
	
	@Resource
	private BlacklistEntityMapper  blacklistEntityMapper;

	@Override
	public ResultData<Void> addBlacklist(String url) {
		String domain = ControllerUtil.parseOneDomain(url);
		BlacklistEntity blacklistEntity = blacklistEntityMapper.queryByDomain(domain);
		if(blacklistEntity!=null){
			blacklistEntity.setStatus(BlacklistStatus.NORMAL);
			blacklistEntityMapper.updateById(blacklistEntity);
		}else{
			blacklistEntityMapper.insert(new BlacklistEntity(domain,BlacklistStatus.NORMAL));
		}
		return ResultData.getInstance();
	}

	@Override
	public ResultData<Void> delete(Long id) {
		int deleteById = blacklistEntityMapper.deleteById(id);
		return deleteById==1?ResultData.getInstance():ResultData.getInstance(BaseRetCode.FAIL);
	}

	@Override
	public ResultData<Void> modifyStatus(Long id, BlacklistStatus status) {
		if(id==null||status==null){
			return ResultData.getInstance(BaseRetCode.PARAM_MISSING);
		}
		BlacklistEntity blacklistEntity = new BlacklistEntity();
		blacklistEntity.setId(id);
		blacklistEntity.setStatus(status);
		int updateById = blacklistEntityMapper.updateById(blacklistEntity);
		return updateById==1?ResultData.getInstance():ResultData.getInstance(BaseRetCode.FAIL);
	}

	@Override
	public Boolean isBlacklist(String url) {
		String domain = ControllerUtil.parseOneDomain(url);
		BlacklistEntity blacklistEntity = blacklistEntityMapper.queryByDomain(domain);
		return blacklistEntity!=null&&blacklistEntity.getStatus()==BlacklistStatus.NORMAL;
	}

	@Override
	public ResultData<PagingData<BlacklistVo>> list(BlacklistCondition condition) {
		int count  = blacklistEntityMapper.queryCountByCondition(condition);
		List<BlacklistEntity> blacklists = blacklistEntityMapper.queryByCondition(condition);
		List<BlacklistVo> list = blacklists.stream().map(u->{
			return new BlacklistVo(u);
		}).collect(Collectors.toList());
		PagingData<BlacklistVo> pagingData = PagingData.getInstance(condition.getPageSize(),count,list);
		return ResultData.getInstance(pagingData);
	}

}
