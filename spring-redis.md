### MAVEN ###

jedis client 와 spring-date-redis 의존 관계를 추가한다. 

```
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-redis</artifactId>
</dependency>
<dependency> 
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>                               
</dependency>
```


### Application Properties ####
https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html

```
# https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html
#
#spring.jpa.hibernate.ddl-auto=create
spring.datasource.jdbc-url=jdbc:mysql://localhost:3306/sample?serverTimezone=UTC
spring.datasource.username=sample
spring.datasource.password=sample
spring.datasource.maximum-pool-size=10

spring.redis.host=127.0.0.1                         
spring.redis.port=6379
spring.redis.connection.timeout = 30
spring.redis.read.timeout = 3


#logging.level.com.sbk.ssample=INFO
logging.level.root=INFO

```



### @Configuration JAVA Class ###

아래는 standalone redis 를 설정하는 JAVA Config Class 이다. 
```
package com.sbk.ssample;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.PoolConfig;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration.JedisClientConfigurationBuilder;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import redis.clients.jedis.JedisPoolConfig;

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
		
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(10);				// 풀갯수
		poolConfig.setMinIdle(2);			    // idle 최소
		poolConfig.setMaxIdle(5);				// idle 최대
		
		JedisClientConfiguration clinetConfig = JedisClientConfiguration.builder()
				.clientName("sample")
				.connectTimeout(Duration.ofSeconds(connectTimeout))
				.readTimeout(Duration.ofSeconds(readTimeout))        // JedisClientConfigurationBuilder
				.usePooling()				// JedisPoolingClientConfigurationBuilder 로 스위칭.
				.poolConfig(poolConfig).and()           // 풀설정 및 JedisClientConfigurationBuilder 로 전환
				.build();
		
		JedisConnectionFactory jedisConFactory = 
				new JedisConnectionFactory(standaloneConfig, clinetConfig);
	
		printRedisConfiguration();
		return jedisConFactory;
	}
	
	// 아래 URL을 방문하면 시리얼라이저를 확인할 수 있다.  
	//https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/serializer/RedisSerializer.html
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());     // 스트링 시리얼 라이저로 설정
		//redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
		redisTemplate.setConnectionFactory(jedisConnectionFactory());		
		return redisTemplate;
	}
	
	
}

```


### Operation Examples ###
https://www.oodlestechnologies.com/blogs/Configure-Connection-Pooling-With-Redis-In-Spring-Boot

#### RedisTemplate ####
```
package io.startup.demo.service;

import javax.annotation.Resource;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class CounterService {
	
	@Resource(name="redisTemplate")
	ValueOperations<String, String> valueOps;
	
	public Long getVisitCount() {
		Long count = 0L;
		try {
			valueOps.increment("spring:redis:visitcount", 1);
			count = Long.valueOf(valueOps.get("spring:redis:visitcount"));
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return count;
	}
}

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringredisApplicationTests {

	@Autowired CounterService counterService;
	
	@Repeat(10)
	@Test
	public void incrementTest() {
		System.err.println(counterService.getVisitCount());
		
	}
}

```




#### CURD Repository ####
```
@Data
@AllArgsConstructor
@RedisHash("Student")
public class Student {
	
	public enum Gender {
		MAIL, FEMAIL
	}

	@Id
	String mid;
	String name;
	Gender gender;
	int grade;
	
}

@Repository
public interface StudentRepository extends CrudRepository<Student, String>{

}




@RunWith(SpringRunner.class)
@DataRedisTest
public class SpringredisApplicationTests {

	@Autowired StudentRepository studentRepository;
	
	@Test
	public void contextLoads() {
	}

	@Ignore
	@Test
	public void read() {
		
		studentRepository.findById("id-k2").ifPresent(System.out::println);
	}
	
	//@Ignore
	@Test
	public void redisTest() {
		
	//	studentRepository.deleteAll();
		
		Student student = new Student("id-k", "redis", Gender.MAIL, 1);
		studentRepository.save(student);

		student = new Student("id-k2", "redis", Gender.FEMAIL, 1);
		studentRepository.save(student);
		
		System.out.println("............");
		studentRepository.findAll().forEach(System.out::println);
		
		
		System.out.println("............");
		Optional<Student> optStudent = studentRepository.findById("id-k");
		optStudent.ifPresent(System.out::println);
		
	}
	
	
}


```







### Redis 명령어 ###
```
> ping
> keys *
> del [keyname]
> save
```


