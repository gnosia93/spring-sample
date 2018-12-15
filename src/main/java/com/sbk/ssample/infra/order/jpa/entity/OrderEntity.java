package com.sbk.ssample.infra.order.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sbk.ssample.app.domain.order.BuyerType;
import com.sbk.ssample.app.domain.order.OrderStatus;
import com.sbk.ssample.app.domain.order.ShippingInfo;

import lombok.Data;

@Entity
@Table(name="tb_order")
@Data
public class OrderEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	long orderId;

	// Buyer
	String buyerId;
	String memberId;
	BuyerType buyerType;
	
	int itemCount;
	int totalPrice;
	@Enumerated(value=EnumType.STRING)
	OrderStatus status;
	
	// ShippingInfo
	String receiverName;
	String receiverPhoneNumber;
	String receiverAddr1;
	String receiverAddr2;;
}
