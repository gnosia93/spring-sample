## POM ##

메이븐 POM 파일에 spring-cloud-starter-openfeign 에 대한 의존관계를 설정한다. 

```
<dependencies>
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-openfeign</artifactId>
		<version>2.1.0.RELEASE</version>
	</dependency>
</dependencies> 
```

## application.yml ##
```
server:
  port: 8080
  servlet:
    context-path: /api

# H2 콘솔 설정.
spring:
  h2:
    enabled: true
    path: /h2
  datasource:
    url: jdbc:h2:file:~/demo
    username: sa
    password:
    driver-class-name: org.h2.Driver

# pretty json 설정
  jackson:
    serialization:
      indent_output: true

# feign
feign:
  client:
    config:
      default:      ## global settings
        connectTimeout: 5000
        readTimeout: 3000
        loggerLevel: full
  #      errorDecorder: com.example.SimpleErrorDecoder
  #      retryer: com.example.SimpleRetryer
  #      requestInterceptors:
  #        - io.startup.demo.FooRequestInterceptor
  #        - io.startup.demo.BarRequestInterceptor
        decode404: false
  #      encoder: io.startup.demo.SimpleEncoder
  #      decoder: io.startup.demo.SimpleDecoder
  #      contract: io.startup.demo.SimpleContract
  http-bin-api.url: https://httpbin.org
  post-api.url: https://jsonplaceholder.typicode.com

  # for use hystrix fallback
  hystrix:
      enabled: true

hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds: 10000   
```

fallback 동작을 위해 hystrix를 enable 하고 있다. 이때 주의 할 점은 feign 에 connection 및 read timeout 값이 있더라도,

hystrix를 enable 하면 timeout 설정은 hystirx를 따라가게 된다. 

default hystrix의 timeout 값은 1초 이므로, 오동작의 우려가 있으므로 위와 같이 10 초로 설정해야 한다. 


## 샘플 코드 ##
```

# 도메인 오브젝트
@Data
public class Post {
    private Long id;
    private Long userId;
    private String title;
    private String body;
}


# 컨트롤러
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostClient postClient;

    @Autowired
    public PostController(PostClient postClient) {
        this.postClient = postClient;
    }

    @GetMapping("/{id}")
    public Post get(@PathVariable Long id) {
        return postClient.get(id);
    }
}


# FeignClient 샘플

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="post-api", url="${feign.post-api.url}")
public interface PostClient {
    @GetMapping(value = "/posts/{id}",  consumes = "application/json")
    Post get(@PathVariable("id") Long id);
}

```





## 레퍼런스 ##

https://www.baeldung.com/intro-to-feign

https://www.baeldung.com/spring-hateoas-tutorial

https://supawer0728.github.io/2018/03/11/Spring-Cloud-Feign/
