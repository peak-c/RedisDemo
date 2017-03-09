package com.peak;

import org.junit.Test;

public class JedisClusterTest {


	@Test
	public void fun1(){
		PipelineCluster redis = JedisClusterUtil.getJedisCluster();
		redis.incrByFloat("key-test", 1L);

		//redis.expire("key-test", 1000*3600*24*365);

		redis.sync();
	}
}
