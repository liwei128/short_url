package com.abner.c1n.controller.manage;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abner.c1n.beans.dto.monitorpage.MonitorPageCondition;
import com.abner.c1n.beans.po.MonitorPageEntity;
import com.abner.c1n.beans.po.MonitorPageEntity.PageType;
import com.abner.c1n.beans.vo.common.PagingData;
import com.abner.c1n.beans.vo.common.ResultData;
import com.abner.c1n.service.MonitorPageService;

/**
 * 监控页面管理
 * @author 01383518
 * @date:   2019年6月12日 上午9:21:09
 */
@RestController
@RequestMapping("/manage/page")
public class MonitorPageController {
	
	@Resource
	private MonitorPageService monitorPageService;
	
	@RequestMapping("/add")
	public ResultData<Void> add(PageType type,String url){
		return monitorPageService.add(type,url);
	}
	
	@RequestMapping("/delete")
	public ResultData<Void> delete(Long id){
		return monitorPageService.delete(id);
	}
	
	@RequestMapping("/list")
	public ResultData<PagingData<MonitorPageEntity>> list(MonitorPageCondition condition){
		return monitorPageService.list(condition);
	}

}
