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
	public void cancelOrder(Order order) {
		
		/*
		 * 주문 취소시 Status 필드만 Cancel 로 업데이트 한다.  
		 * 주문 아이템 데이는 그대로 남겨둔다. 
		 */
		Optional<OrderEntity> optOrderEntity = orderJpaRepository.findById(order.getOrderId());
		optOrderEntity.ifPresent(orderEntity -> {
			orderJpaRepository.save(orderEntityMapper.asOrderEntity(order));
		});
		
	}
	
	
	@Override
	public long save(Order order) {
	
		OrderEntity orderEntity = this.orderEntityMapper.asOrderEntity(order);
		orderEntity.setItemCount(order.getItemCount());
		
		OrderEntity savedOrderEntity = this.orderJpaRepository.save(orderEntity);
		order.setOrderId(savedOrderEntity.getOrderId());
		
		System.out.println("....");
		System.out.println(savedOrderEntity.toString());
		
		
		for(OrderItem orderItem : order.getItemList()) {
			this.orderItemJpaRepository.save(
					this.orderItemEntityMapper.asOrderItemEntity(order, orderItem));
			
		}
		
		System.err.println(orderItemJpaRepository.count());
		
		return savedOrderEntity.getOrderId();
	}
	
	@Override
	public Optional<Order> findById(long id) {
		Order order = null;
		Optional<OrderEntity> optOrderEntity = orderJpaRepository.findById(id);
		if(optOrderEntity.isPresent()) {
			List<OrderItemEntity> orderItemEntityList = 
					orderItemJpaRepository.findByOrderId(optOrderEntity.get().getOrderId());
				
			List<OrderItem> itemList = new ArrayList<>();
			for(OrderItemEntity orderItemEntity : orderItemEntityList) 
				itemList.add(this.orderItemEntityMapper.asOrderItem(orderItemEntity));
			
		//	order = orderEntityMapper.asOrder(optOrderEntity.get());
		//	order.setItemList(itemList);
			
			Buyer buyer = new Buyer(optOrderEntity.get().getBuyerId(), 
						optOrderEntity.get().getBuyerType(), 
						optOrderEntity.get().getMemberId());
				
			ShippingInfo shippingInfo = new ShippingInfo(
					optOrderEntity.get().getReceiverName(),
					optOrderEntity.get().getReceiverPhoneNumber(),
					optOrderEntity.get().getReceiverAddr1(),
					optOrderEntity.get().getReceiverAddr2());
	
			order = Order.builder()
				.orderId(optOrderEntity.get().getOrderId())
				.buyer(buyer)
				.itemList(itemList)
				.orderStatus(optOrderEntity.get().getStatus())
				.shippingInfo(shippingInfo)
				.totalPrice(optOrderEntity.get().getTotalPrice())
				.build();
		}
		
		return Optional.ofNullable(order);
	}
	
	
	public long getCountOrderItem(long orderId) {
		
		return orderItemJpaRepository.countByOrderId(orderId);
	}
	
	public long count() {
		return orderJpaRepository.count();
	}
	

}
