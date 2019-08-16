package com.abner.c1n.service;

import com.abner.c1n.beans.dto.monitorpage.MonitorPageCondition;
import com.abner.c1n.beans.po.MonitorPageEntity;
import com.abner.c1n.beans.po.MonitorPageEntity.PageType;
import com.abner.c1n.beans.vo.common.PagingData;
import com.abner.c1n.beans.vo.common.ResultData;

/**
 * 监控页面
 * @author 01383518
 * @date:   2019年6月13日 下午1:57:15
 */
public interface MonitorPageService {

	/**
	 * 添加页面
	 * @param type
	 * @param url
	 * @return
	 */
	ResultData<Void> add(PageType type, String url);

	/**
	 * 删除页面
	 * @param id
	 * @return
	 */
	ResultData<Void> delete(Long id);

	/**
	 * 页面列表
	 * @param condition
	 * @return
	 */
	ResultData<PagingData<MonitorPageEntity>> list(MonitorPageCondition condition);

}
