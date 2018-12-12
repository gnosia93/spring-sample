package com.sbk.ssample.infra.order.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbk.ssample.infra.order.jpa.entity.OrderItemEntity;

@Repository
public interface OrderItemJpaRepository extends JpaRepository<OrderItemEntity, Long> {

	
	
}
