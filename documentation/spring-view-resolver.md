아래는 spring boot 에서 jsp 뷰 리졸버를 추가하는 예이다.

jsp를 사용하기 위해서는 jstl 및 tomcat-embed-jasper 가 필요하고,

파일의 경로 및 확장자는 아래와 같이 static 하게 정해져 있는 듯 하다. 

바꾸면 제대로 동작하지 않음. 


## Controller ##

@Controller 어노테이션을 설정하고, 웹 브라우저를 이용하여 http://localhost:8080 으로 접속한다. 

아래와 같이 뷰 리졸버를 찾지 못한다고 에러 메시지가 출력된다. 

```
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

	final UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/add")
	public void getUser() {
		//AddUserRequest --> AddUserCommand --> User Domain Entity --> repository(I)
		
		AddUserCommand addUserCommand = new AddUserCommand();
		addUserCommand.setId("mongo-id-test-01");
		addUserCommand.setGender(Gender.FEMAIL);
		addUserCommand.setPassword("password");
		addUserCommand.setGender(Gender.MAIL);
		addUserCommand.setName("name");
		addUserCommand.setAddress(new Address("00000", "서울시", "강동구"));
		
		userService.add(addUserCommand);
		log.info("UserController is called..");
	}
	
	
	@GetMapping("/registration")
	public String showRegistrationForm(WebRequest request, Model model) {
		
		return "registration";
	}
}

Whitelabel Error Page
This application has no explicit mapping for /error, so you are seeing this as a fallback.

Sat Jan 05 12:10:22 KST 2019
There was an unexpected error (type=Internal Server Error, status=500).
Circular view path [registration]: would dispatch back to the current handler URL [/user/registration] again. Check your ViewResolver setup! (Hint: This may be the result of an unspecified view, due to default view name generation.)
javax.servlet.ServletException: Circular view path [registration]: would dispatch back to the current handler URL [/user/registration] again. Check your ViewResolver setup! (Hint: This may be the result of an unspecified view, due to default view name generation.)
	at org.springframework.web.servlet.view.InternalResourceView.prepareForRendering(InternalResourceView.java:209)
	at org.springframework.web.servlet.view.InternalResourceView.renderMergedOutputModel(InternalResourceView.java:147)
	at org.springframework.web.servlet.view.AbstractView.render(AbstractView.java:316)
	at org.springframework.web.servlet.DispatcherServlet.render(DispatcherServlet.java:1370)
	at org.springframework.web.servlet.DispatcherServlet.processDispatchResult(DispatcherServlet.java:1116)
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1055)
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:942)
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1005)
	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:897)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:634)
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:882)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:741)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)
```

## 뷰 설정 ##

application.properties 파일에 아래 설정을 추가하고, 

src/main/webapp/WEB-INF/jsp/registration.jsp 생성 후

재 실행하면 registration.jsp을 찾을 수 없다는 경고가 뜬다. 

```
#view setting
spring.mvc.view.prefix=/WEB-INF/jsp/
spring.mvc.view.suffix=.jsp

2019-01-05 12:41:39.656 WARN ---[nio-8080-exec-1] o.s.w.s.r.ResourceHttpRequestHandler Path with "WEB-INF" or "META-INF": [WEB-INF/jsp/registration.jsp]

```

## POM 의존관계 추가 ##

아래의 의존관계 등록 후 재 실행하면 제대로 동작한다. 

```
<dependency>
	<groupId>javax.servlet</groupId>
	<artifactId>jstl</artifactId>
</dependency>
<dependency>
	<groupId>org.apache.tomcat.embed</groupId>
	<artifactId>tomcat-embed-jasper</artifactId>
	<scope>provided</scope>
</dependency>
```
