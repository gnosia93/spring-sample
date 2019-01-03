package com.sbk.ssample.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sbk.ssample.app.domain.order.Buyer;
import com.sbk.ssample.app.domain.order.BuyerType;
import com.sbk.ssample.app.domain.order.Order;
import com.sbk.ssample.app.domain.order.OrderItem;
import com.sbk.ssample.app.domain.order.OrderStatus;
import com.sbk.ssample.app.domain.order.ShippingInfo;
import com.sbk.ssample.app.service.order.OrderService;
import com.sbk.ssample.app.service.order.command.AddOrderCommand;
import com.sbk.ssample.app.service.order.command.CancelOrderCommand;
import com.sbk.ssample.ui.helper.CommandResult;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

	@Autowired OrderService orderService;	
	
	AddOrderCommand getAddOrderCommand(int itemCount) {
		
		List<OrderItem> orderItemList = new ArrayList<>();
		for(int i = 0; i < itemCount; i++) {
			OrderItem orderItem = new OrderItem(i, "itemName" +i, i+1, (i+1) * 100);
			orderItemList.add(orderItem);
		}
		
		Buyer buyer = new Buyer("buyerId_" + itemCount, BuyerType.MEMBER, "MEMB");
		ShippingInfo shippingInfo = new ShippingInfo("name", "000-00-000", "addr1,", "addr2");
	
		AddOrderCommand addOrderCommand = new AddOrderCommand();
		addOrderCommand.setBuyer(buyer);
		addOrderCommand.setItemList(orderItemList);
		addOrderCommand.setShippingInfo(shippingInfo);
		
		return addOrderCommand;
	}
	
	
	void prepareOrder(int maxOrderCount) {
		for(int order = 1; order <= maxOrderCount; order++) {
			AddOrderCommand addOrderCommand = getAddOrderCommand(order);
			orderService.order(addOrderCommand);
		}
		
	}
	
	@Test
	public void order() {
		prepareOrder(2);
	}
	
	
	@Test
	public void cancelOrder() {
	
		// given
		prepareOrder(3);
		
		int orderId = 3;
		// when
		CancelOrderCommand cancelOrderCommand = new CancelOrderCommand();
		cancelOrderCommand.setOrderId(orderId);
		cancelOrderCommand.setBuyer(new Buyer("buyerId_3", BuyerType.MEMBER, "MEMB"));
		orderService.cancelOrder(cancelOrderCommand);
		
		// then
		CommandResult commandResult = orderService.findOrderById(orderId);
		assertTrue(commandResult.isSuccess());
		
		Optional<Order> optOrder = (Optional<Order>)commandResult.getData();
		assertThat(optOrder.get().getOrderStatus(), equalTo(OrderStatus.CANCELED));
		assertThat(orderService.getOrderItemCount(orderId), equalTo(3L)); 
		
		
	}
	
	
}


