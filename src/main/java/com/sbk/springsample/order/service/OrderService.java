package com.sbk.springsample.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbk.springsample.base.CommandResult;
import com.sbk.springsample.order.domain.Order;
import com.sbk.springsample.order.service.command.AddOrderCommand;

@Service
public class OrderService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	Order order;
	
	@Autowired
	public OrderService(Order order) {
		this.order = order;
	}
	
	public CommandResult order(AddOrderCommand addOrderCommand) {
	
		System.out.print(addOrderCommand);
		
		order.order();
		return CommandResult.success();
	}
	
	
}
