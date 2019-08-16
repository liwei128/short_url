package com.abner.c1n.service.impl;

import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abner.c1n.beans.constant.ShortLinkConstant;
import com.abner.c1n.beans.constant.SymbolConstant;
import com.abner.c1n.beans.constant.UserThreadLocal;
import com.abner.c1n.beans.dto.shorturl.LinkDto;
import com.abner.c1n.beans.dto.shorturl.ShortLinkCondition;
import com.abner.c1n.beans.enums.BaseRetCode;
import com.abner.c1n.beans.enums.ShortLinkEnum;
import com.abner.c1n.beans.po.UrlEntity;
import com.abner.c1n.beans.po.LogEntity.LogStatus;
import com.abner.c1n.beans.po.UrlEntity.Status;
import com.abner.c1n.beans.po.UrlEntity.Type;
import com.abner.c1n.beans.vo.common.PagingData;
import com.abner.c1n.beans.vo.common.ResultData;
import com.abner.c1n.beans.vo.common.RetCode;
import com.abner.c1n.config.SystemConfig;
import com.abner.c1n.dao.RedisDao;
import com.abner.c1n.dao.mapper.UrlEntityMapper;
import com.abner.c1n.service.BlacklistService;
import com.abner.c1n.service.LogInfoService;
import com.abner.c1n.service.ShortLinkService;
import com.abner.c1n.task.ShortUrlReviewTask;
import com.abner.c1n.utils.ControllerUtil;
import com.google.common.collect.Lists;

/**
 * 短链接服务
 * @author liwei
 * @date: 2018年8月24日 下午1:13:27
 *
 */
@Service
public class ShortLinkServiceImpl implements ShortLinkService{
	
	@Resource
	private RedisDao redisDao;
	
	@Resource
	private LogInfoService logInfoService;
	
	@Resource
	private UrlEntityMapper  urlEntityMapper;
	
	@Resource
	private	BlacklistService blacklistService;
	
	@Resource
	private	ShortUrlReviewTask shortUrlReviewTask;
	
	@Autowired
	private SystemConfig systemConfig;

	@Override
	public ResultData<String> shortLink(LinkDto linkDto) {
		//校验逻辑
		RetCode retCode = checkLinkDto(linkDto);
		if(!retCode.success()){
			return ResultData.getInstance(retCode);
		}
		//生成key
		ResultData<Void> keys  = generateKey(linkDto);
		if(!keys.success()){
			return ResultData.getInstance(keys);
		}
		//返回前端url
		String url = systemConfig.getUrlByKeyAndType(linkDto.getType(), linkDto.getKey());
		return ResultData.getInstance(url);
	}

	private ResultData<Void> generateKey(LinkDto linkDto) {
		//随机key
		if(StringUtils.isEmpty(linkDto.getKey())){
			//对于随机key,若数据库有url记录则不重新生成
			List<UrlEntity> urls = urlEntityMapper.queryByUrlAndType(linkDto.getUrl(),linkDto.getType());
			if(urls.size()>0){
				linkDto.setKey(urls.get(0).getKey());
				return ResultData.getInstance();
			}
			//生成随机key
			String key = RandomStringUtils.randomAlphanumeric(5);
			if(linkDto.getType()==Type.DOMAIN){
				key = key.toLowerCase();
			}
			//若key重复,则重新生成
			List<UrlEntity> count = urlEntityMapper.queryByKeyAndType(key,linkDto.getType());
			if(count.size()>0){
				return generateKey(linkDto);
			}
			linkDto.setKey(key);
		//指定key
		}else{
			List<UrlEntity> count = urlEntityMapper.queryByKeyAndType(linkDto.getKey(),linkDto.getType());
			if(count.size()>0){
				return ResultData.getInstance(ShortLinkEnum.EXIST);
			}
		}
		UrlEntity urlEntity = new UrlEntity(linkDto);
		urlEntityMapper.insert(urlEntity);
		shortUrlReviewTask.urlReview(urlEntity);
		return ResultData.getInstance();
	}

	private RetCode checkLinkDto(LinkDto linkDto) {
		// 非空校验
		if (linkDto == null || linkDto.getType() == null || StringUtils.isEmpty(linkDto.getUrl())) {
			return BaseRetCode.PARAM_MISSING;
		}
		if (!(linkDto.getUrl().startsWith(SymbolConstant.HTTP) || linkDto.getUrl().startsWith(SymbolConstant.HTTPS))) {
			linkDto.setUrl(SymbolConstant.HTTP + linkDto.getUrl());
		}
		// url内容校验
		if (!Pattern.matches(ShortLinkConstant.URL_MATCH, linkDto.getUrl())
				|| !linkDto.getUrl().contains(SymbolConstant.DOT)) {
			return ShortLinkEnum.ILLEGAL_URL;
		}
		// 不允许生成本站链接
		if (systemConfig.getDomain().equals(ControllerUtil.parseOneDomain(linkDto.getUrl()))) {
			return ShortLinkEnum.UNSUPPORTED_LINKS;
		}
		// 指定key的校验
		if (StringUtils.isNotEmpty(linkDto.getKey())) {
			if (linkDto.getType() == Type.URI && !Pattern.matches(ShortLinkConstant.KEY_MATCH, linkDto.getKey())) {
				return ShortLinkEnum.FORMAT_ERROR;
			}
			boolean domainCheck = linkDto.getType() == Type.DOMAIN
					&& (!Pattern.matches(ShortLinkConstant.KEY_LOW_MATCH, linkDto.getKey())
							|| ShortLinkConstant.WWW.equals(linkDto.getKey()));
			if (domainCheck) {
				return ShortLinkEnum.FORMAT_ERROR;
			}
		}
		// 黑名单校验
		if (blacklistService.isBlacklist(linkDto.getUrl())) {
			return ShortLinkEnum.BLACKLIST;
		}
		return BaseRetCode.SUCCESS;
	}

