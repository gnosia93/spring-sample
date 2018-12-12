package com.sbk.ssample.app.domain.order;

import org.springframework.stereotype.Component;

import com.sbk.ssample.app.service.order.command.AddOrderCommand;
import com.sbk.ssample.base.CommandResult;

public interface Order {
	
	public CommandResult order(AddOrderCommand addOrderCommand);

	
}
