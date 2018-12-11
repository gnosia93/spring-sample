package com.sbk.springsample.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sbk.springsample.order.domain.Order;

@Service
public class OrderService {

	Order order;
	
	@Autowired
	public OrderService(Order order) {
		this.order = order;
	}
	
	public void order( ) {
		order.order();
	}
	
	
	
}
