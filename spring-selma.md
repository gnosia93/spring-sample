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