	@Override
	public String queryShortUrl(String key) {
		List<UrlEntity> list = Lists.newArrayList();
		//优先使用域名
		String[] domains = ControllerUtil.getDomainName().split("\\.");
		if(domains.length==SymbolConstant.THREE){
			list = urlEntityMapper.queryByKeyAndType(domains[0], Type.DOMAIN);
		}else if(StringUtils.isNotEmpty(key)){
			list = urlEntityMapper.queryByKeyAndType(key, Type.URI);
		}
		//解析、判断长网址是否合法
		return checkUrl(list);
	}

	/**
	 * 解析、判断长网址是否合法
	 * @param list
	 * @return
	 */
	private String checkUrl(List<UrlEntity> list) {
		if(list.size()==0){
			return systemConfig.getHomePage();
		}
		String url = list.get(0).getUrl();
		//判断是否被禁用或者加入黑名单
		boolean isDisable = list.get(0).getStatus() == Status.DISABLED || blacklistService.isBlacklist(url);
		//记录日志
		UserThreadLocal.getRequestInfo().setStatus(isDisable?LogStatus.FAIL:LogStatus.SUCCESS);
		logInfoService.addLog(UserThreadLocal.getRequestInfo());
		if(isDisable){
			return systemConfig.getDisabledPage();
		}
		return url;
	}

	@Override
	public ResultData<PagingData<UrlEntity>> urlList(ShortLinkCondition condition) {
		int count  = urlEntityMapper.queryCountByCondition(condition);
		List<UrlEntity> urlEntitys = urlEntityMapper.queryByCondition(condition);
		PagingData<UrlEntity> pagingData = PagingData.getInstance(condition.getPageSize(),count,urlEntitys);
		return ResultData.getInstance(pagingData);
	}
	@Override
	public ResultData<Void> modifyStatus(Long id, Status status){
		if(id==null||status==null){
			return ResultData.getInstance(BaseRetCode.PARAM_MISSING);
		}
		UrlEntity urlEntity = new UrlEntity();
		urlEntity.setId(id);
		urlEntity.setStatus(status);
		int updateById = urlEntityMapper.updateById(urlEntity);
		return updateById==1?ResultData.getInstance():ResultData.getInstance(BaseRetCode.FAIL);
	}

	@Override
	public ResultData<Void> update(Long id, String key, String url) {
		UrlEntity urlEntity = urlEntityMapper.selectById(id);
		if(StringUtils.isEmpty(key)&&StringUtils.isEmpty(url)){
			return ResultData.getInstance(BaseRetCode.PARAM_MISSING);
		}
		if(StringUtils.isNotEmpty(key)){
			urlEntity.updateKey(key);
		}
		if(StringUtils.isNotEmpty(url)){
			urlEntity.setUrl(url);
		}
		//校验合法性
		RetCode retCode = checkLinkDto(new LinkDto(urlEntity));
		if(!retCode.success()){
			return ResultData.getInstance(retCode);
		}
		//校验是否重复
		List<UrlEntity> count = urlEntityMapper.queryByKeyAndType(urlEntity.getKey(),urlEntity.getType());
		if(count.size()>0&&!count.get(0).getId().equals(id)){
			return ResultData.getInstance(ShortLinkEnum.EXIST);
		}
		urlEntityMapper.updateById(urlEntity);
		shortUrlReviewTask.urlReview(urlEntity);
		return ResultData.getInstance();
	}


	@Override
	public ResultData<Void> delete(Long id) {
		int deleteById = urlEntityMapper.deleteById(id);
		return deleteById==1?ResultData.getInstance():ResultData.getInstance(BaseRetCode.FAIL);
	}
	@Override
	public ResultData<Void> addBlacklist(Long id) {
		UrlEntity urlEntity = urlEntityMapper.selectById(id);
		if(urlEntity!=null){
			return blacklistService.addBlacklist(urlEntity.getUrl());
		}
		return ResultData.getInstance(BaseRetCode.FAIL);
	}
	
	@Override
	public ResultData<Void> addBlacklist(String safeId) {
		String id = redisDao.get(safeId);
		if(StringUtils.isEmpty(id)){
			return ResultData.getInstance(BaseRetCode.URL_FAILURE);
		}
		return addBlacklist(Long.parseLong(id));
	}

	@Override
	public ResultData<Void> disabled(String safeId) {
		String id = redisDao.get(safeId);
		if(StringUtils.isEmpty(id)){
			return ResultData.getInstance(BaseRetCode.URL_FAILURE);
		}
		return modifyStatus(Long.parseLong(id), Status.DISABLED);
	}

	

}
