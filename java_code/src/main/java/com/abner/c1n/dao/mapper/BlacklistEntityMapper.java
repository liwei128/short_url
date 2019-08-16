package com.abner.c1n.dao.mapper;

import java.util.List;

import com.abner.c1n.beans.dto.blacklist.BlacklistCondition;
import com.abner.c1n.beans.po.BlacklistEntity;

/**
 * 黑名单
 * @author 01383518
 * @date:   2019年8月6日 下午3:22:31
 */
public interface BlacklistEntityMapper {
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
    int deleteById(Long id);

    /**
     * 插入
     * @param record
     * @return
     */
    int insert(BlacklistEntity record);


    /**
     * 查询
     * @param id
     * @return
     */
    BlacklistEntity selectById(Long id);


    /**
     * 更新
     * @param record
     * @return
     */
    int updateById(BlacklistEntity record);

    
    /**
     * 根据域名查询
     * @param domain
     * @return
     */
	BlacklistEntity queryByDomain(String domain);

	/**
	 * 根据条件查询
	 * @param condition
	 * @return
	 */
	int queryCountByCondition(BlacklistCondition condition);

	/**
	 * 根据条件查询
	 * @param condition
	 * @return
	 */
	List<BlacklistEntity> queryByCondition(BlacklistCondition condition);
}