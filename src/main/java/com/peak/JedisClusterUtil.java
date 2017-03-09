package com.peak;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class JedisClusterUtil {
	// private static JedisClusterExt cluster;
	private static PipelineCluster redisCluster;
	static Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();

	private static void createJedisCluster() {
		jedisClusterNodes.add(new HostAndPort("10.16.3.4", 6390));
		jedisClusterNodes.add(new HostAndPort("10.16.3.5", 6390));
		jedisClusterNodes.add(new HostAndPort("10.16.3.10", 6390));

		JedisCluster jc = new JedisCluster(jedisClusterNodes);

		redisCluster = PipelineCluster.pipelined(jc);
		redisCluster.refreshCluster();
		// cluster = new JedisClusterExt(jedisClusterNodes);
	}

	public static int getRedisHostsNum() {
		return jedisClusterNodes.size();
	}

	private static synchronized void clusterInit() {
		if (redisCluster == null)
			createJedisCluster();
	}

	public static PipelineCluster getJedisCluster() {
		if (redisCluster == null) {
			clusterInit();
		}
		return redisCluster;
	}

	public static void closeJedisCluster() {
		// if (cluster != null) {
		// cluster.close();
		// }
	}
}