@Caccheable 어노테이션을 사용하여 Redis 서버를 remote Cache 로 사용하는 예제 코드이다.

아래와 같이 Maven 에 spring cache starter를 선언한다.
#### MAVEN ####
```
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-cache</artifactId>
</dependency>

```

#### application properties ####
아래는 Redis 관련 프로퍼티 설정값이다.

자바 코드에서 명시적으로 아래 설정값을 설정하지 않는 경우, properties 의 값을 따른다. 
```
#cache
spring.redis.host=127.0.0.1
spring.redis.port=6379
spring.redis.connection.timeout = 30
spring.redis.read.timeout = 3

spring.redis.jedis.pool.max-active=8
spring.redis.jedis.pool.max-idle=8 
spring.redis.jedis.pool.min-idle=0 
```

#### Source Code ####

```
@RestController
public class CacheableController implements Controller {
	Logger logger = LoggerFactory.getLogger(CacheableController.class);
	
	@Autowired CacheableService cacheableService;
	
	@RequestMapping(path="${cacheable.url}/{id}", method=RequestMethod.GET)
	@Procedure(value="appplication/json")
	public ResponseEntity<CommandResult> getCacheResult(@PathVariable int id) {
		
		CommandResult result = CommandResult.success(cacheableService.getBook(id));
		return success(result);
	}
}

/*
 * inner 클래스이므로 CacheableService 클래스도 직력화를 구현해야 한다. 
 */
@Service
public class CacheableService implements Serializable {

   @Data
	@AllArgsConstructor
	public class Book implements Serializable {
		int id;
	}
	
	void slowServiceCode() {
		try {
			Thread.sleep(3000);
		}
		catch(InterruptedException e)
		{}
	}
	
	@Cacheable("books")
	public Book getBook(int id) {
		slowServiceCode();
		return new Book(id);
	}	
}

```

#### Redis 값 확인하기 ####
```
#> redis-cli
redis> client list
redis> keys *
```

#### Redis Pool 테스트 ####


#### Rerfences ####
```
https://spring.io/guides/gs/caching/
https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#cache
https://spring.io/guides/gs/caching/
```
