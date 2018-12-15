package com.sbk.ssample.app.service.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.sbk.ssample.app.domain.order.ErrorCode;
import com.sbk.ssample.app.domain.order.Order;
import com.sbk.ssample.app.domain.order.exception.DomainException;
import com.sbk.ssample.app.domain.order.repository.OrderRepository;
import com.sbk.ssample.app.service.order.command.AddOrderCommand;
import com.sbk.ssample.base.CommandResult;

@Service
public class OrderService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	OrderRepository orderRepository;
	
	@Autowired
	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}
	
	public CommandResult order(AddOrderCommand addOrderCommand) {
		/*
		 * 파라미터 값에 대한 검증을 누가 해야 하는게 맞는가 ?
		 * ParameterValidationException ??? 
		 */
		if(addOrderCommand == null)
			throw new DomainException(ErrorCode.PARAM_IS_NULL);
	
		if(StringUtils.isEmpty(addOrderCommand.getBuyer()))
			throw new DomainException(ErrorCode.PARAM_IS_NULL, "(buyer is null)");
		if(addOrderCommand.getItemList() == null ||
		   addOrderCommand.getItemList().size() == 0)
			throw new DomainException(ErrorCode.PARAM_IS_NULL, "(item is null)");
		if(addOrderCommand.getShippingInfo() == null)
			throw new DomainException(ErrorCode.PARAM_IS_NULL, "(shoppinginfo is null)");
		// end of parameter validation.	

		
		Order order = new Order(addOrderCommand.getBuyer(), 
				addOrderCommand.getItemList(), addOrderCommand.getShippingInfo());
	
		orderRepository.save(order);
		return CommandResult.success();
	}

}
