package com.sbk.ssample.ui.order.request.mapper;

import com.sbk.ssample.app.order.service.command.AddOrderCommand;
import com.sbk.ssample.ui.order.request.AddOrderRequest;

import fr.xebia.extras.selma.IgnoreMissing;
import fr.xebia.extras.selma.IoC;
import fr.xebia.extras.selma.Mapper;

@Mapper(withIgnoreMissing = IgnoreMissing.ALL, withIoC = IoC.SPRING)
public interface AddOrderRequestMapper {
	
	AddOrderCommand asAddOrderCommand(AddOrderRequest request);

}
