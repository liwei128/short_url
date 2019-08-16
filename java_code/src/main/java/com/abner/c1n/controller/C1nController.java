package com.abner.c1n.controller;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abner.c1n.annotation.Slf4j;
import com.abner.c1n.beans.dto.shorturl.LinkDto;
import com.abner.c1n.beans.vo.common.ResultData;
import com.abner.c1n.service.LogInfoService;
import com.abner.c1n.service.ShortLinkService;

/**
 * 短链接服务,对外的公共数据入口
 * @author liwei
 * @date: 2018年8月23日 上午10:12:54
 *
 */
@Controller
public class C1nController {
	
	@Resource
	private ShortLinkService shortLinkService;
	
	@Resource
	private LogInfoService logInfoService;

	/**
	 * 生成短链接
	 * @param linkDto
	 * @return
	 */
	@RequestMapping("/link/short")
	@ResponseBody
	@Slf4j("generate shortUrl")
	public ResultData<String> generate(LinkDto linkDto) {
		return shortLinkService.shortLink(linkDto);
	}
	
	/**
	 * 禁用短链接
	 * @param id
	 * @return
	 */
	@RequestMapping("/link/disabled")
	@ResponseBody
	public ResultData<Void> disabled(String id) {
		return shortLinkService.disabled(id);
	}
	
	/**
	 * 一键封禁
	 * @param id
	 * @return
	 */
	@RequestMapping("/link/addBlacklist")
	@ResponseBody
	public ResultData<Void> addBlacklist(String id) {
		return shortLinkService.addBlacklist(id);
	}
	
	/**
	 * 记录日志
	 * @param url
	 * @return
	 */
	@RequestMapping("/logs/record")
	@ResponseBody
	public ResultData<Void> record(String url){
		return logInfoService.addLog(url);
	}
	
	/**
	 * 通过uri访问短网址
	 * @param key
	 * @return
	 */
	@RequestMapping("/{key}")
	public String get(@PathVariable("key") String key) {
		String longLink = shortLinkService.queryShortUrl(key);
		return "redirect:" + longLink;
	}
	/**
	 * 通过域名访问短网址
	 * @return
	 */
	@RequestMapping("/")
	public String get() {
		String longLink = shortLinkService.queryShortUrl(null);
		return "redirect:" + longLink;
	}
	
	
	
	

}
