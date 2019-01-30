@CreatedBy, @CreatedDate, @LastModifiedBy, @LastModifiedDate 어노테이션을 엔터티 클래스에 추가한다. 

@EntityListeners(AuditingEntityListener.class) 스프링에서 기본적으로 제공하는 리스너인 어노테이션을 추가한다.

```
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Cart {
	
	@Id
	@Column(name="card_id")
	long id;
	
	@Column(name="buyer_id", length=500)
	String buyerId;
	
	@CreatedDate
	private LocalDateTime createdDate;
	
	@Column(name="mod_ymdt")
	@LastModifiedDate
	private Date modifiedDate;
	
	@CreatedBy
	private String createdBy;
	
	@LastModifiedBy
	private String modifiedBy;
	
	@CreatedDate
	private Instant anotherTime;
	
	public Cart(long id, String buyerId) {
		this.id = id;
		this.buyerId = buyerId;
	}
}
```

사용자 정보를 받아오기 위햇 AuditorAware<T> 인터페이스를 구현한다. 

사용자 정보가 불필한 경우 생략가능하다. 

```
public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional.of("auditor");
	}

}

```

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
