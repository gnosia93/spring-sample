스프링 프로파일 설정에 대한 예제로 application.yml 파일에 정의된 내용이다. 

active 프로파일을 dev 와 local 로 정의하고 있다.

```
spring.profiles.active : dev, local
spring.application.name : demoservice
spring.api:
   endpoint: /api
```

Properties 바인딩 및 프로파일 테스트를 위한 예제 코드로, yml 파일에 dev 및 local 만 설정되어 있으므로,
@Profile("production") 로 설정된 ProductionBean 는 주입되지 않는다. 

```
@RestController
@Configuration
@SpringBootApplication
public class SpringplaceholderApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SpringplaceholderApplication.class, args);
	}

	@GetMapping("${spring.api.endpoint:/empty}")
	public String justGet() {
		return "justGet()";
	}
	
	@GetMapping("${spring.api.endpoint.empty:/empty}")
	public String emptyGet() {
		return "emptyGet()";
	}
	

	class DevBean {}
	class ProductionBean {}
	
	@Profile("dev")
	@Bean 
	public DevBean getDataSource() {
		return new DevBean();
	}
	
	@Profile("production")
	@Bean
	ProductionBean getBean() {
		return new ProductionBean();
	}
	
	@Bean
	public CommandLineRunner runner() {
		return new CommandLineRunner() {
			@Autowired ConfigurableEnvironment env;
			@Autowired(required=false) DevBean devBean;
			@Autowired(required=false) ProductionBean productionBean;
			@Value("${spring.api.endpoint:test}") String apiEndPoint;	

			@Override
			public void run(String... args) throws Exception {
				System.out.println("active profiles : " );
				Arrays.asList(env.getActiveProfiles()).stream().forEach(x -> System.out.format("  - %s\n",x));
				System.out.println("api.end.point " + apiEndPoint);
				
				System.out.println("dev bean wired ? " + !ObjectUtils.isEmpty(devBean));
				System.out.println("production wired ? " + !ObjectUtils.isEmpty(productionBean));
			}
		};
			
	};
}


** 실행결과 
active profiles : 
  - dev
  - local
api.end.point /api
dev bean wired ? true
production wired ? false


```
