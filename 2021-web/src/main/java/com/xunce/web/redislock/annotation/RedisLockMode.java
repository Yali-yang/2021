package com.xunce.web.redislock.annotation;

/**
 * @author zhaozonglu001
 * @date 2023/6/17
 */
public enum RedisLockMode {
	FAST_FAIL(),
	TRY_WAIT(),
	WAIT();
}
