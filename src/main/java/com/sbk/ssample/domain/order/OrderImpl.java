package com.sbk.ssample.domain.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sbk.ssample.app.order.service.command.AddOrderCommand;
import com.sbk.ssample.base.CommandResult;
import com.sbk.ssample.domain.order.repository.OrderRepository;

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
