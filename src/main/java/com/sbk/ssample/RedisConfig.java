package com.sbk.ssample;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration.JedisClientConfigurationBuilder;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

	static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);
	
	@Value("${spring.redis.host}") String redisHost;
	@Value("${spring.redis.port}") int redisPort;
	@Value("${spring.redis.connection.timeout}") int connectTimeout;
	@Value("${spring.redis.read.timeout}") int readTimeout;
	 
	
	void printRedisConfiguration() {
		logger.info(String.format("**** redis configuration *****"));
		logger.info(String.format("redis host: %s", redisHost));
		logger.info(String.format("redis port: %d", redisPort));
		logger.info(String.format("redis connect-timeout: %d", connectTimeout));
		logger.info(String.format("redis read-timeout: %d", readTimeout));
	}
	
	
	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration();
		standaloneConfig.setHostName(redisHost);
		standaloneConfig.setPort(redisPort);
		
		JedisClientConfigurationBuilder clientConfig = JedisClientConfiguration.builder();
		clientConfig.connectTimeout(Duration.ofSeconds(connectTimeout));
		clientConfig.readTimeout(Duration.ofSeconds(readTimeout));
		
		JedisConnectionFactory jedisConFactory = 
				new JedisConnectionFactory(standaloneConfig, clientConfig.build());
		
		printRedisConfiguration();
		return jedisConFactory;
	}
	
	
	
	
	
}
