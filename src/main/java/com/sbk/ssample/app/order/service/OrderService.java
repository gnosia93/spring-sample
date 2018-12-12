package com.sbk.ssample.app.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbk.ssample.app.order.service.command.AddOrderCommand;
import com.sbk.ssample.base.CommandResult;
import com.sbk.ssample.domain.order.Order;

@Service
public class OrderService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	Order order;
	
	@Autowired
	public OrderService(Order order) {
		this.order = order;
	}
	
	public CommandResult order(AddOrderCommand addOrderCommand) {
	
		return order.order(addOrderCommand);
	
	}

}
