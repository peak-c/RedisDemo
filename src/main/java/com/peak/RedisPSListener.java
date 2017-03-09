package com.peak;

import redis.clients.jedis.JedisPubSub;

/**
 * 监听订阅事件
 * 
 * @author peak
 * @Date 2017年1月13日 下午6:18:42
 */
public class RedisPSListener extends JedisPubSub {
	// 取得订阅的消息后的处理
	public void onMessage(String channel, String message) {
		System.out.println("onMessage:" + channel + "=" + message);
	}

	// 初始化订阅时候的处理
	public void onSubscribe(String channel, int subscribedChannels) {
		System.out.println("onSubscribe:" + channel + "=" + subscribedChannels);
	}

	// 取消订阅时候的处理
	public void onUnsubscribe(String channel, int subscribedChannels) {
		System.out.println("onUnsubscribe:" + channel + "=" + subscribedChannels);
	}

	// 初始化按表达式的方式订阅时候的处理
	public void onPSubscribe(String pattern, int subscribedChannels) {
		System.out.println("onPSubscribe:" + pattern + "=" + subscribedChannels);
	}

	// 取消按表达式的方式订阅时候的处理
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
		System.out.println("onPUnsubscribe:" + pattern + "=" + subscribedChannels);
	}

	// 取得按表达式的方式订阅的消息后的处理
	public void onPMessage(String pattern, String channel, String message) {
		System.out.println("onPMessage:" + pattern + "=" + channel + "=" + message);
	}
}
