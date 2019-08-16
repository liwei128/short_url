package com.abner.c1n.service;

import com.abner.c1n.beans.dto.blacklist.BlacklistCondition;
import com.abner.c1n.beans.po.BlacklistEntity.BlacklistStatus;
import com.abner.c1n.beans.vo.blacklist.BlacklistVo;
import com.abner.c1n.beans.vo.common.PagingData;
import com.abner.c1n.beans.vo.common.ResultData;

/**
 * 黑名单服务
 * @author 01383518
 * @date:   2019年8月6日 下午3:26:23
 */
public interface BlacklistService {
	
	/**
	 * 添加黑名单
	 * @param url
	 * @return
	 */
	public ResultData<Void> addBlacklist(String url);
	
	/**
	 * 是否在黑名单
	 * @param url
	 * @return
	 */
	public Boolean isBlacklist(String url);
	
	/**
	 * 删除黑名单
	 * @param id
	 * @return
	 */
	public ResultData<Void> delete(Long id);
	
	/**
	 * 启用/禁用
	 * @param id
	 * @param status
	 * @return
	 */
	ResultData<Void> modifyStatus(Long id, BlacklistStatus status);

	/**
	 * 列表查询
	 * @param condition
	 * @return
	 */
	public ResultData<PagingData<BlacklistVo>> list(BlacklistCondition condition);

}
