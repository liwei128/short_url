package com.abner.c1n.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.abner.c1n.beans.dto.shorturl.ShortLinkCondition;
import com.abner.c1n.beans.po.UrlEntity;
import com.abner.c1n.beans.po.UrlEntity.Type;
/**
 * 短网址
 * @author 01383518
 * @date:   2019年6月6日 下午6:38:30
 */
public interface UrlEntityMapper {
	
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
    int insert(UrlEntity record);

    /**
     * 查询
     * @param id
     * @return
     */
    UrlEntity selectById(Long id);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateById(UrlEntity record);

    /**
     * 根据key和类型查询
     * @param key
     * @param type
     * @return
     */
	List<UrlEntity> queryByKeyAndType(@Param("key")String key, @Param("type")Type type);
	
	/**
	 * 查询短网址数量
	 * @param condition
	 * @return
	 */
	int queryCountByCondition(ShortLinkCondition condition);

	/**
	 * 查询短网址列表
	 * @param condition
	 * @return
	 */
	List<UrlEntity> queryByCondition(ShortLinkCondition condition);

	/**
	 * 根据url和类型查询
	 * @param url
	 * @param type
	 * @return
	 */
	List<UrlEntity> queryByUrlAndType(@Param("url")String url, @Param("type")Type type);

	/**
	 * 根据更新时间查询
	 * @param day
	 * @return
	 */
	List<UrlEntity> queryUrlByUpdateTime(int day);

}