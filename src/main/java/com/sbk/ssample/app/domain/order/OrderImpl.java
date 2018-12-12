package com.sbk.ssample.app.domain.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sbk.ssample.app.domain.order.repository.OrderRepository;
import com.sbk.ssample.app.service.order.command.AddOrderCommand;
import com.sbk.ssample.base.CommandResult;

@Component
public class OrderImpl implements Order {

	OrderRepository orderRepository;
	
	@Autowired
	public OrderImpl(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
		
	}
	
	
	@Override
	public CommandResult order(AddOrderCommand addOrderCommand) {

		this.orderRepository.save(addOrderCommand);	
		
		return CommandResult.success();
	}
}
