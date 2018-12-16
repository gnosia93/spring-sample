package com.sbk.ssample.infra.order.mapper;


import com.sbk.ssample.app.domain.order.Order;
import com.sbk.ssample.infra.order.jpa.entity.OrderEntity;
import com.sbk.ssample.infra.order.jpa.entity.OrderItemEntity;

import fr.xebia.extras.selma.Field;
import fr.xebia.extras.selma.IgnoreMissing;
import fr.xebia.extras.selma.InheritMaps;
import fr.xebia.extras.selma.IoC;
import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.Maps;

@Mapper(withIgnoreMissing = IgnoreMissing.ALL, withIoC = IoC.SPRING)
public interface OrderEntityMapper {
	
	@Maps(
			withCustomFields = {
					@Field({"buyer.buyerId", "buyerId"}),
					@Field({"buyer.memberId", "memberId"}), 
					@Field({"buyer.buyerType", "buyerType"}),
					@Field({"orderStatus", "status"}),
					@Field({"totalPrice", "totalPrice"}),
					@Field({"shippingInfo.receiverName", "receiverName"}),
					@Field({"shippingInfo.receiverPhoneNumber", "receiverPhoneNumber"}),
					@Field({"shippingInfo.receiverAddr1", "receiverAddr1"}),
					@Field({"shippingInfo.receiverAddr2", "receiverAddr2"})
				}
			
	)
	OrderEntity asOrderEntity(Order order);

	// ????
	Order asOrder(OrderEntity orderEntity, OrderItemEntity orderItemEntity);
	
}

