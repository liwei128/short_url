package com.abner.c1n.dao.impl;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.abner.c1n.dao.RedisDao;
/**
 * 仅仅支持String类型	
 * @author liwei
 * @date: 2018年8月21日 下午4:16:06
 *
 */
@Repository
public class RedisDaoImpl implements RedisDao{

	@Resource
	private StringRedisTemplate stringRedisTemplate;
	
	@Override
	public void set(String key, String value) {
		stringRedisTemplate.opsForValue().set(key, value);
	}

	@Override
	public void set(String key, String value, long timeout, TimeUnit unit) {
		stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
	}

	@Override
	public boolean setIfAbsent(String key, String value) {
		return stringRedisTemplate.opsForValue().setIfAbsent(key, value);
	}

	@Override
	public boolean setIfAbsent(String key, String value, long timeout, TimeUnit unit) {
		try {
			return stringRedisTemplate.opsForValue().setIfAbsent(key, value);
		} finally {
			stringRedisTemplate.expire(key, timeout, unit);
		}
	}

	@Override
	public String get(String key) {
		return stringRedisTemplate.opsForValue().get(key);
	}

	@Override
	public boolean expire(String key, long timeout, TimeUnit unit) {
		return stringRedisTemplate.expire(key, timeout, unit);
	}

	@Override
	public long getExpire(String key) {
		return stringRedisTemplate.getExpire(key);
	}

	@Override
	public Set<String> keys(String pattern) {
		return stringRedisTemplate.keys(pattern);
	}

	@Override
	public boolean delete(String key) {
		return stringRedisTemplate.delete(key);
	}

	@Override
	public long delete(Collection<String> keys) {
		return stringRedisTemplate.delete(keys);
	}
    
}
