package com.abner.c1n.controller.manage;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abner.c1n.annotation.Admin;
import com.abner.c1n.beans.dto.blacklist.BlacklistCondition;
import com.abner.c1n.beans.po.BlacklistEntity.BlacklistStatus;
import com.abner.c1n.beans.vo.blacklist.BlacklistVo;
import com.abner.c1n.beans.vo.common.PagingData;
import com.abner.c1n.beans.vo.common.ResultData;
import com.abner.c1n.service.BlacklistService;

/**
 * 黑名单管理
 * @author 01383518
 * @date:   2019年8月6日 下午3:56:19
 */
@Controller
@RequestMapping("/manage/blacklist")
public class BlacklistController {
	
	
	@Resource
	private BlacklistService blacklistService;
	
	/**
	 * 查询用户列表,只有管理员能访问
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	@Admin
	public ResultData<PagingData<BlacklistVo>> list(BlacklistCondition condition){
		return blacklistService.list(condition);
	}
	
	/**
	 * 启用/禁用
	 * @param id
	 * @param status
	 * @return
	 */
	@Admin
	@ResponseBody
	@RequestMapping("/modifyStatus")
	public ResultData<Void> modifyStatus(Long id,BlacklistStatus status){
		return blacklistService.modifyStatus(id,status);
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@Admin
	@ResponseBody
	@RequestMapping("/delete")
	public ResultData<Void> delete(Long id){
		return blacklistService.delete(id);
	}

}
