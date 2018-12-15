package com.sbk.ssample.app.domain.order.repository;

import java.util.Optional;

import com.sbk.ssample.app.domain.order.Order;

public interface OrderRepository {

	public long save(Order order);
	
	public Optional<Order> findById(long id);
	
	
}
