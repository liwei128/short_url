package com.abner.c1n.dao.mapper;

import java.util.List;

import com.abner.c1n.beans.dto.user.UserListCondition;
import com.abner.c1n.beans.po.UserEntity;

/**
 * 用户
 * @author 01383518
 * @date:   2019年6月12日 上午10:42:27
 */
public interface UserEntityMapper {
	/**
	 * 删除
	 * @param id
	 * @return
	 */
    int deleteById(Long id);

    /**
     * 新增
     * @param record
     * @return
     */
    int insert(UserEntity record);

    /**
     * 查询
     * @param id
     * @return
     */
    UserEntity selectById(Long id);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateById(UserEntity record);

    /**
     * 根据账号查询用户
     * @param name
     * @return
     */
    UserEntity queryUserByName(String name);

    /**
     * 查询记录数
     * @param condition
     * @return
     */
	int queryCountByCondition(UserListCondition condition);

	/**
	 * 查询记录
	 * @param condition
	 * @return
	 */
	List<UserEntity> queryByCondition(UserListCondition condition);

	/**
	 * 查询
	 * @param token
	 * @return
	 */
	UserEntity queryByToken(String token);


	/**
	 * 删除未激活用户
	 * @param minute
	 * @return
	 */
	int deleteInitUserByTime(int minute);
}