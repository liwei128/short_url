package com.abner.c1n.dao.mapper;

import java.util.List;
import java.util.Map;

import com.abner.c1n.beans.dto.logs.AttributeLogCondition;
import com.abner.c1n.beans.dto.logs.LogCondition;
import com.abner.c1n.beans.po.LogEntity;
import com.abner.c1n.beans.vo.url.Pv;
import com.abner.c1n.beans.vo.url.PvByAttribute;
import com.abner.c1n.beans.vo.url.PvByDate;
/**
 * 日志
 * @author 01383518
 * @date:   2019年6月6日 下午6:37:46
 */
public interface LogEntityMapper {
	
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
    int insert(LogEntity record);

    /**
     * 查询
     * @param id
     * @return
     */
    LogEntity selectById(Long id);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateById(LogEntity record);

	/**
	 * 查询访问量
	 * @param condition
	 * @return
	 */
    Pv queryVisits(LogCondition condition);
	
	/**
	 * 查询每天访问量
	 * @param condition
	 * @return
	 */
	List<PvByDate> queryEverydayPv(LogCondition condition);

	/**
	 * 查询每小时访问量
	 * @param condition
	 * @return
	 */
	List<PvByDate> everyhourPv(LogCondition condition);
	
	/**
	 * 查询记录数
	 * @param condition
	 * @return
	 */
	int queryCountByCondition(LogCondition condition);

	/**
	 * 查询记录明细
	 * @param condition
	 * @return
	 */
	List<LogEntity> queryByCondition(LogCondition condition);

	/**
	 * 根据属性查询访问量
	 * @param condition
	 * @return
	 */
	List<PvByAttribute> queryPvByAttribute(AttributeLogCondition condition);
	
	/**
	 * 高频访问IP名单
	 * @param condition
	 * @return
	 */
	List<Map<String, String>> oftenIp(LogCondition condition);

	/**
	 * 查询访问量
	 * @param shortUrl
	 * @return
	 */
	int queryCountByUrl(String shortUrl);

	/**
	 * 删除日志
	 * @param shortUrl
	 * @return
	 */
	int deleteByUrl(String shortUrl);
}