엔터티에 @SqlResultSetMapping 이용하여 DTO 매핑을 정의한다. 

```
@SqlResultSetMapping(
	name="ProductOrderedMemberMapping",
	classes = @ConstructorResult(
		targetClass = ProductOrderedMemberResult.class,
		columns = {
			@ColumnResult(name="product_id", type=Long.class),
			@ColumnResult(name="product_name", type=String.class),
			@ColumnResult(name="price", type=Integer.class),
			@ColumnResult(name="cnt", type=Integer.class),
			@ColumnResult(name="order_id", type=Long.class),
			@ColumnResult(name="member_id", type=Long.class),
			@ColumnResult(name="member_name", type=String.class)
		}
	)	
)
@NoArgsConstructor
@Table(name="tbl_product")
@Entity
public class Product {

	@Id
	@Column(name="product_id")
	long id;
	
	@Column(name="product_name")
	String name;
	
	@Column(name="price")
	int price;
	
	@Column(name="cnt")
	int count;
	
}


```


조인결과를 받아올 DTO 를 아래와 같이 선언한다. (POJO 클래스이다)
```
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductOrderedMemberResult {
	
	private long productId;
	private String productName;
	private int price;
	private int cnt;
	private long orderId;
	private long memberId;
	private String memberName;
}
```

레포지토리 인터페이스 / Impl 를 아래와 같이 구현한다. 이때 주의할 것은 JpaRepository 인터페이스를 상속하면 안되며,

EntityManager 를 이용하여 Native Query를 작성해야 한다. 



```
public interface ProductRepository {

	public List<ProductOrderedMemberResult> getOrderedList(@Param("productId") long productId);
	
}

@Repository
public class ProductRepositoryImpl implements ProductRepository {

	private EntityManager entityManager;
	
	@Autowired
	public ProductRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	
	@Override
	public List<ProductOrderedMemberResult> getOrderedList(@Param("productId") long productId) {
		String query = 
				"select p.product_id, p.product_name, p.price, p.cnt, " +
			    "       o.order_id, m.member_id, m.member_name " +
			    "from tbl_product p " +
				"left join tbl_order o on p.product_id = o.product_id " +
				"left join tbl_member m on o.member_id = m.member_id " +
				"where p.product_id = :productId "; 
		
			/*
			 * Caused by: org.hibernate.MappingException: Unknown entity: io.startup.demo.OrderDto
			 * 에러를 유발시킨다. createNativeQuery의 resultClass 는 Entity 이어야 한다. 
			 */
			//EntityManager entityManger = entityManagerFacotry.createEntityManager();
			// Query nativeQuery = entityManger.createNativeQuery(query, OrderDto.class);
			// List<OrderDto> orderList = nativeQuery.getResultList();
			
		   List<ProductOrderedMemberResult> result = 
			entityManager.createNativeQuery(query, "ProductOrderedMemberMapping")
						.setParameter("productId", productId)
						.getResultList();
			
		
		return result;
	}

}
```


## 레퍼런스 ##

https://theuphill.tistory.com/11

https://vladmihalcea.com/the-best-way-to-map-a-projection-query-to-a-dto-with-jpa-and-hibernate/



## 추가 테스트 ##


1. 힌트 (주석)
   
   https://docs.spring.io/spring-data/jpa/docs/1.7.1.RELEASE/reference/html/#jpa.query-hints

2. groovy native ..  (groovy 에서 따옴표 3개의 multiline 문자열을 가리킨다. )

3. procedure.. 오라클 프로시저 테스트.

