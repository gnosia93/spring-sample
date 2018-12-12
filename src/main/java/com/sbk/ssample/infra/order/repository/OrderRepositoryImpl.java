package com.sbk.ssample.infra.order.repository;

import org.springframework.beans.factory.annotation.Autowired;

import com.sbk.ssample.app.domain.order.repository.OrderRepository;
import com.sbk.ssample.app.service.order.command.AddOrderCommand;
import com.sbk.ssample.infra.order.jpa.entity.OrderEntity;
import com.sbk.ssample.infra.order.jpa.repository.OrderItemJpaRepository;
import com.sbk.ssample.infra.order.jpa.repository.OrderJpaRepository;

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
	public void save(AddOrderCommand addOrderCommand) {
		
		OrderEntity orderEntity = null;
		System.out.println("infra.. OrderRepository Impl..");
		
		
		//this.orderJpaRepository.save(entity)
	
	}

}
