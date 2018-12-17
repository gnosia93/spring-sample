package com.sbk.ssample.app.domain.order.repository;

import java.util.Optional;

import com.sbk.ssample.app.domain.order.Order;

public interface OrderRepository {

	public long save(Order order);
	
	public void cancelOrder(Order order);
	
	public Optional<Order> findById(long id);
	
	public long getCountOrderItem(long orderId);
	
	public long count();
	
}
