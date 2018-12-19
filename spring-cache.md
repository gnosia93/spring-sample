@Caccheable 어노테이션을 사용하여 Redis 서버를 remote Cache 로 사용하는 예제 코드이다.

MAVEN
```
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-cache</artifactId>
</dependency>

```
위와 같이 Maven 에 spring cache starter를 선언한다.

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


Rerfences
```
https://spring.io/guides/gs/caching/
https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#cache
https://spring.io/guides/gs/caching/
```
