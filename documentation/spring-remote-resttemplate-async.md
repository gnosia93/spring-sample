@EnableAsync 를 이용하여 요청에 대해 비동기로 처리하도록 설정한다. 

```
@EnableAsync
@SpringBootApplication
public class SpringtestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringtestApplication.class, args);
	}
}
```

Controller 에서 @Async 어노테이션을 사용하고 있지 않기 때문에 서블릿 NIO 쓰레드에 의해 함수가 호출된다. 

물론 @Async 어노테이션을 사용하면 다른 쓰레드에서 처리된다. 

```
@Slf4j
@RestController
public class AsyncController {

	private LookupService lookupService;
	
	@Autowired
	public AsyncController(LookupService lookupService) {
		this.lookupService = lookupService;
	}
	
	@RequestMapping("/user/{name}")
	public CompletableFuture<TimedResponse<User>> findUser(@PathVariable(value="name") String name) throws InterruptedException {
		long start = System.currentTimeMillis();
		
		log.info(" ---> " + Thread.currentThread().getName());
		TimedResponse<User> response = new TimedResponse<>();
		return lookupService.findUser(name)
				.thenApply(user -> {
					response.setData(user);
					response.setTimeMs(System.currentTimeMillis() - start);
					response.setCompletingThread(Thread.currentThread().getName());
					return response;
				});
	}
}

```

@Async 어노테이션을 이용하여 시간이 많이 걸리는 외부 호출를 비동기로 처리하고 있다. 

```
@Slf4j
@Service
public class LookupService {

	private final RestTemplate restTemplate;
	
	public LookupService(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}
	
	@Async
	public CompletableFuture<User> findUser(String user) throws InterruptedException {
		log.info("Lookup up " + user);
		String url = String.format("https://api.github.com/users/%s", user);
		User results = restTemplate.getForObject(url, User.class);
		
		Thread.sleep(2000L);          // 처리 지연 시뮬레이션을 위한 코드. 
		log.info(" ---> " + Thread.currentThread().getName() + "  " + results);
  
    // 처리 결과를 Future 로 반환한다. 
		return CompletableFuture.completedFuture(results);          
	}	
}
```

처리 시간 및 쓰레드 정보를 출력하기 위한 Warpper Class 이다. thenApply 에서 User 객체를 랩핑한다.

```
public class TimedResponse<T> {
	private T data;
	private String startingThread;
	private String completingThread;
	private long timeMs;
	private boolean error = false;
	
	public TimedResponse() {
		this.startingThread = Thread.currentThread().getName();
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getStartingThread() {
		return startingThread;
	}

	public void setStartingThread(String startingThread) {
		this.startingThread = startingThread;
	}

	public String getCompletingThread() {
		return completingThread;
	}

	public void setCompletingThread(String completingThread) {
		this.completingThread = completingThread;
	}

	public long getTimeMs() {
		return timeMs;
	}

	public void setTimeMs(long timeMs) {
		this.timeMs = timeMs;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}
}
```

아래는 도멘인 클래스인 User 이다. 

```
@JsonIgnoreProperties(ignoreUnknown=true)
public class User {

	    private String name;
	    private String url;
	    private String company;
	    private String location;
	    @JsonProperty("avatar_url")
	    private String avatarUrl;

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getUrl() {
	        return url;
	    }

	    public void setUrl(String url) {
	        this.url = url;
	    }

	    public String getCompany() {
	        return company;
	    }

	    public void setCompany(String company) {
	        this.company = company;
	    }

	    public String getLocation() {
	        return location;
	    }

	    public void setLocation(String location) {
	        this.location = location;
	    }

	    public String getAvatarUrl() {
	        return avatarUrl;
	    }

	    public void setAvatarUrl(String avatarUrl) {
	        this.avatarUrl = avatarUrl;
	    }

		@Override
		public String toString() {
			return "User [name=" + name + ", url=" + url + ", company=" + company + ", location=" + location
					+ ", avatarUrl=" + avatarUrl + "]";
		}
}
```

위의 예제를 실행하는 경우 아래와 같은 로그가 찍히는데, Lookup 서비스가 Executor 에 의해 실행 되고 있음을 알수 있다. 

그러나 별다른 Executor 를 설정하지 않은 상태이므로, SimpleAsyncTaskExecutor 에 의해 실행되고 있으며, 

성능을 위해 명시적으로 Executor POOL 을 만들어 줘야 한다. 

```
[nio-8080-exec-1] io.startup.springtest.AsyncController    : ---> http-nio-8080-exec-1
[nio-8080-exec-1] .s.a.AnnotationAsyncExecutionInterceptor : No TaskExecutor bean found for async processing
[cTaskExecutor-1] io.startup.springtest.LookupService      : Lookup up gnosia93
[cTaskExecutor-1] io.startup.springtest.LookupService      : ---> SimpleAsyncTaskExecutor-1
```

아래는 웹브라우저 상에 찍히는 결과 값이다. 
```
{
    "data": {
	        "name":null,
                "url":"https://api.github.com/users/gnosia93",
	        "company":null,
	        "location":null,
	        "avatar_url":"https://avatars0.githubusercontent.com/u/45726390?v=4"
    },
    "startingThread":"http-nio-8080-exec-1",
    "completingThread":"SimpleAsyncTaskExecutor-1",
    "timeMs":3260,
    "error":false
}
```




## 레퍼런스 ##

https://github.com/mchernyavskaya/gs-rest-service/tree/master/complete

https://mchernyavska.wordpress.com/2017/10/02/call-on-me-or-asynchronous-rest/

https://spring.io/guides/gs/async-method/
