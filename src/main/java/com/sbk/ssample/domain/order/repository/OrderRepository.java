package com.sbk.ssample.domain.order.repository;

import com.sbk.ssample.app.order.service.command.AddOrderCommand;

public interface OrderRepository {

	public void save(AddOrderCommand addOrderCommand);
	
}
