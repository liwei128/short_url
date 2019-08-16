package com.abner.c1n.controller.manage;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abner.c1n.annotation.Admin;
import com.abner.c1n.annotation.Slf4j;
import com.abner.c1n.beans.bo.UserInfoBo;
import com.abner.c1n.beans.constant.UserThreadLocal;
import com.abner.c1n.beans.dto.user.UserListCondition;
import com.abner.c1n.beans.enums.BaseRetCode;
import com.abner.c1n.beans.po.UserEntity.UserStatus;
import com.abner.c1n.beans.vo.common.PagingData;
import com.abner.c1n.beans.vo.common.ResultData;
import com.abner.c1n.beans.vo.user.UserVo;
import com.abner.c1n.service.UserService;

/**
 * 用户信息管理
 * @author 01383518
 * @date:   2019年6月12日 上午9:23:42
 */
@Controller
@RequestMapping("/manage/user")
public class UserController {
	
	@Resource
	private UserService userService;
	
	/**
	 * 查询用户列表,只有管理员能访问
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	@Admin
	public ResultData<PagingData<UserVo>> list(UserListCondition condition){
		return userService.list(condition);
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
	public ResultData<Void> modifyStatus(Long id,UserStatus status){
		return userService.modifyStatus(id,status);
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
		return userService.delete(id);
	}
	
	/**
	 * 当前用户
	 * @return
	 */
	@RequestMapping("/currentUser")
	@ResponseBody
	public ResultData<UserVo> currentUser(){
		if(UserThreadLocal.isLogin()){
			return ResultData.getInstance(new UserVo(UserThreadLocal.getCurrentUser()));
		}
		return ResultData.getInstance(BaseRetCode.FAIL);
	}

	/**
	 * 注册
	 * @param userInfo
	 * @return
	 */
	@RequestMapping("/register")
	@ResponseBody
	@Slf4j("register")
	public ResultData<Void> register(UserInfoBo userInfo){
		return userService.register(userInfo);
	}
	/**
	 * 激活账号，自动重定向
	 * @param pwd
	 * @return
	 */
	@RequestMapping("/activate")
	@Slf4j("activate")
	public String activate(String key){
		String url = userService.activate(key);
		return "redirect:" + url;
	}
	
	/***
	 * 登录，之后跳转主页
	 * @param userInfo
	 * @return
	 */
	@RequestMapping("/login")
	@ResponseBody
	@Slf4j("login")
	public ResultData<Void> login(UserInfoBo userInfo){
		return userService.login(userInfo);
	}
	
	/**
	 * 退出登录，之后退出到登录页面
	 * @return
	 */
	@RequestMapping("/exit")
	@ResponseBody
	@Slf4j("exit")
	public ResultData<Void> exit(){
		return userService.exit();
	}
	
	/**
	 * 修改密码，之后退出到登录页面
	 * @param id
	 * @param oldPwd
	 * @param pwd
	 * @return
	 */
	@RequestMapping("/changePassword")
	@ResponseBody
	@Slf4j("changePassword")
	public ResultData<Void> changePassword(String oldPwd, String pwd){
		return userService.changePassword(oldPwd,pwd);
	}
	
	
	/**
	 * 发送验证码
	 * @param pwd
	 * @return
	 */
	@RequestMapping("/verificationCode")
	@ResponseBody
	@Slf4j("verificationCode")
	public ResultData<Void> verificationCode(String user,String mail){
		return userService.verificationCode(user,mail);
	}
	
	/**
	 * 重置密码
	 * @param user
	 * @param pwd
	 * @param code
	 * @return
	 */
	@RequestMapping("/resetPassword")
	@ResponseBody
	@Slf4j("resetPassword")
	public ResultData<Void> resetPassword(String user,String pwd,String code){
		return userService.resetPassword(user,pwd,code);
	}
	
	
	
	/**
	 * 刷新token
	 * @return
	 */
	@RequestMapping("/refreshToken")
	@ResponseBody
	@Slf4j("refreshToken")
	public ResultData<UserVo> refreshToken(){
		return userService.refreshToken();
	}
	

}
