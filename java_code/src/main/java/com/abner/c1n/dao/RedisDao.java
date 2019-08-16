package com.abner.c1n.dao;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis相关操作
 * @author liwei
 * @date: 2018年8月21日 下午4:17:02
 *
 */
public interface RedisDao {
	/**
	 * 保存
	 * @param key
	 * @param value
	 */
	public void set(String key, String value);
	/**
	 * 保存并设置失效时间
	 * @param key
	 * @param value
	 * @param timeout
	 * @param unit
	 */
	public void set(String key, String value, long timeout, TimeUnit unit);
	
	/**
	 * 如果不存在则保存
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean setIfAbsent(String key, String value);
	/**
	 * 如果不存在则保存，且设置失效时间
	 * @param key
	 * @param value
	 * @param timeout
	 * @param unit
	 * @return
	 */
	public boolean setIfAbsent(String key, String value, long timeout, TimeUnit unit);
	
	/**
	 * 查询
	 * @param key
	 * @return
	 */
	public String get(String key);
	
	/**
	 * 设置失效时间
	 * @param key
	 * @param timeout
	 * @param unit
	 * @return
	 */
	public boolean expire(String key, long timeout, TimeUnit unit);
	
	/**
	 * 查询失效时间
	 * @param key
	 * @return
	 */
	public long getExpire(String key);
	/**
	 * 查询keys
	 * @param pattern
	 * @return
	 */
	public Set<String> keys(String pattern);
	/**
	 * 删除
	 * @param key
	 * @return
	 */
	public boolean delete(String key);
	/**
	 * 批量删除
	 * @param keys
	 * @return
	 */
	public long delete(Collection<String> keys);

}
