package com.sbk.ssample.infra.order.mapper;

import com.sbk.ssample.app.domain.order.Order;
import com.sbk.ssample.app.domain.order.OrderItem;
import com.sbk.ssample.infra.order.jpa.entity.OrderItemEntity;

import fr.xebia.extras.selma.Field;
import fr.xebia.extras.selma.IgnoreMissing;
import fr.xebia.extras.selma.IoC;
import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.Maps;


@Mapper(withIgnoreMissing = IgnoreMissing.ALL, withIoC = IoC.SPRING)
public interface OrderItemEntityMapper {
	@Maps(withCustomFields = {
			@Field({"orderId", "orderId"}),
			@Field({"buyer.buyerId", "buyerId"}),
			@Field({"itemName", "itemName"}),
			@Field({"itemCount", "itemCount"}),
			@Field({"itemPrice", "itemPrice"})
	})
	OrderItemEntity asOrderItemEntity(Order order, OrderItem orderItem);
	
	@Maps(withCustomFields = {
			@Field({"orderItemId", "itemId"}),
			@Field({"itemName", "itemName"}),
			@Field({"itemCount", "itemCount"}),
			@Field({"itemPrice", "itemPrice"})
	})
	OrderItem asOrderItem(OrderItemEntity orderItemEntity);
	
}


