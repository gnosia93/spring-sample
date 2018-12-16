package com.sbk.ssample.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sbk.ssample.app.domain.order.Buyer;
import com.sbk.ssample.app.domain.order.BuyerType;
import com.sbk.ssample.app.domain.order.Order;
import com.sbk.ssample.app.domain.order.OrderItem;
import com.sbk.ssample.app.domain.order.ShippingInfo;
import com.sbk.ssample.app.domain.order.repository.OrderRepository;


@RunWith(SpringRunner.class)
@SpringBootTest
//@DataJpaTest
//@AutoConfigureMockMvc               		 // mock object 를 autowire 하기 위해 추가 
public class OrderRepositoryTest {

	@Autowired OrderRepository orderRepository;
	
	public Order prepareOrder() {
		Buyer buyer = new Buyer("testbuyerId", BuyerType.NON_MEMBER, "testMemberId");
		List<OrderItem> itemList = new ArrayList<>();
		for(int i = 0; i < 5; i++) {
			OrderItem item = new OrderItem(i, "itemName"+i, i+1, 1000* i);
			itemList.add(item);
		}
		ShippingInfo shippingInfo = new ShippingInfo("test-name", "test-phoneNumber", "test-addr1", "test-addr2");
		
		return new Order(buyer, itemList, shippingInfo);
	}
	
	
	@Test
	public void saveAndFind() {
		long id = orderRepository.save(prepareOrder());
		
		// 입력된 것인지.. 확인한다. 
		Optional<Order> optOrder = orderRepository.findById(id);
		
		assertThat(BuyerType.NON_MEMBER, equalTo(optOrder.get().getBuyer().getBuyerType()));
		assertThat(5, equalTo(optOrder.get().getItemList().size()));
	}

	@Test(expected=RuntimeException.class)
	public void EnumColumnTest() {
		Optional<Order> optOrder = orderRepository.findById(2);
		optOrder.orElseThrow(() -> {throw new RuntimeException();});
	}
	
}
