package com.abner.c1n.dao.mapper;

import java.util.List;

import com.abner.c1n.beans.dto.monitorpage.MonitorPageCondition;
import com.abner.c1n.beans.po.MonitorPageEntity;

/**
 * 监控页面
 * @author 01383518
 * @date:   2019年6月13日 下午1:53:46
 */
public interface MonitorPageEntityMapper {
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
    int insert(MonitorPageEntity record);

    /**
     * 查询
     * @param id
     * @return
     */
    MonitorPageEntity selectById(Long id);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateById(MonitorPageEntity record);

    /**
     * 查询数量
     * @param condition
     * @return
     */
	int queryCountByCondition(MonitorPageCondition condition);

	/**
	 * 查询列表
	 * @param condition
	 * @return
	 */
	List<MonitorPageEntity> queryByCondition(MonitorPageCondition condition);
}