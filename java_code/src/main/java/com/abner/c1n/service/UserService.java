package com.abner.c1n.service;

import com.abner.c1n.beans.bo.UserInfoBo;
import com.abner.c1n.beans.dto.user.UserListCondition;
import com.abner.c1n.beans.po.UserEntity.UserStatus;
import com.abner.c1n.beans.vo.common.PagingData;
import com.abner.c1n.beans.vo.common.ResultData;
import com.abner.c1n.beans.vo.user.UserVo;

/**
 * 用户相关服务
 * @author 01383518
 * @date:   2019年6月12日 上午9:26:31
 */
public interface UserService {

	/**
	 * 退出登录
	 * @return
	 */
	ResultData<Void> exit();

	/**
	 * 注册
	 * @param userInfo
	 * @return
	 */
	ResultData<Void> register(UserInfoBo userInfo);

	/**
	 * 激活账号
	 * @param key
	 * @return
	 */
	String activate(String key);

	/**
	 * 登录
	 * @param userInfo
	 * @return
	 */
	ResultData<Void> login(UserInfoBo userInfo);

	/**
	 * 重置密码
	 * @param user
	 * @param pwd
	 * @param code
	 * @return
	 */
	ResultData<Void> resetPassword(String user,String pwd,String code);

	/**
	 * 修改密码
	 * @param oldPwd
	 * @param pwd
	 * @return
	 */
	ResultData<Void> changePassword(String oldPwd, String pwd);

	/**
	 * 查询用户列表
	 * @param condition
	 * @return
	 */
	ResultData<PagingData<UserVo>> list(UserListCondition condition);

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	ResultData<Void> delete(Long id);

	/**
	 * 刷新token
	 * @return
	 */
	ResultData<UserVo> refreshToken();

	/**
	 * 发送验证码
	 * @param user
	 * @param mail
	 * @return
	 */
	ResultData<Void> verificationCode(String user, String mail);

	/**
	 * 修改状态
	 * @param id
	 * @param status
	 * @return
	 */
	ResultData<Void> modifyStatus(Long id, UserStatus status);

}
