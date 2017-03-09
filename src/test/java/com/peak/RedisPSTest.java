package com.peak;

import java.util.Scanner;

import redis.clients.jedis.Jedis;

public class RedisPSTest {

	public static void main(String[] args) {
		RedisPSTest test = new RedisPSTest();
		final Jedis jedis = RedisUtil.getJedis();
		new Thread(new Runnable() {
			@Override
			public void run() {
				RedisPSListener redisPSListener = new RedisPSListener();
				// 可以订阅多个频道
				// 订阅得到信息在lister的onMessage(...)方法中进行处理
				// jedis.subscribe(listener, "news.share", "news.log");
				// jedis.subscribe(redisPSListener, new
				// String[]{"news.share","news.log"});
				// jedis.psubscribe(redisPSListener, new String[] { "news.share"
				// });// 使用模式匹配的方式设置频道

				// 简单聊天
				jedis.psubscribe(redisPSListener, new String[] { "talk.room" });
			}
		}).start();

		test.publish();
		Scanner scanner = new Scanner(System.in);
		String input;
		while (!(input = scanner.next()).equals("bye")) {
			test.say("talk.room", input);
		}
	}

	/**
	 * 发布
	 */
	public void publish() {
		Jedis jedis = RedisUtil.getJedis();
		jedis.publish("news.share", "ok");
		jedis.publish("news.share", "hello word");
		jedis.publish("news.log", "this is a log");
	}

	public void say(String room, String words) {
		Jedis jedis = RedisUtil.getJedis();
		jedis.publish(room, words);
	}

}
