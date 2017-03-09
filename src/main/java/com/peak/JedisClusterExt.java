package com.peak;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisClusterCommand;
import redis.clients.jedis.JedisClusterConnectionHandler;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSlotBasedConnectionHandler;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

/**
 * 鎵╁睍JedisCluster 娣诲姞鏈疄鐜癑edis鐨勬帴鍙�
 * 
 * @author xiaojia
 * 
 */
public class JedisClusterExt extends JedisCluster {
	public static final short HASHSLOTS = 16384;
	private static final int DEFAULT_TIMEOUT = 20;
	private static final int DEFAULT_MAX_REDIRECTIONS = 5;

	private int timeout;
	private int maxRedirections;

	private JedisClusterConnectionHandler connectionHandler;

	public JedisClusterExt(Set<HostAndPort> nodes) {
		this(nodes, DEFAULT_TIMEOUT, DEFAULT_MAX_REDIRECTIONS);
	}

	public JedisClusterExt(Set<HostAndPort> nodes, int timeout, int maxRedirections) {
		this(nodes, timeout, maxRedirections, new GenericObjectPoolConfig());
	}

	public JedisClusterExt(Set<HostAndPort> jedisClusterNode, int timeout, int maxRedirections,
			final GenericObjectPoolConfig poolConfig) {
		super(jedisClusterNode);
		this.connectionHandler = new JedisSlotBasedConnectionHandler(jedisClusterNode, poolConfig, maxRedirections);
		this.timeout = timeout;
		this.maxRedirections = maxRedirections;
	}

	public ScanResult<String> scan(final String cursor, final ScanParams params) {
		return new JedisClusterCommand<ScanResult<String>>(connectionHandler, timeout) {
			@Override
			public ScanResult<String> execute(Jedis connection) {
				return connection.scan(cursor, params);
			}
		}.run(null);
	}

	public ScanResult<Map.Entry<String, String>> hscan(final String key, final String cursor, final ScanParams params) {
		return new JedisClusterCommand<ScanResult<Map.Entry<String, String>>>(connectionHandler, timeout) {
			@Override
			public ScanResult<Map.Entry<String, String>> execute(Jedis connection) {
				return connection.hscan(key, cursor, params);
			}
		}.run(key);
	}

	/*
	 * public Set<String> keys(final String key) { return new
	 * JedisClusterCommand<Set<String>>(connectionHandler, timeout,
	 * maxRedirections) {
	 * 
	 * @Override public Set<String> execute(Jedis connection) { return
	 * connection.keys(key); } }.run(key);
	 * 
	 * }
	 */
	public TreeSet<String> keys(String pattern) {
		TreeSet<String> keys = new TreeSet<String>();
		Map<String, JedisPool> clusterNodes = this.getClusterNodes();
		for (String k : clusterNodes.keySet()) {
			JedisPool jp = clusterNodes.get(k);
			Jedis connection = jp.getResource();
			try {
				keys.addAll(connection.keys(pattern));
			} catch (Exception e) {
			} finally {
				connection.close();// 用完一定要close这个链接！！！
			}
		}
		return keys;
	}
}
