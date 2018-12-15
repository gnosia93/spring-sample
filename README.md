### spring-sample
스프링 부트 단위 테스트를 spock 을 이용한 예제이다. 

### Prerequisite
1. 이클립스에서 spring boot 프로젝트를 생성한다.
1. jspresso 를 이용하여 spock nature 을 enable 시킨다.
1. 팝업 convert 메뉴에서 groovy 프로젝트로 convert 한다. 
1. spring boot 어플리케이션 시작 테스트를 한다. 
1. src/test/java 디렉토리에 샘플 spock 테스트 케이스를 아래와 같이 작성하고, maven 에 spock-core 라이브러리에 대한 의존성을 추가한다. 
  
  1.maven 의존추가
  ```
	<dependency>
	   <groupId>org.spockframework</groupId>
	    <artifactId>spock-core</artifactId>
	    <version>1.0-groovy-2.4</version>
	    <scope>test</scope>
	</dependency>
  ```		
  
  2.spock testcase 생성 -> /src/test/java 디렉토리에 
 
```
  package com.sbk.springsample`
  import spock.lang.Specification

  class ControllerTest extends Specification {

    def "groovy test"() {
      given:
        int left = 2
        int right = 2

      when:
        int result = left + right

      then:
        result == 4
    }
  }
```
  

### boot testing ###
#### 테스트팅 튜토리얼 ####
https://www.baeldung.com/spring-boot-testing

#### Mockito ####
http://www.vogella.com/tutorials/Mockito/article.html


#### 부트 테스트 레퍼런스 ####
https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html

  
MVC 단위 테스트 예제
https://memorynotfound.com/unit-test-spring-mvc-rest-service-junit-mockito/#unit-test-http-post




## Cache ##
#### Redis ####
https://www.baeldung.com/spring-data-redis-tutorial
https://github.com/spring-projects/spring-data-examples/tree/master/redis/repositories
https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/

#### ehcache ####


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


    
