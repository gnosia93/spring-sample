package com.sbk.ssample.app.domain.order.repository;

import com.sbk.ssample.app.service.order.command.AddOrderCommand;

public interface OrderRepository {

	public void save(AddOrderCommand addOrderCommand);
	
}
