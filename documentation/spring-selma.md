## Selma 사용법 ##
STS 에서 selma 를 사용하는 경우 코딩 후 mvn clean; mvn install 을 실행시켜야 selma class 가 생성된다.
실행시 자동으로 체크하여 코드를 생성하는 방법은 모르겠다.
아래는 selma를 모듈은 위한 POM 의존관계이다. 

'''
<!-- scope provided because the processor is only needed at compile time-->
	<dependency>
	    <groupId>fr.xebia.extras</groupId>
	    <artifactId>selma-processor</artifactId>
	    <version>1.0</version>
	    <scope>provided</scope>
	</dependency>

	<!-- This is the only real dependency you will have in your binaries -->
	<dependency>
	    <groupId>fr.xebia.extras</groupId>
	    <artifactId>selma</artifactId>
	    <version>1.0</version>
	</dependency>
'''


아래는 selma를 이용하는 샘플코드로 도메인 클래스인 OrderItem 과 JPA 엔터티간의 매핑이 이뤄지고 있다. 


```
public class Order {
	long orderId;
	
	@NonNull
	Buyer buyer;
	@NonNull
	List<OrderItem> itemList;
	@NonNull
	ShippingInfo shippingInfo;

	OrderStatus orderStatus;
	int totalPrice;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
	long itemId;
	String itemName;
	int itemCount;
	int itemPrice;
}




@Data
@Entity
@Table(name="tb_order_item")
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	long orderItemId;
	long orderId;
	String buyerId;
	String itemName;
	int itemCount;
	int itemPrice;
	
}

@Mapper(withIgnoreMissing = IgnoreMissing.ALL, withIoC = IoC.SPRING)
public interface OrderItemEntityMapper {
	@Maps(withCustomFields = {
			@Field({"orderId", "orderId"}),
			@Field({"buyer.buyerId", "buyerId"}),
			@Field({"itemName", "itemName"}),
			@Field({"itemCount", "itemCount"}),
			@Field({"itemPrice", "itemPrice"})
	})
	OrderItemEntity asOrderItemEntity(Order order, OrderItem orderItem);
	
	@Maps(withCustomFields = {
			@Field({"orderItemId", "itemId"}),
			@Field({"itemName", "itemName"}),
			@Field({"itemCount", "itemCount"}),
			@Field({"itemPrice", "itemPrice"})
	})
	OrderItem asOrderItem(OrderItemEntity orderItemEntity);
}
```
OrderItemEntity asOrderItemEntity(Order order, OrderItem orderItem) 의 경우 Order 와 OrderItem 클래스를 소스로 사용하여 매핑을 하고 있다.

@Field 어노테이션이 말해주는 것처럼 필드명을 나열하면 selma 가 알아서 매핑해 준다. 

buyer.buyerid 의 경우 Order 클래스의 buyer 필드 클래스의 buyerId 프로퍼티이다. 


