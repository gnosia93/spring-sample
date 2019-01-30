@EnableJpaAuditing 어노테이션을 선언한다. 이 경우 create date, update date 대한 정보를 받아올 수 있다.

데이터 입력/수정자 정보를 받아오기 위해서는 @EnableJpaAuditing(auditorAwareRef="auditorProvider") 로 어노테이션을 선언한 후,

auditorProvider 를 구현해야 한다. 

```
@EnableJpaAuditing(auditorAwareRef="auditorProvider")
@SpringBootApplication
public class SpringJpgNativeQueryApplication {
	
	@Autowired
	private CartRepository cartRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringJpgNativeQueryApplication.class, args);
	}

	@Bean
	AuditorAware<String> auditorProvider() {
		return new AuditorAwareImpl();
	}
	
	
	private void auditLog(Cart cart) {
		System.out.println(cart.getCreatedDate() + "  " + cart.getModifiedDate() + "  " +
				cart.getCreatedBy() + " " + cart.getModifiedBy() + " " +
				cart.getAnotherTime()); 
	}
	
	@Bean
	public CommandLineRunner runner() {
		
		
		return (a) -> {
			System.out.println(productRepository);
			
			Cart cart = new Cart(1, "test");

      Cart savedCart = cartRepository.save(cart);
			auditLog(savedCart);
			
      savedCart.setBuyerId("test2");
			cartRepository.saveAndFlush(savedCart);
			auditLog(savedCart);
			
		};
	}
}
```
