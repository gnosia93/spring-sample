package com.sbk.ssample.infra.order.mapper;


import com.sbk.ssample.app.domain.order.Order;
import com.sbk.ssample.infra.order.jpa.entity.OrderEntity;

import fr.xebia.extras.selma.Field;
import fr.xebia.extras.selma.IgnoreMissing;
import fr.xebia.extras.selma.IoC;
import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.Maps;

@Mapper(
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
		},
		withIgnoreMissing = IgnoreMissing.ALL, withIoC = IoC.SPRING)
public interface OrderEntityMapper {
	
	OrderEntity asOrderEntity(Order order);
	
  //  Order asOrder(OrderEntity orderEntity);
	
}

