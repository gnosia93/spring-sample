package com.sbk.ssample.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.AfterTransaction;

import com.sbk.ssample.app.domain.order.BuyerType;
import com.sbk.ssample.app.domain.order.OrderStatus;
import com.sbk.ssample.infra.order.jpa.entity.OrderEntity;
import com.sbk.ssample.infra.order.jpa.repository.OrderJpaRepository;
import com.zaxxer.hikari.HikariDataSource;




/*
 * 레포지토리 테스트를 해 @DataJpaTest 어노테이션을 사용하고, @AutoConfigureTestDatabase(replace = Replace.NONE) 사용하여
 * custom DBMS을 사용하도록 한다. 
 * @DataJpaTest 어노테이션만 사용하는 경우  H2 메모리 데이터베이스를 사용하게 된다. (POM 에 의존성 추가 필요)
 * https://www.baeldung.com/spring-boot-testing
 * 
 * 트랜잭션 매니저 설정을 누락한 경우 @DataJapTest 사용하더라도, 자동 롤백이 되지 않는다.
 * @AutoConfigureTestDatabase(replace = Replace.NONE)과 @DataJpaTest 을 동시에 사용하는 경우 롤백이 되지 않으며,
 * @DataJpaTest 만 사용하는 경우 내장 메모리 DB를 사용하게 되므로 롤백된다. 
 * 
 * 아래는 트랜잭션 매니저를 설정하는 코드로 클래스로 만들어서 @Configuration 어노테이션을 붙여주면 스프링 부트에서 자동 설정된다.  
 * 
 *  @Configuration
    // @EnableTransactionManagement
	public class DBConfig {
	
	
		// 데이터소스를 만들기 위해 커넥션풀 프로퍼티 객체를 반환하는 빈을 선언합니다.
	    // 해당 빈을 만들 때 @ConfigurationProperties 어노테이션을 사용합니다.
	    // "prefix" 값을 이용해 해당 prefix로 시작하는 값들을 주입 받습니다.
		@Bean
		@ConfigurationProperties("spring.datasource")
		public HikariDataSource dataSource() {
			return DataSourceBuilder.create().type(HikariDataSource.class).build();
		}
	
	    //트랜잭션 매니저 설정   --> 이 설정이 존재해야 한다. 없으면 Rollback 이 안된다. 
	    @Bean
	    public DataSourceTransactionManager transactionManager() {
	        DataSourceTransactionManager manager = new DataSourceTransactionManager(dataSource());
	        return manager;
	    }
	}

	[application.properties]
	 --> hikia cp 는 jdbc-url 이라는 키워드를 사용하여 db url 을 설정한다.
	 
    spring.jpa.hibernate.ddl-auto=create
	spring.datasource.jdbc-url=jdbc:mysql://localhost:3306/sample?serverTimezone=UTC
	spring.datasource.username=sample
	spring.datasource.password=sample
	spring.datasource.maximum-pool-size=10

 */

//@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)       // MySQL을 사용하기 위해서 필요한 설정
public class OrderJpaRepositoryTest {
	
	@Autowired OrderJpaRepository orderJpaRepository;
	
	@Before
	public void showCountBefore() {
		System.err.println("before: " + orderJpaRepository.count());
	}
	
	@After
	public void showCountAfter() {
		System.err.println("after: " + orderJpaRepository.count());
	}
	
	@AfterTransaction
	public void showCountAfterTransaction() {
	     System.err.println("after tx: " + orderJpaRepository.count());
	}
	

	@Test
	public void insertTest() {
		
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setBuyerId("test-buyerid");
		orderEntity.setBuyerType(BuyerType.MEMBER);
		orderEntity.setItemCount(3);
		orderEntity.setMemberId("test-memberid");
		orderEntity.setReceiverAddr1("test-adr1");
		orderEntity.setReceiverAddr2("test-adr2");
		orderEntity.setReceiverName("test-reciever");
		orderEntity.setReceiverPhoneNumber("000=000-000");
		orderEntity.setStatus(OrderStatus.ORDERED);
		orderEntity.setTotalPrice(30000);
		
		OrderEntity savedOrderEntity = orderJpaRepository.save(orderEntity);
		assertThat(orderJpaRepository.getOne(savedOrderEntity.getOrderId()), equalTo(orderEntity));
	}

	@Test
	public void updateTest() {
		
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setBuyerId("test-buyerid");
		orderEntity.setBuyerType(BuyerType.MEMBER);
		orderEntity.setItemCount(3);
		orderEntity.setMemberId("test-memberid");
		orderEntity.setReceiverAddr1("test-adr1");
		orderEntity.setReceiverAddr2("test-adr2");
		orderEntity.setReceiverName("test-reciever");
		orderEntity.setReceiverPhoneNumber("11-111-111");
		orderEntity.setStatus(OrderStatus.ORDERED);
		orderEntity.setTotalPrice(30000);
		
		// first create
		orderJpaRepository.save(orderEntity);
		
		// second update
		orderEntity.setBuyerId("modified-test-buyerid");
		orderEntity.setStatus(OrderStatus.COMPLETE);
		orderEntity.setTotalPrice(100);
		orderJpaRepository.save(orderEntity);
		
		assertThat(orderJpaRepository.getOne(orderEntity.getOrderId()), equalTo(orderEntity));
		assertThat(orderJpaRepository.getOne(orderEntity.getOrderId()).getTotalPrice(), equalTo(100));
	}
	
	
}
