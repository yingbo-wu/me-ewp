package cn.rongcapital.mc2.me.ewp.domain;

import java.util.Collections;

import org.redisson.RedissonNode;
import org.redisson.api.RedissonClient;
import org.redisson.config.RedissonNodeConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan
@PropertySource({ "classpath:domain-config.properties" })
public class Config {

	@Value("${me.redisson.address}")
	private String address;

	@Bean
	public RedissonNode redissonNode() {
		RedissonNodeConfig config = new RedissonNodeConfig();
		config.useSingleServer()
			  .setAddress(address)
			  .setDatabase(0)
			  .setConnectTimeout(15000)
			  .setTimeout(60000);
		config.setExecutorServiceWorkers(Collections.singletonMap("EWP-EXECUTOR-WORKER", 2));
		RedissonNode node = RedissonNode.create(config);
		node.start();
		return node;
	}

	@Bean
	public RedissonClient redissonClient() {
		return redissonNode().getRedisson();
	}

}
