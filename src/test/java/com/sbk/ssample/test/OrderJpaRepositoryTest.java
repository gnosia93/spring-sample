package com.sbk.ssample.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sbk.ssample.app.domain.order.BuyerType;
import com.sbk.ssample.app.domain.order.OrderStatus;
import com.sbk.ssample.infra.order.jpa.entity.OrderEntity;
import com.sbk.ssample.infra.order.jpa.repository.OrderJpaRepository;




/*
 * 레포지토리 테스트를 해 @DataJpaTest 어노테이션을 사용하고, @AutoConfigureTestDatabase(replace = Replace.NONE) 사용하여
 * application.properties 에 설정된 MySQL을 사용하도록 한다. 
 * @DataJpaTest 어노테이션만 사용하는 경우  H2 메모리 데이터베이스를 사용하게 되는 해당 메모리 DBMS 사용 방법에 대해서는 아래 URL 참조.
 * https://www.baeldung.com/spring-boot-testing
 */

//@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)       // MySQL을 사용하기 위해서 필요한 설정
public class OrderJpaRepositoryTest {
	
	@Autowired OrderJpaRepository orderJpaRepository;
	
	@Before
	public void showCountBefore() {
		System.err.println(orderJpaRepository.count());
	}
	
	@After
	public void showCountAfter() {
		System.err.println(orderJpaRepository.count());
	}

	
	
	@Test
	@Rollback
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
		
	}
	
	@Test
	public void deleteTest() {
		
	//	orderJpaRepository.deleteById(2L);
	}
	
	

	
}
