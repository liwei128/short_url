package com.abner.c1n.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abner.c1n.beans.bo.UserInfoBo;
import com.abner.c1n.beans.constant.UserConstant;
import com.abner.c1n.beans.constant.UserThreadLocal;
import com.abner.c1n.beans.dto.user.UserListCondition;
import com.abner.c1n.beans.enums.BaseRetCode;
import com.abner.c1n.beans.enums.UserEnum;
import com.abner.c1n.beans.po.UserEntity;
import com.abner.c1n.beans.po.UserEntity.UserStatus;
import com.abner.c1n.beans.vo.common.PagingData;
import com.abner.c1n.beans.vo.common.ResultData;
import com.abner.c1n.beans.vo.common.RetCode;
import com.abner.c1n.beans.vo.user.UserVo;
import com.abner.c1n.config.SystemConfig;
import com.abner.c1n.dao.RedisDao;
import com.abner.c1n.dao.mapper.UserEntityMapper;
import com.abner.c1n.service.MailSendService;
import com.abner.c1n.service.UserService;
import com.abner.c1n.utils.ControllerUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

/**
 * 用户相关服务
 * @author 01383518
 * @date:   2019年6月12日 上午9:31:36
 */
@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserEntityMapper userEntityMapper;
	
	@Autowired
	private RedisDao redisDao;
	
	@Autowired
	private MailSendService mailSendService;
	
	@Autowired
	private SystemConfig systemConfig;
	


	@Override
	public ResultData<Void> exit() {
		Map<String,String> cookies = Maps.newHashMap();
		cookies.put(UserConstant.TOKEN, "");
		ControllerUtil.setCookie(cookies, -1);
		if(UserThreadLocal.isLogin()){
			UserEntity currentUser = UserThreadLocal.getCurrentUser();
			redisDao.delete(currentUser.generateToken());
		}
		return ResultData.getInstance();
	}

	/**
	 * 注册
	 */
	@Override
	@Transactional(rollbackFor={Exception.class})
	public ResultData<Void> register(UserInfoBo userInfo) {
		//校验用户注册参数
		RetCode retCode = checkUserParams(userInfo);
		if(!retCode.success()){
			return ResultData.getInstance(retCode);
		}
		UserEntity userEntity = new UserEntity(userInfo).init();
		//生成注册的临时key，30分钟内有效，用来激活账号
		String key = String.format(UserConstant.ACTIVE_KEY, RandomStringUtils.randomAlphanumeric(10));
		redisDao.set(key, userEntity.getName(),30,TimeUnit.MINUTES);
		//插入数据库
		userEntityMapper.insert(userEntity);
		//发送激活邮件
		String activateUrl = systemConfig.getActivateUrl(key);
		mailSendService.sendRegisterMail(userEntity.getMail(),userEntity.getName(),activateUrl);
		return ResultData.getInstance();
	}
	/**
	 * 激活账号
	 */
	@Override
	public String activate(String key) {
		if(StringUtils.isEmpty(key)){
			return systemConfig.getAbsentPage();
		}
		String name = redisDao.get(key);
		if(StringUtils.isEmpty(name)){
			return systemConfig.getAbsentPage();
		}
		UserEntity userEntity = userEntityMapper.queryUserByName(name);
		if(userEntity==null){
			return systemConfig.getAbsentPage();
		}
		//更新状态
		userEntity.setStatus(UserStatus.NORMAL);
		userEntityMapper.updateById(userEntity);
		//设置登录信息
		setLoginInfo(userEntity);
		return systemConfig.getHomePage();
	}
	/**
	 * 登录
	 */
	@Override
	public ResultData<Void> login(UserInfoBo userInfo) {
		if(StringUtils.isEmpty(userInfo.getName())||StringUtils.isEmpty(userInfo.getPwd())){
			return ResultData.getInstance(BaseRetCode.PARAM_MISSING);
		}
		UserEntity userEntity = userEntityMapper.queryUserByName(userInfo.getName());
		if (userEntity == null || !userInfo.getPwd().equals(userEntity.getPwd())) {
			return ResultData.getInstance(UserEnum.LOGIN_FAIL);
		}
		if(userEntity.getStatus() == UserStatus.INIT){
			return ResultData.getInstance(UserEnum.NOT_ACTIVATED);
		}
		if(userEntity.getStatus() == UserStatus.DISABLED){
			return ResultData.getInstance(UserEnum.USER_DISABLED);
		}
		//设置登录信息
		setLoginInfo(userEntity);
		return ResultData.getInstance();
	}

	/**
	 * 设置登录信息
	 * @param userEntity
	 */
	private void setLoginInfo(UserEntity userEntity) {
		String tokenKey = userEntity.generateToken();
		//登录信息30分钟有效
		redisDao.set(tokenKey, JSON.toJSONString(userEntity),30,TimeUnit.MINUTES);
		//设置cookies
		Map<String, String> cookies = Maps.newHashMap();
		cookies.put(UserConstant.TOKEN, tokenKey);
		ControllerUtil.setCookie(cookies, -1);
		
	}

	/**
	 * 校验注册参数
	 * @param userInfo
	 * @return
	 */
	private RetCode checkUserParams(UserInfoBo userInfo) {
		//参数为空
		if (StringUtils.isEmpty(userInfo.getName()) || StringUtils.isEmpty(userInfo.getPwd())
				|| StringUtils.isEmpty(userInfo.getMail())) {
			return BaseRetCode.PARAM_MISSING;
		}
		//用户名格式
		if(!Pattern.matches(UserConstant.USER_NAME_MATCH, userInfo.getName())){
			return UserEnum.USER_NOT_MATCH;
		}
		//邮箱格式
	    if(!Pattern.matches(UserConstant.MAIL_MATCH, userInfo.getMail())){
	    	return UserEnum.MAIL_NOT_MATCH;
	    }
	    //用户名是否已存在
	    UserEntity userEntity = userEntityMapper.queryUserByName(userInfo.getName());
	    if(userEntity!=null){
	    	return UserEnum.USER_EXIST;
	    }
		return BaseRetCode.SUCCESS;
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public ResultData<Void> resetPassword(String user,String pwd,String code) {
		//参数为空
		if (StringUtils.isEmpty(user) || StringUtils.isEmpty(pwd)||StringUtils.isEmpty(code)){
			return ResultData.getInstance(BaseRetCode.PARAM_MISSING);
		}
		//验证码
		String codes = redisDao.get(UserConstant.CODE+user);
		if(!code.equals(codes)){
			return ResultData.getInstance(UserEnum.CODE_FAIL);
		}
		//查询用户
		UserEntity userEntity = userEntityMapper.queryUserByName(user);
		if(userEntity==null){
			return ResultData.getInstance(BaseRetCode.FAIL);
		}
		//更新到数据库
		userEntity.setPwd(pwd);
		userEntityMapper.updateById(userEntity);
		//退出登录
		exit();
		return ResultData.getInstance();
	}

	@Override
	public ResultData<Void> changePassword(String oldPwd, String pwd) {
		//参数为空
		if (StringUtils.isEmpty(oldPwd) || StringUtils.isEmpty(pwd)){
			return ResultData.getInstance(BaseRetCode.PARAM_MISSING);
		}
		//原密码不匹配
		UserEntity currentUser = UserThreadLocal.getCurrentUser();
		if(!oldPwd.equals(currentUser.getPwd())){
			return ResultData.getInstance(UserEnum.CHANGE_PWD_FAIL);
		}
		//插入数据库
		currentUser.setPwd(pwd);
		userEntityMapper.updateById(currentUser);
		//退出登录
		exit();
		return ResultData.getInstance();
	}

	@Override
	public ResultData<PagingData<UserVo>> list(UserListCondition condition) {
		int count  = userEntityMapper.queryCountByCondition(condition);
		List<UserEntity> logEntitys = userEntityMapper.queryByCondition(condition);
		List<UserVo> list = logEntitys.stream().map(u->{
			return new UserVo(u);
		}).collect(Collectors.toList());
		PagingData<UserVo> pagingData = PagingData.getInstance(condition.getPageSize(),count,list);
		return ResultData.getInstance(pagingData);
	}
	
	@Override
	public ResultData<Void> modifyStatus(Long id, UserStatus status) {
		if(id==null||status==null){
			return ResultData.getInstance(BaseRetCode.PARAM_MISSING);
		}
		if(UserConstant.ADMIN_ID.equals(id)){
			return ResultData.getInstance(BaseRetCode.NOT_PERMISSION);
		}
		UserEntity userEntity = userEntityMapper.selectById(id);
		if(userEntity==null){
			return ResultData.getInstance(BaseRetCode.FAIL);
		}
		userEntity.setStatus(status);
		userEntityMapper.updateById(userEntity);
		if(status==UserStatus.DISABLED){
			redisDao.delete(userEntity.generateToken());
		}
		return ResultData.getInstance();
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public ResultData<Void> delete(Long id) {
		if(UserConstant.ADMIN_ID.equals(id)){
			return ResultData.getInstance(BaseRetCode.NOT_PERMISSION);
		}
		UserEntity userEntity = userEntityMapper.selectById(id);
		if(userEntity==null){
			return ResultData.getInstance(BaseRetCode.FAIL);
		}
		int deleteById = userEntityMapper.deleteById(id);
		if(deleteById==1){
			redisDao.delete(userEntity.generateToken());
			return ResultData.getInstance();
		}
		return ResultData.getInstance(BaseRetCode.FAIL);
	}

	@Override
	public ResultData<UserVo> refreshToken() {
		UserEntity currentUser = UserThreadLocal.getCurrentUser();
		currentUser.setToken(RandomStringUtils.randomAlphanumeric(20));
		userEntityMapper.updateById(currentUser);
		setLoginInfo(currentUser);
		return ResultData.getInstance(new UserVo(currentUser));
	}

	@Override
	public ResultData<Void> verificationCode(String user, String mail) {
		//参数为空
		if (StringUtils.isEmpty(user) || StringUtils.isEmpty(mail)){
			return ResultData.getInstance(BaseRetCode.PARAM_MISSING);
		}
		//资料不匹配
		UserEntity userEntity = userEntityMapper.queryUserByName(user);
		if(userEntity==null||!userEntity.getMail().equals(mail)){
			return ResultData.getInstance(UserEnum.RESET_PWD_FAIL);
		}
		//生成验证码
		String code = RandomStringUtils.randomNumeric(6);
		//保存验证码
		redisDao.set(UserConstant.CODE+user, code,10,TimeUnit.MINUTES);
		//发送验证码邮件
		mailSendService.sendVerificationCode(mail, user, code);
		return ResultData.getInstance();
	}






}
