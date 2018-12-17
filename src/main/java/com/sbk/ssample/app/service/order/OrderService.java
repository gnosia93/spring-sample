package com.sbk.ssample.app.service.order;

import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbk.ssample.app.domain.order.ErrorCode;
import com.sbk.ssample.app.domain.order.Order;
import com.sbk.ssample.app.domain.order.exception.DomainException;
import com.sbk.ssample.app.domain.order.repository.OrderRepository;
import com.sbk.ssample.app.service.order.command.AddOrderCommand;
import com.sbk.ssample.app.service.order.command.CancelOrderCommand;
import com.sbk.ssample.base.CommandResult;

@Service
public class OrderService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	OrderRepository orderRepository;
	RefundService refundService;
	
	@Autowired
	public OrderService(OrderRepository orderRepository, RefundService refundService) {
		this.orderRepository = orderRepository;
		this.refundService = refundService;
	}
	
	@Transactional
	public CommandResult order(AddOrderCommand addOrderCommand) {
		/*
		 * Command 에 대해 NULL 여부만 체크한다. 
		 */
		if(addOrderCommand == null)
			throw new DomainException(ErrorCode.PARAM_IS_NULL);
	
		// 도메인 생성자에서 각 파라미터에 대한 Validation 체크 수행.
		Order order = new Order(addOrderCommand.getBuyer(), 
				addOrderCommand.getItemList(), addOrderCommand.getShippingInfo());
	
		orderRepository.save(order);
		return CommandResult.success();
	}

	/**
	 * @param cancelOrderCommand
	 * @return
	 */
	@Transactional
	public CommandResult cancelOrder(CancelOrderCommand cancelOrderCommand) {	
		
		if(cancelOrderCommand == null)
			throw new DomainException(ErrorCode.PARAM_IS_NULL);
		
		Order order = orderRepository.findById(cancelOrderCommand.getOrderId())
				.orElseThrow(() -> new DomainException(ErrorCode.ORDER_NO_EXIST, cancelOrderCommand.toString()));
		
		if(!cancelOrderCommand.getBuyer().getBuyerId().equals(order.getBuyer().getBuyerId()))
			throw new DomainException(ErrorCode.ORDER_BUYER_ID_MISSMATCH, cancelOrderCommand.getBuyer().toString() + "  " +
					order.getBuyer().getBuyerId());	
		
		order.cancel(refundService);
		orderRepository.cancelOrder(order);
	
		return CommandResult.success(cancelOrderCommand.getOrderId() + " order is canceled");
	}
	
	public CommandResult findOrderById(long orderId) {
		
		return CommandResult.success(orderRepository.findById(orderId));
	}
	
	public long getOrderItemCount(long orderId) {
		return orderRepository.getCountOrderItem(orderId);
	}
	
	
		
	
}
