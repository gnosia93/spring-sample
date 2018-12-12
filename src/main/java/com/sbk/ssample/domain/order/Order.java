package com.sbk.ssample.domain.order;

import org.springframework.stereotype.Component;

import com.sbk.ssample.app.order.service.command.AddOrderCommand;
import com.sbk.ssample.base.CommandResult;

public interface Order {
	
	public CommandResult order(AddOrderCommand addOrderCommand);

	
}
