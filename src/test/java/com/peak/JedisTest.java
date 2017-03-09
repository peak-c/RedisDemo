package com.peak;


import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class JedisTest {
	
	@Test
	public void fun1(){
		Jedis redis = new Jedis("10.16.3.4",7000);
		Pipeline pl = redis.pipelined();
		
		
		pl.incrByFloat("key", 1L);
		
		pl.expire("key", 1000*3600*24*365);
		
		pl.sync();
	}
}
