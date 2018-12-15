package com.sbk.ssample.infra.order.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sbk.ssample.app.domain.order.Buyer;
import com.sbk.ssample.app.domain.order.Order;
import com.sbk.ssample.app.domain.order.OrderItem;
import com.sbk.ssample.app.domain.order.ShippingInfo;
import com.sbk.ssample.app.domain.order.repository.OrderRepository;
import com.sbk.ssample.infra.order.jpa.entity.OrderEntity;
import com.sbk.ssample.infra.order.jpa.entity.OrderItemEntity;
import com.sbk.ssample.infra.order.jpa.repository.OrderItemJpaRepository;
import com.sbk.ssample.infra.order.jpa.repository.OrderJpaRepository;
import com.sbk.ssample.infra.order.mapper.OrderEntityMapper;
import com.sbk.ssample.infra.order.mapper.OrderItemEntityMapper;


//@Component("OrderRepository")
@Component
public class OrderRepositoryImpl implements OrderRepository {

	OrderJpaRepository orderJpaRepository;
	OrderItemJpaRepository orderItemJpaRepository;
	OrderEntityMapper orderEntityMapper;
	OrderItemEntityMapper orderItemEntityMapper;
	
	@Autowired
	public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository, 
							   OrderItemJpaRepository orderItemJpaRepository,
							   OrderEntityMapper orderEntityMapper,
							   OrderItemEntityMapper orderItemEntityMapper) {
	
		this.orderJpaRepository = orderJpaRepository;
		this.orderItemJpaRepository = orderItemJpaRepository;
		this.orderEntityMapper = orderEntityMapper;
		this.orderItemEntityMapper = orderItemEntityMapper;
	}

	
	@Override
	public long save(Order order) {
	
		OrderEntity orderEntity = this.orderEntityMapper.asOrderEntity(order);
		orderEntity.setItemCount(order.getItemCount());
		
		OrderEntity savedOrderEntity = this.orderJpaRepository.save(orderEntity);
		order.setOrderId(savedOrderEntity.getOrderId());
		
		for(OrderItem orderItem : order.getItemList()) {
			this.orderItemJpaRepository.save(
					this.orderItemEntityMapper.asOrderItemEntity(order, orderItem));
		}
		
		return order.getOrderId();
	}
	
	@Override
	public Optional<Order> findById(long id) {
		Order order = null;
		Optional<OrderEntity> optOrderEntity = orderJpaRepository.findById(id);
		if(optOrderEntity.isPresent()) {
			List<OrderItemEntity> orderItemEntityList = 
					orderItemJpaRepository.findByOrderId(optOrderEntity.get().getOrderId());
				
			List<OrderItem> itemList = new ArrayList<>();
			for(OrderItemEntity orderItemEntity : orderItemEntityList) {
				
				/*
				OrderItem orderItem = new OrderItem(
						orderItemEntity.getOrderId(), 
						orderItemEntity.getItemName(),
						orderItemEntity.getItemCount(),
						orderItemEntity.getItemPrice());
						
				itemList.add(orderItem);		
				*/
				itemList.add(this.orderItemEntityMapper.asOrderItem(orderItemEntity));
			}
			
			Buyer buyer = new Buyer(optOrderEntity.get().getBuyerId(), 
						optOrderEntity.get().getBuyerType(), 
						optOrderEntity.get().getMemberId());
				
			ShippingInfo shippingInfo = new ShippingInfo(
					optOrderEntity.get().getReceiverName(),
					optOrderEntity.get().getReceiverName(),
					optOrderEntity.get().getReceiverAddr1(),
					optOrderEntity.get().getReceiverAddr2());
			
			order = new Order(buyer, itemList, shippingInfo);
		}
		
		return Optional.ofNullable(order);
	}
	
	

}
