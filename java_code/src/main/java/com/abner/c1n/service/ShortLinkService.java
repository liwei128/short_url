package com.abner.c1n.service;

import com.abner.c1n.beans.dto.shorturl.LinkDto;
import com.abner.c1n.beans.dto.shorturl.ShortLinkCondition;
import com.abner.c1n.beans.po.UrlEntity;
import com.abner.c1n.beans.po.UrlEntity.Status;
import com.abner.c1n.beans.vo.common.PagingData;
import com.abner.c1n.beans.vo.common.ResultData;

/**
 * 短链接服务
 * @author liwei
 * @date: 2018年8月24日 下午1:12:30
 *
 */
public interface ShortLinkService {

	/**
	 * 生成短网址
	 * @param linkDto
	 * @return
	 */
	ResultData<String> shortLink(LinkDto linkDto);

	/**
	 * 查询短网址
	 * @param key
	 * @return
	 */
	String queryShortUrl(String key);

	/**
	 * 查询短网址列表
	 * @param condition
	 * @return
	 */
	ResultData<PagingData<UrlEntity>> urlList(ShortLinkCondition condition);

	/**
	 * 修改状态
	 * @param id
	 * @param status
	 * @return
	 */
	ResultData<Void> modifyStatus(Long id, Status status);

	/**
	 * 修改短网址
	 * @param id
	 * @param key
	 * @param url
	 * @return
	 */
	ResultData<Void> update(Long id, String key, String url);


	/**
	 * 删除短网址
	 * @param id
	 * @return
	 */
	ResultData<Void> delete(Long id);

	/**
	 * 一键加入黑名单
	 * @param id
	 * @return
	 */
	ResultData<Void> addBlacklist(Long id);


	

}
