package com.abner.c1n.controller.manage;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abner.c1n.annotation.Admin;
import com.abner.c1n.beans.dto.shorturl.ShortLinkCondition;
import com.abner.c1n.beans.po.UrlEntity;
import com.abner.c1n.beans.po.UrlEntity.Status;
import com.abner.c1n.beans.vo.common.PagingData;
import com.abner.c1n.beans.vo.common.ResultData;
import com.abner.c1n.service.ShortLinkService;

/**
 * 短网址管理
 * @author 01383518
 * @date:   2019年6月12日 上午9:20:01
 */
@RestController
@RequestMapping("/manage/shortUrl")
public class ShortUrlController {
	
	@Resource
	private ShortLinkService shortLinkService;
	
	
	/**
	 * 短网址列表
	 * @param condition
	 * @return
	 */
	@RequestMapping("")
	public ResultData<PagingData<UrlEntity>> urlList(ShortLinkCondition condition){
		return shortLinkService.urlList(condition);
	}
	
	/**
	 * 短网址启用/禁用
	 * @param id
	 * @return
	 */
	@RequestMapping("/modifyStatus")
	public ResultData<Void> modifyStatus(Long id,Status status){
		return shortLinkService.modifyStatus(id,status);
	}
	
	/**
	 * 修改短网址
	 * @param id
	 * @param key
	 * @param url
	 * @return
	 */
	@RequestMapping("/update")
	public ResultData<Void> update(Long id,String key,String url){
		return shortLinkService.update(id,key,url);
	}
	
	/**
	 * 删除短网址
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	public ResultData<Void> delete(Long id){
		return shortLinkService.delete(id);
	}
	
	/**
	 * 一键封禁
	 * @param id
	 * @return
	 */
	@Admin
	@RequestMapping("/addBlacklist")
	public ResultData<Void> addBlacklist(Long id){
		return shortLinkService.addBlacklist(id);
	}

}
