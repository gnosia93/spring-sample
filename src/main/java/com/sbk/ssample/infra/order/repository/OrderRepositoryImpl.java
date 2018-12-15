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


//@Component("OrderRepository")
@Component
public class OrderRepositoryImpl implements OrderRepository {

	OrderJpaRepository orderJpaRepository;
	OrderItemJpaRepository orderItemJpaRepository;
	
	@Autowired
	public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository, 
							   OrderItemJpaRepository orderItemJpaRepository) {
	
		this.orderJpaRepository = orderJpaRepository;
		this.orderItemJpaRepository = orderItemJpaRepository;
	}

	
	@Override
	public long save(Order order) {
		/*
		 * Order --> OrderEntity
		 * Order --> OrderItem List
		 */
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setBuyerId(order.getBuyer().getBuyerId());
		orderEntity.setMemberId(order.getBuyer().getMemberId());
		orderEntity.setBuyerType(order.getBuyer().getBuyerType());
		
		orderEntity.setItemCount(order.getItemList().size());
		
		orderEntity.setStatus(order.getOrderStatus());
		orderEntity.setTotalPrice(order.getTotalPrice());

		orderEntity.setReceiverName(order.getShippinInfo().getReciverName());
		orderEntity.setReceiverPhoneNumber(order.getShippinInfo().getReciverPhoneNumber());
		orderEntity.setReceiverAddr1(order.getShippinInfo().getRecieverAddr1());
		orderEntity.setReceiverAddr2(order.getShippinInfo().getReceiverAddr2());
		
		OrderEntity savedOrderEntity = this.orderJpaRepository.save(orderEntity);
		
		for(OrderItem item : order.getItemList()) {
			OrderItemEntity orderItemEntity = 
									new OrderItemEntity(0, savedOrderEntity.getId(), 
														order.getBuyer().getBuyerId(), 
														item.getItemName(), 
														item.getItemCount(),
														item.getItemPrice());
			this.orderItemJpaRepository.save(orderItemEntity);
		}
		
		return savedOrderEntity.getId();
	}
	
	@Override
	public Optional<Order> findById(long id) {
		Order order = null;
		Optional<OrderEntity> optOrderEntity = orderJpaRepository.findById(id);
		if(optOrderEntity.isPresent()) {
			List<OrderItemEntity> orderItemEntityList = orderItemJpaRepository.findByOrderId(optOrderEntity.get().getId());
				
			List<OrderItem> itemList = new ArrayList<>();
			for(OrderItemEntity orderItemEntity : orderItemEntityList) {
				OrderItem orderItem = new OrderItem(
						orderItemEntity.getId(), 
						orderItemEntity.getItemName(),
						orderItemEntity.getItemCount(),
						orderItemEntity.getItemPrice());
				itemList.add(orderItem);
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
